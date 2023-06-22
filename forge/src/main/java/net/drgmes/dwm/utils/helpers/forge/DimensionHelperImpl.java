package net.drgmes.dwm.utils.helpers.forge;

import com.google.common.collect.ImmutableList;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.network.client.DimensionAddPacket;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
import net.minecraft.server.WorldGenerationProgressListenerFactory;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.Unit;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.SaveProperties;
import net.minecraft.world.World;
import net.minecraft.world.biome.source.BiomeAccess;
import net.minecraft.world.border.WorldBorder;
import net.minecraft.world.border.WorldBorderListener;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.level.UnmodifiableLevelProperties;
import net.minecraft.world.level.storage.LevelStorage;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import java.util.function.Function;

public class DimensionHelperImpl {
    public static final Function<MinecraftServer, Map<RegistryKey<World>, ServerWorld>> WORLDS = getInstanceField(MinecraftServer.class, "worlds");
    public static final Function<MinecraftServer, WorldGenerationProgressListenerFactory> CHUNK_STATUS_LISTENER_FACTORY_FIELD = getInstanceField(MinecraftServer.class, "worldGenerationProgressListenerFactory");
    public static final Function<MinecraftServer, Executor> BACKGROUND_EXECUTOR_FIELD = getInstanceField(MinecraftServer.class, "workerExecutor");
    public static final Function<MinecraftServer, LevelStorage.Session> ANVIL_CONVERTER_FOR_ANVIL_FILE_FIELD = getInstanceField(MinecraftServer.class, "session");

    public static ServerWorld customGetOrCreateWorld(String id, MinecraftServer server, Consumer<ServerWorld> initialConsumer, Function<MinecraftServer, DimensionOptions> dimensionFactory) {
        Identifier worldIdentifier = DWM.getIdentifier(id);
        RegistryKey<World> worldKey = DimensionHelper.getWorldKey(worldIdentifier);
        ServerWorld world = server.getWorldRegistryKeys().contains(worldKey) ? server.getWorld(worldKey) : null;
        if (world != null) return world;

        DimensionOptions dimension = dimensionFactory.apply(server);
        WorldGenerationProgressListener chunkListener = CHUNK_STATUS_LISTENER_FACTORY_FIELD.apply(server).create(11);
        LevelStorage.Session levelSave = ANVIL_CONVERTER_FOR_ANVIL_FILE_FIELD.apply(server);
        Executor executor = BACKGROUND_EXECUTOR_FIELD.apply(server);

        SaveProperties serverConfig = server.getSaveProperties();
        GeneratorOptions dimensionGeneratorSettings = serverConfig.getGeneratorOptions();
        UnmodifiableLevelProperties derivedWorldInfo = new UnmodifiableLevelProperties(serverConfig, serverConfig.getMainWorldProperties());

        world = new ServerWorld(
            server,
            executor,
            levelSave,
            derivedWorldInfo,
            worldKey,
            dimension,
            chunkListener,
            false,
            BiomeAccess.hashSeed(dimensionGeneratorSettings.getSeed()),
            ImmutableList.of(),
            false,
            null
        );

        WorldBorder worldBorder = server.getOverworld().getWorldBorder();
        if (worldBorder != null) worldBorder.addListener(new WorldBorderListener.WorldBorderSyncer(world.getWorldBorder()));

        WORLDS.apply(server).put(worldKey, world);
        ModDimensions.addWorldToRegistry(server, worldKey);
        new DimensionAddPacket(worldKey).sendToAll(server);
        server.markWorldsDirty();

        if (initialConsumer != null) initialConsumer.accept(world);
        chunkListener.start(new ChunkPos(0, 0));
        ServerChunkManager chunkManager = world.getChunkManager();
        chunkManager.addRegionTicket(ChunkTicketType.START, new ChunkPos(0, 0), 11, Unit.INSTANCE, false);

        return world;
    }

    public static void customRemoveWorld(String id, MinecraftServer server) {
        // TODO
        throw new AssertionError();
    }

    @SuppressWarnings("unchecked")
    private static <FIELDHOLDER, FIELDTYPE> Function<FIELDHOLDER, FIELDTYPE> getInstanceField(Class<FIELDHOLDER> fieldHolderClass, String fieldName) {
        Field field = ObfuscationReflectionHelper.findField(fieldHolderClass, fieldName);

        return (instance) -> {
            try {
                return (FIELDTYPE) (field.get(instance));
            } catch (IllegalArgumentException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        };
    }
}

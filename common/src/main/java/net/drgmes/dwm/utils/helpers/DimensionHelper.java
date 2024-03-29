package net.drgmes.dwm.utils.helpers;

import com.google.common.collect.ImmutableList;
import dev.architectury.injectables.annotations.ExpectPlatform;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.compat.DimLib;
import net.drgmes.dwm.network.client.DimensionAddPacket;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.ModDimensions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.WorldGenerationProgressListener;
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

import java.util.function.Function;

public class DimensionHelper {
    public static String getWorldId(World world) {
        return world.getRegistryKey().getValue().getPath();
    }

    public static RegistryKey<World> getWorldKey(Identifier id) {
        return RegistryKey.of(RegistryKeys.WORLD, id);
    }

    public static RegistryKey<World> getWorldKey(String id) {
        return getWorldKey(new Identifier(id));
    }

    public static RegistryKey<World> getWorldKey(World world) {
        return getWorldKey(getWorldId(world));
    }

    public static ServerWorld getWorld(RegistryKey<World> worldKey, MinecraftServer server) {
        return server.getWorldRegistryKeys().contains(worldKey) ? server.getWorld(worldKey) : null;
    }

    public static ServerWorld getWorld(Identifier id, MinecraftServer server) {
        return getWorld(getWorldKey(id), server);
    }

    public static ServerWorld getWorld(String id, MinecraftServer server) {
        return getWorld(getWorldKey(id), server);
    }

    public static ServerWorld getModWorld(String id, MinecraftServer server) {
        return getWorld(DWM.getIdentifier(id), server);
    }

    public static ServerWorld getOrCreateWorld(String id, MinecraftServer server, Function<MinecraftServer, DimensionOptions> dimensionFactory) {
        ServerWorld world = getModWorld(id, server);
        if (world != null) return world;

        if (ModCompats.dimLib()) {
            return DimLib.createWorld(id, server, dimensionFactory);
        }

        RegistryKey<World> worldKey = getWorldKey(DWM.getIdentifier(id));
        DimensionOptions dimension = dimensionFactory.apply(server);
        WorldGenerationProgressListener chunkListener = server.worldGenerationProgressListenerFactory.create(11);

        SaveProperties serverConfig = server.getSaveProperties();
        GeneratorOptions dimensionGeneratorSettings = serverConfig.getGeneratorOptions();
        UnmodifiableLevelProperties derivedWorldInfo = new UnmodifiableLevelProperties(serverConfig, serverConfig.getMainWorldProperties());

        world = new ServerWorld(
            server,
            server.workerExecutor,
            server.session,
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

        server.worlds.put(worldKey, world);
        ModDimensions.addWorldToRegistry(server, worldKey);
        new DimensionAddPacket(worldKey).sendToAll(server);
        setChanged(server);

        chunkListener.start(new ChunkPos(0, 0));
        ServerChunkManager chunkManager = world.getChunkManager();
        chunkManager.addTicket(ChunkTicketType.START, new ChunkPos(0, 0), 11, Unit.INSTANCE);

        return world;
    }

    public static void removeWorld(String id, MinecraftServer server) {
        if (ModCompats.dimLib()) {
            DimLib.removeWorld(id, server);
            return;
        }

        // TODO ...

        RegistryKey<World> worldKey = getWorldKey(DWM.getIdentifier(id));
        ModDimensions.removeWorldFromRegistry(server, worldKey);
    }

    @ExpectPlatform
    public static void setChanged(MinecraftServer server) {
        throw new AssertionError();
    }
}

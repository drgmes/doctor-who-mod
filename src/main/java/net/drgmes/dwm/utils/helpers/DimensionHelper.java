package net.drgmes.dwm.utils.helpers;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;

import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.BiomeManager;
import net.minecraft.world.level.border.BorderChangeListener;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldGenSettings;
import net.minecraft.world.level.storage.DerivedLevelData;
import net.minecraft.world.level.storage.LevelStorageSource.LevelStorageAccess;
import net.minecraft.world.level.storage.WorldData;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.world.WorldEvent;

public class DimensionHelper {
    public static ServerLevel getLevel(MinecraftServer server, ResourceKey<Level> levelKey, Consumer<ServerLevel> initialConsumer, BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();
        if (map.containsKey(levelKey)) return map.get(levelKey);

        ServerLevel level = getDynamicWorldAndDimension(server, map, levelKey, dimensionFactory);
        initialConsumer.accept(level);
        return level;
    }

    public static ServerLevel getLevelStatic(MinecraftServer server, ResourceKey<Level> levelKey, BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();
        if (map.containsKey(levelKey)) return map.get(levelKey);

        return getWorldAndDimension(server, map, levelKey, dimensionFactory);
    }

    private static ServerLevel getDynamicWorldAndDimension(MinecraftServer server, Map<ResourceKey<Level>, ServerLevel> map, ResourceKey<Level> levelKey, BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        ServerLevel world = getWorldAndDimension(server, map, levelKey, dimensionFactory);
        // Network.sendPacketToAll(new SyncDimensionListMessage(levelKey, true));
        return world;
    }

    private static ServerLevel getWorldAndDimension(MinecraftServer server, Map<ResourceKey<Level>, ServerLevel> map, ResourceKey<Level> levelKey, BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        ResourceKey<LevelStem> dimensionKey = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, levelKey.location());
        LevelStem dimension = dimensionFactory.apply(server, dimensionKey);

        ChunkProgressListener chunkListener = server.progressListenerFactory.create(11);
        Executor executor = server.executor;
        LevelStorageAccess levelSave = server.storageSource;

        WorldData serverConfig = server.getWorldData();
        WorldGenSettings worldGenSettings = serverConfig.worldGenSettings();
        Registry<LevelStem> dimensionRegistry = worldGenSettings.dimensions();

        if (dimensionRegistry instanceof WritableRegistry<LevelStem> writableRegistry) {
            writableRegistry.register(dimensionKey, dimension, Lifecycle.stable());
        }
        else {
            throw new IllegalStateException("Unable to register dimension '" + dimensionKey.location() + "'! Registry not writable!");
        }

        DerivedLevelData derivedWorldInfo = new DerivedLevelData(serverConfig, serverConfig.overworldData());
        ServerLevel level = new ServerLevel(server, executor, levelSave, derivedWorldInfo, levelKey, dimension.typeHolder(), chunkListener, dimension.generator(), worldGenSettings.isDebug(), BiomeManager.obfuscateSeed(worldGenSettings.seed()), ImmutableList.of(), false);

        overworld.getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(level.getWorldBorder()));
        map.put(levelKey, level);
        server.markWorldsDirty();

        MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(level));
        return level;
    }
}

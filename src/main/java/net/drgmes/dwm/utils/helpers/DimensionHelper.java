package net.drgmes.dwm.utils.helpers;

import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.BiFunction;
import java.util.function.Consumer;

import com.google.common.collect.ImmutableList;
import com.mojang.serialization.Lifecycle;

import net.drgmes.dwm.DWM;
import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
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
    public static ResourceKey<Level> getLevelKey(String id) {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(id));
    }

    public static ResourceKey<Level> getModLevelKey(String id) {
        return ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(DWM.MODID, id));
    }

    @SuppressWarnings("deprecation")
    public static ServerLevel getLevel(MinecraftServer server, ResourceKey<Level> levelKey) {
        if (server == null) return null;
        Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();
        return map.containsKey(levelKey) ? map.get(levelKey) : null;
    }

    public static ServerLevel getLevel(MinecraftServer server, String id) {
        return DimensionHelper.getLevel(server, DimensionHelper.getModLevelKey(id));
    }

    public static ServerLevel getOrCreateLevel(MinecraftServer server, String id, Consumer<ServerLevel> initialConsumer, BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        ServerLevel level = DimensionHelper.getLevel(server, id);
        if (level != null) return level;

        level = createLevel(server, id, dimensionFactory);
        initialConsumer.accept(level);
        return level;
    }

    public static ServerLevel getOrCreateLevelStatic(MinecraftServer server, String id, BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        ServerLevel level = DimensionHelper.getLevel(server, id);
        return level != null ? level : createLevel(server, id, dimensionFactory);
    }

    @SuppressWarnings("deprecation")
    private static ServerLevel createLevel(MinecraftServer server, String id, BiFunction<MinecraftServer, ResourceKey<LevelStem>, LevelStem> dimensionFactory) {
        Map<ResourceKey<Level>, ServerLevel> map = server.forgeGetWorldMap();

        ServerLevel overworld = server.getLevel(Level.OVERWORLD);
        ResourceKey<Level> levelKey = DimensionHelper.getModLevelKey(id);
        ResourceKey<LevelStem> levelStemKey = ResourceKey.create(Registry.LEVEL_STEM_REGISTRY, levelKey.location());
        LevelStem levelStem = dimensionFactory.apply(server, levelStemKey);

        ChunkProgressListener chunkListener = server.progressListenerFactory.create(11);
        LevelStorageAccess levelSave = server.storageSource;
        Executor executor = server.executor;

        WorldData serverConfig = server.getWorldData();
        WorldGenSettings worldGenSettings = serverConfig.worldGenSettings();
        Registry<LevelStem> levelStemRegistry = worldGenSettings.dimensions();

        if (levelStemRegistry instanceof WritableRegistry<LevelStem> writableRegistry) {
            writableRegistry.register(levelStemKey, levelStem, Lifecycle.stable());
        }
        else {
            throw new IllegalStateException("Unable to register dimension '" + levelStemKey.location() + "'! Registry not writable!");
        }

        DerivedLevelData derivedLevelInfo = new DerivedLevelData(serverConfig, serverConfig.overworldData());
        ServerLevel level = new ServerLevel(server, executor, levelSave, derivedLevelInfo, levelKey, levelStem.typeHolder(), chunkListener, levelStem.generator(), worldGenSettings.isDebug(), BiomeManager.obfuscateSeed(worldGenSettings.seed()), ImmutableList.of(), false);

        overworld.getWorldBorder().addListener(new BorderChangeListener.DelegateBorderChangeListener(level.getWorldBorder()));
        map.put(levelKey, level);
        server.markWorldsDirty();

        MinecraftForge.EVENT_BUS.post(new WorldEvent.Load(level));
        return level;
    }
}

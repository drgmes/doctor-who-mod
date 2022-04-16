package net.drgmes.dwm.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.world.data.TardisWorldData;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

public class ModDimensions {
    public static final List<ResourceKey<Level>> TARDISES = new ArrayList<ResourceKey<Level>>();

    public static void init() {
    }

    public static void setup() {
        Registry.register(Registry.CHUNK_GENERATOR, DWM.LOCS.TARDIS, TardisChunkGenerator.CODEC);
    }

    public static LevelStem tardisDimensionBuilder(MinecraftServer server, ResourceKey<LevelStem> dimensionKey) {
        return new LevelStem(
            server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getHolderOrThrow(ModDimensionTypes.TARDIS),
            new TardisChunkGenerator(server)
        );
    }

    public static ServerLevel getTardisDimension(MinecraftServer server, String uuid) {
        ResourceKey<Level> levelKey = ResourceKey.create(Registry.DIMENSION_REGISTRY, new ResourceLocation(DWM.MODID, uuid));
        ServerLevel level = DimensionHelper.getLevel(server, levelKey, ModDimensions::tardisDimensionBuilder);

        if (server.getLevel(levelKey) != null) return level;
        TARDISES.add(levelKey);

        if (Events.DATA != null) {
            Events.DATA.setDirty();
        }

        return level;
    }

    public static void registerOldTardises(MinecraftServer server) {
        for (ResourceKey<Level> level : TARDISES) {
            if (server.getLevel(level) == null) {
                DimensionHelper.getLevelStatic(server, level, ModDimensions::tardisDimensionBuilder);

                if (!TARDISES.contains(level)) {
                    TARDISES.add(level);
                }
            }
        }
    }

    public static class ModDimensionTypes {
        public static final ResourceKey<DimensionType> TARDIS = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, DWM.LOCS.TARDIS);
    }

    @Mod.EventBusSubscriber(modid = DWM.MODID)
    public static class Events {
        static TardisWorldData DATA;

        @SubscribeEvent
        public static void saveTardisDimensions(WorldEvent.Save event) {
            if (!(event.getWorld() instanceof ServerLevel)) return;

            ServerLevel serverWorld = (ServerLevel) event.getWorld();
            if (serverWorld.dimension() == Level.OVERWORLD) {
                ((ServerLevel) event.getWorld()).getDataStorage().set("tardis", DATA);
            }
        }

        @SubscribeEvent
        public static void load(WorldEvent.Load event) {
            if (!(event.getWorld() instanceof ServerLevel)) return;

            ServerLevel serverWorld = (ServerLevel) event.getWorld();
            if (serverWorld.dimension() == Level.OVERWORLD) {
                DATA = ((ServerLevel) event.getWorld()).getDataStorage().computeIfAbsent(TardisWorldData::load, TardisWorldData::new, "tardis");
                registerOldTardises(serverWorld.getServer());
            }
        }

        @SubscribeEvent
        public static void unload(WorldEvent.Unload event) {
            if (!(event.getWorld() instanceof ServerLevel)) return;

            ServerLevel serverWorld = (ServerLevel) event.getWorld();
            if (serverWorld.dimension() == Level.OVERWORLD) {
                TARDISES.clear();
            }
        }
    }
}

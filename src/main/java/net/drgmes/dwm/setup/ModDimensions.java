package net.drgmes.dwm.setup;

import java.util.ArrayList;
import java.util.List;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.dimension.DimensionType;

public class ModDimensions {
    public static final List<String> TARDISES = new ArrayList<>();

    public static void init() {
    }

    public static void setup() {
        Registry.register(Registry.CHUNK_GENERATOR, DWM.LOCS.TARDIS, TardisChunkGenerator.CODEC);
    }

    public static class ModDimensionTypes {
        public static final ResourceKey<DimensionType> TARDIS = ResourceKey.create(Registry.DIMENSION_TYPE_REGISTRY, DWM.LOCS.TARDIS);
    }
}

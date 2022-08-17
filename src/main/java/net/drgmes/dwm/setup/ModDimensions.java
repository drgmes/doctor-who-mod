package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.dimension.DimensionType;

public class ModDimensions {
    public static void init() {
    }

    public static void setup() {
        Registry.register(Registry.CHUNK_GENERATOR, DWM.LOCS.TARDIS, TardisChunkGenerator.CODEC);
    }

    public static class ModDimensionTypes {
        public static final RegistryKey<DimensionType> TARDIS = RegistryKey.of(Registry.DIMENSION_TYPE_KEY, DWM.LOCS.TARDIS);
    }
}

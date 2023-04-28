package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.biome.Biome;

public class ModBiomes {
    public static final RegistryKey<Biome> TARDIS_KEY = RegistryKey.of(RegistryKeys.BIOME, DWM.getIdentifier("tardis"));

    public static void init() {
    }
}

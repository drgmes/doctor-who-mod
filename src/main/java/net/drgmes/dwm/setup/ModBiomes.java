package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.OverworldBiomeCreator;

public class ModBiomes {
    public static final Biome TARDIS = Registration.registerBiome("tardis", OverworldBiomeCreator::createTheVoid);
    public static final RegistryKey<Biome> TARDIS_KEY = RegistryKey.of(Registry.BIOME_KEY, DWM.getIdentifier("tardis"));

    public static void init() {
    }
}

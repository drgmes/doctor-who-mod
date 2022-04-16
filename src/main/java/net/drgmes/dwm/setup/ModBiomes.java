package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.minecraft.core.Registry;
import net.minecraft.data.worldgen.biome.OverworldBiomes;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.registries.RegistryObject;

public class ModBiomes {
    public static final RegistryObject<Biome> TARDIS = Registration.registerBiome("tardis", () -> OverworldBiomes.theVoid());
    public static final ResourceKey<Biome> TARDIS_KEY = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(DWM.MODID, "tardis"));

    public static void init() {
    }
}

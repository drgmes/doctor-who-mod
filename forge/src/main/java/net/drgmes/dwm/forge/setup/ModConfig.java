package net.drgmes.dwm.forge.setup;

import net.minecraftforge.fml.ModLoadingContext;

public class ModConfig {
    public static void setup() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, net.drgmes.dwm.setup.ModConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, net.drgmes.dwm.setup.ModConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, net.drgmes.dwm.setup.ModConfig.SERVER_SPEC);
    }
}

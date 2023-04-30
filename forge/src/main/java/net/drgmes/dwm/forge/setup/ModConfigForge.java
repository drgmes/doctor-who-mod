package net.drgmes.dwm.forge.setup;

import net.drgmes.dwm.setup.ModConfig;
import net.minecraftforge.fml.ModLoadingContext;

public class ModConfigForge {
    public static void setup() {
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
    }
}

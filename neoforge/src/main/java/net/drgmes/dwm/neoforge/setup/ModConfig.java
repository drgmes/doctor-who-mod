package net.drgmes.dwm.neoforge.setup;

import fuzs.forgeconfigapiport.neoforge.api.forge.v4.ForgeConfigRegistry;
import net.drgmes.dwm.DWM;

public class ModConfig {
    public static void setup() {
        ForgeConfigRegistry.INSTANCE.register(DWM.MODID, net.neoforged.fml.config.ModConfig.Type.COMMON, net.drgmes.dwm.setup.ModConfig.COMMON_SPEC);
        ForgeConfigRegistry.INSTANCE.register(DWM.MODID, net.neoforged.fml.config.ModConfig.Type.CLIENT, net.drgmes.dwm.setup.ModConfig.CLIENT_SPEC);
        ForgeConfigRegistry.INSTANCE.register(DWM.MODID, net.neoforged.fml.config.ModConfig.Type.SERVER, net.drgmes.dwm.setup.ModConfig.SERVER_SPEC);
    }
}

package net.drgmes.dwm.fabric.setup;

import fuzs.forgeconfigapiport.api.config.v2.ForgeConfigRegistry;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModConfig;

public class ModConfigFabric {
    public static void setup() {
        ForgeConfigRegistry.INSTANCE.register(DWM.MODID, net.minecraftforge.fml.config.ModConfig.Type.COMMON, ModConfig.COMMON_SPEC);
        ForgeConfigRegistry.INSTANCE.register(DWM.MODID, net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);
        ForgeConfigRegistry.INSTANCE.register(DWM.MODID, net.minecraftforge.fml.config.ModConfig.Type.SERVER, ModConfig.SERVER_SPEC);
    }
}

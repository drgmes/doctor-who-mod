package net.drgmes.dwm.fabric;

import net.drgmes.dwm.compat.immersiveportals.ImmersivePortalsAPI;
import net.drgmes.dwm.fabric.setup.*;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class DWM implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        if (ModCompats.immersivePortalsAPI()) ImmersivePortalsAPI.suppressExperimentalWarning();

        net.drgmes.dwm.setup.ModConfig.setup();
        ModConfig.setup();
        ModEvents.setup();
        ModCommands.setup();
        ModResourcePacks.setup();

        Registration.setupCommon();
    }

    @Override
    public void onInitializeClient() {
        Registration.setupClient();
        ModRenderers.setup();
    }

    @Override
    public void onInitializeServer() {
        Registration.setupServer();
    }
}

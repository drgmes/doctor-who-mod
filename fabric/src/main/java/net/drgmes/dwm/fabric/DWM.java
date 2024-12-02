package net.drgmes.dwm.fabric;

import net.drgmes.dwm.compat.dimlib.DimLib;
import net.drgmes.dwm.fabric.setup.*;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class DWM implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        if (ModCompats.dimLib()) DimLib.suppressExperimentalWarning();

        net.drgmes.dwm.setup.ModConfig.setup();
        ModConfig.setup();
        ModCommands.setup();
        ModResourcePacks.setup();

        Registration.setupCommon();
    }

    @Override
    public void onInitializeClient() {
        net.drgmes.dwm.setup.ModKeyBindings.setup();
        ModRenderers.setup();
        ModScreens.setup();

        Registration.setupClient();
    }

    @Override
    public void onInitializeServer() {
        Registration.setupServer();
    }
}

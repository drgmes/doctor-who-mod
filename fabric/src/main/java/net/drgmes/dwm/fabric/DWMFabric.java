package net.drgmes.dwm.fabric;

import net.drgmes.dwm.compat.DimLib;
import net.drgmes.dwm.fabric.setup.*;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;

public class DWMFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        if (ModCompats.dimLib()) DimLib.suppressExperimentalWarning();

        ModConfig.setup();
        ModConfigFabric.setup();
        ModEventsFabric.setup();
        ModResourcePacksFabric.setup();
        ModCommandsFabric.setup();

        Registration.setupCommon();
    }

    @Override
    public void onInitializeClient() {
        Registration.setupClient();
        ModRenderersFabric.setup();
    }

    @Override
    public void onInitializeServer() {
        Registration.setupServer();
    }
}

package net.drgmes.dwm.fabric;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.fabric.setup.*;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.DedicatedServerModInitializer;
import net.fabricmc.api.ModInitializer;
import qouteall.q_misc_util.LifecycleHack;

public class DWMFabric implements ModInitializer, ClientModInitializer, DedicatedServerModInitializer {
    @Override
    public void onInitialize() {
        LifecycleHack.markNamespaceStable(DWM.MODID);

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

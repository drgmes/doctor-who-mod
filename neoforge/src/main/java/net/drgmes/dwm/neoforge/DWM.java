package net.drgmes.dwm.neoforge;

import net.drgmes.dwm.neoforge.setup.ModCommands;
import net.drgmes.dwm.neoforge.setup.ModConfig;
import net.drgmes.dwm.neoforge.setup.ModRenderers;
import net.drgmes.dwm.setup.Registration;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLDedicatedServerSetupEvent;

@Mod(net.drgmes.dwm.DWM.MODID)
public class DWM {
    public DWM(IEventBus modEventBus) {
        modEventBus.addListener(DWM::setupClient);
        modEventBus.addListener(DWM::setupServer);

        net.drgmes.dwm.setup.ModConfig.setup();
        ModConfig.setup();
        ModCommands.setup();

        Registration.setupCommon();
    }

    public static void setupClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Registration.setupClient();
            ModRenderers.setup();
        });
    }

    public static void setupServer(FMLDedicatedServerSetupEvent event) {
        event.enqueueWork(() -> {
            Registration.setupServer();
        });
    }
}

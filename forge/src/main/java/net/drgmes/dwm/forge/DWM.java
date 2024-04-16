package net.drgmes.dwm.forge;

import dev.architectury.platform.forge.EventBuses;
import net.drgmes.dwm.forge.setup.ModCommands;
import net.drgmes.dwm.forge.setup.ModConfig;
import net.drgmes.dwm.forge.setup.ModRenderers;
import net.drgmes.dwm.setup.Registration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(net.drgmes.dwm.DWM.MODID)
public class DWM {
    public DWM() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(net.drgmes.dwm.DWM.MODID, modEventBus);
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

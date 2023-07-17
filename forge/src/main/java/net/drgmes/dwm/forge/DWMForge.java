package net.drgmes.dwm.forge;

import dev.architectury.platform.forge.EventBuses;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.forge.setup.ModBlockEntitiesForge;
import net.drgmes.dwm.forge.setup.ModCommandsForge;
import net.drgmes.dwm.forge.setup.ModConfigForge;
import net.drgmes.dwm.forge.setup.ModRenderersForge;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.Registration;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(DWM.MODID)
public class DWMForge {
    public DWMForge() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        EventBuses.registerModEventBus(DWM.MODID, modEventBus);
        modEventBus.addListener(DWMForge::setupClient);
        modEventBus.addListener(DWMForge::setupServer);

        ModConfig.setup();
        ModConfigForge.setup();
        ModCommandsForge.setup();

        Registration.setupCommon();
        ModBlockEntitiesForge.setup();
    }

    public static void setupClient(FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            Registration.setupClient();
            ModRenderersForge.setup();
        });
    }

    public static void setupServer(FMLDedicatedServerSetupEvent event) {
        event.enqueueWork(() -> {
            Registration.setupServer();
        });
    }
}

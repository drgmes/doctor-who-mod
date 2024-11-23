package net.drgmes.dwm.neoforge.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.engines.screens.TardisEngineSystemsScreen;
import net.drgmes.dwm.common.sonicdevice.modes.scan.SonicDeviceScanModeOverlay;
import net.drgmes.dwm.setup.ModInventories;
import net.drgmes.dwm.setup.ModKeys;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = DWM.MODID, value = Dist.CLIENT)
public class ModEvents {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderGuiOverlay(RenderGuiEvent.Pre event) {
        SonicDeviceScanModeOverlay.INSTANCE.render(event.getGuiGraphics());
    }

    @EventBusSubscriber(modid = DWM.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ModEventsSecondary {
        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void registerBindings(RegisterKeyMappingsEvent event) {
            ModKeys.setup();

            event.register(ModKeys.SONIC_DEVICE_SETTINGS);
            event.register(ModKeys.SONIC_SUNGLASSES_USAGE);
        }

        @SubscribeEvent
        @OnlyIn(Dist.CLIENT)
        public static void registerScreens(RegisterMenuScreensEvent event) {
            event.register(ModInventories.TARDIS_ENGINE.get(), TardisEngineSystemsScreen::new);
        }
    }
}

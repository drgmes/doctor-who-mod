package net.drgmes.dwm.forge.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.modes.scan.SonicDeviceScanModeOverlay;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ModEventsForge {
    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent event) {
        SonicDeviceScanModeOverlay.INSTANCE.render(event.getGuiGraphics());
    }
}

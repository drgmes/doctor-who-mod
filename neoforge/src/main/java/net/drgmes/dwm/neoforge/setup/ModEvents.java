package net.drgmes.dwm.neoforge.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.modes.scan.SonicDeviceScanModeOverlay;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ModEvents {
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent.Post event) {
        SonicDeviceScanModeOverlay.INSTANCE.render(event.getGuiGraphics());
    }
}

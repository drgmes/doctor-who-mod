package net.drgmes.dwm.forge.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicscrewdriver.modes.scan.SonicScrewdriverScanModeOverlay;
import net.drgmes.dwm.setup.ModDimensions;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.event.server.ServerStartedEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ModEventsForge {
    @SubscribeEvent
    public static void onServerStarted(ServerStartedEvent event) {
        ModDimensions.loadWorldsRegistry(event.getServer());
    }

    @SubscribeEvent
    public static void onRenderGuiOverlay(RenderGuiOverlayEvent event) {
        SonicScrewdriverScanModeOverlay.INSTANCE.render(event.getGuiGraphics());
    }
}

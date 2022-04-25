package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onLevelTick(TickEvent.WorldTickEvent event) {
        if (!event.world.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) return;
        event.world.getCapability(ModCapabilities.TARDIS_DATA).ifPresent(ITardisLevelData::tick);
    }
}

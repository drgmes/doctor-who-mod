package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisChunkLoader;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ModEvents {
    @SubscribeEvent
    public static void onLevelTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.world instanceof ServerLevel level)) return;

        level.getCapability(ModCapabilities.TARDIS_CHUNK_LOADER).ifPresent(ITardisChunkLoader::tick);

        if (level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) {
            level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent(ITardisLevelData::tick);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangeDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getPlayer() instanceof ServerPlayer serverPlayer) {
            serverPlayer.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                if (provider.isValid()) provider.updateConsoleTiles();
            });
        }
    }
}

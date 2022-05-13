package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisChunkLoader;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.world.data.TardisLevelData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = DWM.MODID)
public class ModEvents {
    ///////////////////////////////////////
    // START Providing Tardis dimensions //
    ///////////////////////////////////////

    public static TardisLevelData DATA;

    @SubscribeEvent
    public static void onLevelSave(WorldEvent.Save event) {
        if (!(event.getWorld() instanceof ServerLevel serverLevel)) return;

        if (serverLevel.dimension() == Level.OVERWORLD) {
            serverLevel.getDataStorage().set("tardis", DATA);
        }
    }

    @SubscribeEvent
    public static void onLevelLoad(WorldEvent.Load event) {
        if (!(event.getWorld() instanceof ServerLevel serverLevel)) return;

        if (serverLevel.dimension() == Level.OVERWORLD) {
            DATA = serverLevel.getDataStorage().computeIfAbsent(TardisLevelData::load, TardisLevelData::new, "tardis");
            TardisHelper.registerOldTardises(serverLevel.getServer());
        }
    }

    @SubscribeEvent
    public static void onLevelUnload(WorldEvent.Unload event) {
        if (!(event.getWorld() instanceof ServerLevel serverLevel)) return;

        if (serverLevel.dimension() == Level.OVERWORLD) {
            ModDimensions.TARDISES.clear();
        }
    }

    /////////////////////////////////////
    // END Providing Tardis dimensions //
    /////////////////////////////////////

    @SubscribeEvent
    public static void onLevelTick(TickEvent.WorldTickEvent event) {
        if (event.phase != TickEvent.Phase.END || !(event.world instanceof ServerLevel level)) return;

        level.getCapability(ModCapabilities.TARDIS_CHUNK_LOADER).ifPresent(ITardisChunkLoader::tick);

        if (TardisHelper.isTardisDimension(level)) {
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

    @SubscribeEvent
    public static void onItemUseOnBlock(PlayerInteractEvent.RightClickBlock e) {
        Item mainHandItem = e.getPlayer().getMainHandItem().getItem();
        Item offHandItem = e.getPlayer().getOffhandItem().getItem();

        if (e.isCancelable() && (mainHandItem instanceof ScrewdriverItem || offHandItem instanceof ScrewdriverItem)) {
            e.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemUseOnEntity(PlayerInteractEvent.EntityInteract e) {
        Item mainHandItem = e.getPlayer().getMainHandItem().getItem();
        Item offHandItem = e.getPlayer().getOffhandItem().getItem();

        if (e.isCancelable() && (mainHandItem instanceof ScrewdriverItem || offHandItem instanceof ScrewdriverItem)) {
            if (e.getTarget().getType() == ModEntities.TARDIS_CONSOLE_CONTROL.get()) return;
            if (e.getTarget().getType() == ModEntities.TARDIS_CONSOLE_CONTROL_SMALL.get()) return;
            if (e.getTarget().getType() == ModEntities.TARDIS_CONSOLE_CONTROL_MEDIUM.get()) return;
            if (e.getTarget().getType() == ModEntities.TARDIS_CONSOLE_CONTROL_LARGE.get()) return;

            e.setCanceled(true);
        }
    }
}

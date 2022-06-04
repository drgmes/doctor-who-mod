package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlock;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.caps.ITardisChunkLoader;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.drgmes.dwm.network.ServerboundScrewdriverUsePacket;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.world.data.TardisLevelData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
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
    public static void onItemUseOnBlock(PlayerInteractEvent.RightClickBlock event) {
        Item mainHandItem = event.getPlayer().getMainHandItem().getItem();
        Item offHandItem = event.getPlayer().getOffhandItem().getItem();

        if (event.isCancelable() && (mainHandItem instanceof ScrewdriverItem || offHandItem instanceof ScrewdriverItem)) {
            BlockState blockState = event.getWorld().getBlockState(event.getHitVec().getBlockPos());
            if (blockState.getBlock() instanceof BaseTardisConsoleBlock) return;
            if (blockState.getBlock() instanceof BaseTardisExteriorBlock) return;
            if (blockState.getBlock() instanceof BaseTardisDoorsBlock) return;

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onItemUseOnEntity(PlayerInteractEvent.EntityInteract event) {
        Item mainHandItem = event.getPlayer().getMainHandItem().getItem();
        Item offHandItem = event.getPlayer().getOffhandItem().getItem();

        if (event.isCancelable() && (mainHandItem instanceof ScrewdriverItem || offHandItem instanceof ScrewdriverItem)) {
            if (event.getTarget().getType() == ModEntities.TARDIS_CONSOLE_CONTROL.get()) return;
            if (event.getTarget().getType() == ModEntities.TARDIS_CONSOLE_CONTROL_SMALL.get()) return;
            if (event.getTarget().getType() == ModEntities.TARDIS_CONSOLE_CONTROL_MEDIUM.get()) return;
            if (event.getTarget().getType() == ModEntities.TARDIS_CONSOLE_CONTROL_LARGE.get()) return;

            event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerUseLeftClickWithEmpty(PlayerInteractEvent.LeftClickEmpty event) {
        if (event.getItemStack().getItem() instanceof ScrewdriverItem screwdriverItem) {
            ModPackets.INSTANCE.sendToServer(new ServerboundScrewdriverUsePacket(event.getItemStack(), event.getHand(), true));
            if (event.isCancelable()) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onPlayerUseLeftClickWithBlock(PlayerInteractEvent.LeftClickBlock event) {
        if (event.getItemStack().getItem() instanceof ScrewdriverItem screwdriverItem) {
            screwdriverItem.useScrewdriver(event.getWorld(), event.getPlayer(), event.getHand(), true);
            if (event.isCancelable()) event.setCanceled(true);
        }
    }

    @SubscribeEvent
    public static void onAttackEntity(AttackEntityEvent event) {
        if (event.getPlayer().getMainHandItem().getItem() instanceof ScrewdriverItem screwdriverItem) {
            screwdriverItem.useScrewdriver(event.getPlayer().level, event.getPlayer(), InteractionHand.MAIN_HAND, true);
            if (event.isCancelable()) event.setCanceled(true);
        }
    }
}

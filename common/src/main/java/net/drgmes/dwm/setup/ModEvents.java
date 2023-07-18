package net.drgmes.dwm.setup;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.*;
import net.drgmes.dwm.common.sonicdevice.modes.BaseSonicDeviceMode;
import net.drgmes.dwm.common.tardis.TardisEnergyManager;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.compat.ImmersivePortals;
import net.drgmes.dwm.items.sonicdevices.ISonicDeviceItem;
import net.drgmes.dwm.items.sonicdevices.SonicScrewdriverItem;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.network.server.SonicDeviceUsePacket;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ModEvents {
    public static void setup() {
        // //////////// //
        // Level Events //
        // //////////// //

        LifecycleEvent.SERVER_STARTED.register((world) -> {
            TardisEnergyManager.clear();
            if (ModCompats.immersivePortals()) ImmersivePortals.clearTardisPortalsState();
        });

        LifecycleEvent.SERVER_LEVEL_LOAD.register((world) -> {
            if (TardisHelper.isTardisDimension(world)) {
                TardisStateManager.get(world).ifPresent((tardis) -> {
                    TardisEnergyManager.remove(tardis.getId());
                    if (ModCompats.immersivePortals()) ImmersivePortals.removeTardisPortalsState(tardis.getId());

                    tardis.updateEntrancePortals();
                    tardis.updateRoomEntrancePortals();
                });
            }
        });

        TickEvent.SERVER_LEVEL_POST.register((world) -> {
            if (TardisHelper.isTardisDimension(world)) {
                TardisStateManager.get(world).ifPresent(TardisStateManager::tick);
            }
        });

        // ///////////// //
        // Entity Events //
        // ///////////// //

        EntityEvent.ADD.register((entity, world) -> {
            if (entity instanceof ItemEntity itemEntity) {
                Item item = itemEntity.getStack().getItem();

                if (item instanceof ISonicDeviceItem) itemEntity.setNeverDespawn();
                else if (item instanceof TardisKeyItem) itemEntity.setNeverDespawn();
            }

            return EventResult.pass();
        });

        // //////////////////////// //
        // Sonic Screwdriver Events //
        // //////////////////////// //

        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, blockPos, direction) -> (
            applySonicScrewdriver(player, hand, false)
        ));

        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, blockPos, direction) -> (
            applySonicScrewdriver(player, hand, true)
        ));

        PlayerEvent.ATTACK_ENTITY.register((player, world, entity, hand, entityHitResult) -> (
            applySonicScrewdriver(player, hand, true)
        ));

        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> {
            if (!BaseSonicDeviceMode.checkIsValidHitEntity(entity)) return EventResult.pass();
            return applySonicScrewdriver(player, hand, false);
        });
    }

    public static EventResult applySonicScrewdriver(PlayerEntity player, Hand hand, boolean isAlternativeAction) {
        EquipmentSlot slot = hand == Hand.MAIN_HAND ? EquipmentSlot.MAINHAND : EquipmentSlot.OFFHAND;
        ItemStack sonicScrewdriverItemStack = player.getEquippedStack(slot);

        if (sonicScrewdriverItemStack.getItem() instanceof SonicScrewdriverItem screwdriverItem) {
            if (!player.getWorld().isClient) return EventResult.interruptFalse();
            ActionResult result = screwdriverItem.useSonicDevice(player.getWorld(), player, slot, isAlternativeAction).getResult();

            if (result.shouldSwingHand()) {
                new SonicDeviceUsePacket(sonicScrewdriverItemStack, slot.getName(), isAlternativeAction).sendToServer();
            }

            return EventResult.interruptTrue();
        }

        return EventResult.pass();
    }
}

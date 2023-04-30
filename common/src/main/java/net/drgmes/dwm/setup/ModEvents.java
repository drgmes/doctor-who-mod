package net.drgmes.dwm.setup;

import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.InteractionEvent;
import dev.architectury.event.events.common.LifecycleEvent;
import dev.architectury.event.events.common.PlayerEvent;
import dev.architectury.event.events.common.TickEvent;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.drgmes.dwm.network.server.ScrewdriverUsePacket;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;

public class ModEvents {
    public static void setup() {
        // //////////// //
        // Level Events //
        // //////////// //

        LifecycleEvent.SERVER_LEVEL_LOAD.register((world) -> {
            if (TardisHelper.isTardisDimension(world)) {
                TardisStateManager.get(world).ifPresent((tardis) -> {
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

        // ////////////////// //
        // Screwdriver Events //
        // ////////////////// //

        InteractionEvent.RIGHT_CLICK_BLOCK.register((player, hand, blockPos, direction) -> (
            applyScrewdriver(player, hand, false)
        ));

        InteractionEvent.LEFT_CLICK_BLOCK.register((player, hand, blockPos, direction) -> (
            applyScrewdriver(player, hand, true)
        ));

        InteractionEvent.INTERACT_ENTITY.register((player, entity, hand) -> (
            applyScrewdriver(player, hand, false)
        ));

        PlayerEvent.ATTACK_ENTITY.register((player, world, entity, hand, entityHitResult) -> (
            applyScrewdriver(player, hand, true)
        ));
    }

    public static EventResult applyScrewdriver(PlayerEntity player, Hand hand, boolean isAlternativeAction) {
        ItemStack screwdriverItemStack = player.getStackInHand(hand);

        if (Screwdriver.checkItemStackIsScrewdriver(screwdriverItemStack)) {
            if (!player.world.isClient) return EventResult.interruptFalse();
            ActionResult result = ((ScrewdriverItem) screwdriverItemStack.getItem()).useScrewdriver(player.world, player, hand, isAlternativeAction).getResult();

            if (result.shouldSwingHand()) {
                new ScrewdriverUsePacket(screwdriverItemStack, hand == Hand.MAIN_HAND, isAlternativeAction).sendToServer();
                return EventResult.interruptTrue();
            }

            return EventResult.interruptFalse();
        }

        return EventResult.pass();
    }
}

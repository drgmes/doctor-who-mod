package net.drgmes.dwm.setup;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.drgmes.dwm.network.ScrewdriverRemoteCallablePackets;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.fabricmc.fabric.api.event.player.AttackBlockCallback;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

public class ModEvents {
    public static void setup() {
        // //////////// //
        // World Events //
        // //////////// //

        ServerWorldEvents.LOAD.register((server, world) -> {
            if (TardisHelper.isTardisDimension(world)) {
                TardisStateManager.get(world).ifPresent((tardis) -> {
                    tardis.updateEntrancePortals();
                    tardis.updateRoomsEntrancesPortals();
                });
            }
        });

        ServerTickEvents.START_WORLD_TICK.register((world) -> {
            if (TardisHelper.isTardisDimension(world)) {
                TardisStateManager.get(world).ifPresent(TardisStateManager::tick);
            }
        });

        // ////////////////// //
        // Screwdriver Events //
        // ////////////////// //

        UseBlockCallback.EVENT.register((player, world, hand, blockHitResult) -> (
            applyScrewdriver(player, world, hand, false)
        ));

        AttackBlockCallback.EVENT.register((player, world, hand, blockPos, direction) -> (
            applyScrewdriver(player, world, hand, true)
        ));

        UseEntityCallback.EVENT.register((player, world, hand, entity, entityHitResult) -> (
            applyScrewdriver(player, world, hand, false)
        ));

        AttackEntityCallback.EVENT.register((player, world, hand, entity, entityHitResult) -> (
            applyScrewdriver(player, world, hand, true)
        ));
    }

    private static ActionResult applyScrewdriver(PlayerEntity player, World world, Hand hand, boolean isAlternativeAction) {
        ItemStack screwdriverItemStack = player.getStackInHand(hand);

        if (Screwdriver.checkItemStackIsScrewdriver(screwdriverItemStack)) {
            if (!world.isClient) return ActionResult.FAIL;
            ActionResult result = ((ScrewdriverItem) screwdriverItemStack.getItem()).useScrewdriver(world, player, hand, isAlternativeAction).getResult();

            if (result.shouldSwingHand()) {
                PacketHelper.sendToServer(
                    ScrewdriverRemoteCallablePackets.class,
                    "useScrewdriver",
                    screwdriverItemStack, hand == Hand.MAIN_HAND, isAlternativeAction
                );
            }

            return result;
        }

        return ActionResult.PASS;
    }
}

package net.drgmes.dwm.network;

import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class ScrewdriverRemoteCallablePackets {
    // /////////// //
    // Serverbound //
    // /////////// //

    public static void updateScrewdriverData(ServerPlayerEntity player, ItemStack newScrewdriverItemStack, boolean isMainHand) {
        ItemStack screwdriverItemStack = player.getStackInHand(isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND);

        if (Screwdriver.checkItemStackIsScrewdriver(screwdriverItemStack)) {
            player.setStackInHand(isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND, newScrewdriverItemStack);
        }
    }

    public static void useScrewdriver(ServerPlayerEntity player, ItemStack screwdriverItemStack, boolean isMainHand, boolean isAlternativeAction) {
        if (Screwdriver.checkItemStackIsScrewdriver(screwdriverItemStack)) {
            ((ScrewdriverItem) screwdriverItemStack.getItem()).useScrewdriver(player.world, player, isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND, isAlternativeAction);
        }
    }
}

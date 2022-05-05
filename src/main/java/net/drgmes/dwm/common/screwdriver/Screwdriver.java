package net.drgmes.dwm.common.screwdriver;

import net.drgmes.dwm.common.screwdriver.modes.IScrewdriverMode;
import net.drgmes.dwm.common.screwdriver.modes.ScrewdriverSettingMode;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;

public class Screwdriver {
    public static boolean interact(Level level, Player player, InteractionHand hand, HitResult hitResult) {
        IScrewdriverMode mode = Screwdriver.getInteractionMode(player.getItemInHand(hand));

        if (hitResult == null || hitResult.getType() == HitResult.Type.MISS) {
            return false;
        }

        if (hitResult.getType() == HitResult.Type.BLOCK) {
            return mode.interactWithBlock(level, player, hand, (BlockHitResult) hitResult);
        }

        if (hitResult.getType() == HitResult.Type.ENTITY) {
            return mode.interactWithEntity(level, player, hand, (EntityHitResult) hitResult);
        }

        return false;
    }

    public static IScrewdriverMode getInteractionMode(ItemStack screwdriver) {
        return ScrewdriverSettingMode.INSTANCE;
    }
}

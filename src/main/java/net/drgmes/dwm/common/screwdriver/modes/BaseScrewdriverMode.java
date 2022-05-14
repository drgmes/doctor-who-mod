package net.drgmes.dwm.common.screwdriver.modes;

import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public abstract class BaseScrewdriverMode {
    public boolean interactWithBlock(Level level, Player player, InteractionHand hand, BlockHitResult hitResult, boolean isAlternativeAction) {
        return false;
    }

    public boolean interactWithBlockNative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return false;
    }

    public boolean interactWithBlockAlternative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        return false;
    }

    public boolean interactWithEntity(Level level, Player player, InteractionHand hand, EntityHitResult hitResult, boolean isAlternativeAction) {
        return false;
    }

    public boolean interactWithEntityNative(Level level, Player player, InteractionHand hand, EntityHitResult hitResult) {
        return false;
    }

    public boolean interactWithEntityAlternative(Level level, Player player, InteractionHand hand, EntityHitResult hitResult) {
        return false;
    }

    protected boolean checkIsValidHitEntity(Entity entity) {
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL.get()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_SMALL.get()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_MEDIUM.get()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_LARGE.get()) return false;
        return true;
    }
}

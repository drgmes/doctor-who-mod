package net.drgmes.dwm.common.screwdriver.modes;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlock;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public abstract class BaseScrewdriverMode {
    public boolean interactWithBlock(Level level, Player player, InteractionHand hand, BlockHitResult hitResult, boolean isAlternativeAction) {
        if (!this.checkIsValidHitBlock(level.getBlockState(hitResult.getBlockPos()))) return false;
        return false;
    }

    public boolean interactWithBlockNative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!this.checkIsValidHitBlock(level.getBlockState(hitResult.getBlockPos()))) return false;
        return true;
    }

    public boolean interactWithBlockAlternative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (!this.checkIsValidHitBlock(level.getBlockState(hitResult.getBlockPos()))) return false;
        return true;
    }

    public boolean interactWithEntity(Level level, Player player, InteractionHand hand, EntityHitResult hitResult, boolean isAlternativeAction) {
        if (!this.checkIsValidHitEntity(hitResult.getEntity())) return false;
        return false;
    }

    public boolean interactWithEntityNative(Level level, Player player, InteractionHand hand, EntityHitResult hitResult) {
        if (!this.checkIsValidHitEntity(hitResult.getEntity())) return false;
        return true;
    }

    public boolean interactWithEntityAlternative(Level level, Player player, InteractionHand hand, EntityHitResult hitResult) {
        if (!this.checkIsValidHitEntity(hitResult.getEntity())) return false;
        return true;
    }

    public void generateVibration(Level level, Player player, BlockPos blockPos) {
        level.gameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Context.of(null, level.getBlockState(blockPos)));
    }

    protected boolean checkIsValidHitBlock(BlockState blockState) {
        if (blockState.getBlock() instanceof BaseTardisConsoleBlock) return false;
        if (blockState.getBlock() instanceof BaseTardisExteriorBlock) return false;
        if (blockState.getBlock() instanceof BaseTardisDoorsBlock) return false;
        return true;
    }

    protected boolean checkIsValidHitEntity(Entity entity) {
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL.get()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_SMALL.get()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_MEDIUM.get()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_LARGE.get()) return false;
        return true;
    }
}

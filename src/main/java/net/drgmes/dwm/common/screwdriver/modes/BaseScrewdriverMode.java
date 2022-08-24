package net.drgmes.dwm.common.screwdriver.modes;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlock;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import qouteall.imm_ptl.core.portal.Portal;

public class BaseScrewdriverMode {
    public ActionResult interactWithBlock(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult, boolean isAlternativeAction) {
        return ActionResult.CONSUME;
    }

    public ActionResult interactWithBlockNative(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        return this.interactWithBlock(world, player, hand, hitResult, false);
    }

    public ActionResult interactWithBlockAlternative(World world, PlayerEntity player, Hand hand, BlockHitResult hitResult) {
        return this.interactWithBlock(world, player, hand, hitResult, true);
    }

    public ActionResult interactWithEntity(World world, PlayerEntity player, Hand hand, EntityHitResult hitResult, boolean isAlternativeAction) {
        return ActionResult.CONSUME;
    }

    public ActionResult interactWithEntityNative(World world, PlayerEntity player, Hand hand, EntityHitResult hitResult) {
        return this.interactWithEntity(world, player, hand, hitResult, false);
    }

    public ActionResult interactWithEntityAlternative(World world, PlayerEntity player, Hand hand, EntityHitResult hitResult) {
        return this.interactWithEntity(world, player, hand, hitResult, true);
    }

    public void generateVibration(World world, PlayerEntity player, BlockPos blockPos) {
        world.emitGameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Emitter.of(null, world.getBlockState(blockPos)));
    }

    protected boolean checkIsValidHitBlock(BlockState blockState) {
        if (blockState.getBlock() instanceof BaseTardisConsoleBlock) return false;
        if (blockState.getBlock() instanceof BaseTardisExteriorBlock) return false;
        if (blockState.getBlock() instanceof BaseTardisDoorsBlock) return false;
        return true;
    }

    protected boolean checkIsValidHitEntity(Entity entity) {
        if (entity.getType() == Portal.entityType) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL.getEntityType()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_SMALL.getEntityType()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_MEDIUM.getEntityType()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_CONTROL_LARGE.getEntityType()) return false;
        return true;
    }
}

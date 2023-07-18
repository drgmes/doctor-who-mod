package net.drgmes.dwm.common.sonicdevice.modes;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlock;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.types.IMixinPortal;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

public class BaseSonicDeviceMode {
    public static boolean checkIsValidHitBlock(BlockState blockState) {
        if (blockState.getBlock() instanceof BaseTardisConsoleUnitBlock) return false;
        if (blockState.getBlock() instanceof BaseTardisExteriorBlock) return false;
        if (blockState.getBlock() instanceof BaseTardisDoorsBlock) return false;
        return true;
    }

    public static boolean checkIsValidHitEntity(Entity entity) {
        if (entity instanceof IMixinPortal) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_UNIT_CONTROL.getEntityType()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL.getEntityType()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_MEDIUM.getEntityType()) return false;
        if (entity.getType() == ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_LARGE.getEntityType()) return false;
        return true;
    }

    public ActionResult interactWithBlock(World world, PlayerEntity player, EquipmentSlot slot, BlockHitResult hitResult, boolean isAlternativeAction) {
        return ActionResult.CONSUME;
    }

    public ActionResult interactWithBlockNative(World world, PlayerEntity player, EquipmentSlot slot, BlockHitResult hitResult) {
        return this.interactWithBlock(world, player, slot, hitResult, false);
    }

    public ActionResult interactWithBlockAlternative(World world, PlayerEntity player, EquipmentSlot slot, BlockHitResult hitResult) {
        return this.interactWithBlock(world, player, slot, hitResult, true);
    }

    public ActionResult interactWithEntity(World world, PlayerEntity player, EquipmentSlot slot, EntityHitResult hitResult, boolean isAlternativeAction) {
        return ActionResult.CONSUME;
    }

    public ActionResult interactWithEntityNative(World world, PlayerEntity player, EquipmentSlot slot, EntityHitResult hitResult) {
        return this.interactWithEntity(world, player, slot, hitResult, false);
    }

    public ActionResult interactWithEntityAlternative(World world, PlayerEntity player, EquipmentSlot slot, EntityHitResult hitResult) {
        return this.interactWithEntity(world, player, slot, hitResult, true);
    }

    public void generateVibration(World world, PlayerEntity player, BlockPos blockPos) {
        world.emitGameEvent(GameEvent.PROJECTILE_LAND, blockPos, GameEvent.Emitter.of(null, world.getBlockState(blockPos)));
    }
}

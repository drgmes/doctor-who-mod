package net.drgmes.dwm.common.sonicdevice.modes.setting;

import net.drgmes.dwm.common.sonicdevice.modes.BaseSonicDeviceMode;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.JukeboxBlockEntity;
import net.minecraft.block.entity.SculkShriekerBlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.FoxEntity;
import net.minecraft.entity.passive.MooshroomEntity;
import net.minecraft.entity.passive.SheepEntity;
import net.minecraft.entity.passive.TraderLlamaEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FireballEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

import java.lang.reflect.Method;

public class SonicDeviceSettingMode extends BaseSonicDeviceMode {
    public static final SonicDeviceSettingMode INSTANCE = new SonicDeviceSettingMode();

    @Override
    public ActionResult interactWithBlockNative(World world, PlayerEntity player, EquipmentSlot slot, BlockHitResult hitResult) {
        if (!checkIsValidHitBlock(world.getBlockState(hitResult.getBlockPos()))) return ActionResult.PASS;

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        BlockEntity blockEntity = world.getBlockEntity(blockPos);
        Block block = blockState.getBlock();

//        if (world.getBlockEntity(blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
//            if (!world.isClient) {
//                tardisRoundelBlockEntity.uncovered = !tardisRoundelBlockEntity.uncovered;
//                tardisRoundelBlockEntity.markDirty();
//                tardisRoundelBlockEntity.sendUpdatePacket();
//            }
//
//            return ActionResult.SUCCESS;
//        }

        // SculkShriekerBlock
        if (blockEntity instanceof SculkShriekerBlockEntity sculkShriekerBlockEntity) {
            if (!world.isClient) sculkShriekerBlockEntity.shriek((ServerWorld) world, (ServerPlayerEntity) player);
            return ActionResult.SUCCESS;
        }

        // SculkSensorBlock
        if (block instanceof SculkSensorBlock) {
            world.emitGameEvent(player, GameEvent.BLOCK_ACTIVATE, blockPos);
            return ActionResult.SUCCESS;
        }

        // Bell
        if (block instanceof BellBlock bellBlock) {
            if (bellBlock.ring(world, hitResult.getBlockPos(), hitResult.getSide())) {
                player.incrementStat(Stats.BELL_RING);
                return ActionResult.SUCCESS;
            }
        }

        // TNT
        if (block instanceof TntBlock) {
            TntBlock.primeTnt(world, blockPos);
            world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), 11);
            return ActionResult.SUCCESS;
        }

        // Dispenser
        if (block instanceof DispenserBlock dispenserBlock) {
            if (!world.isClient) dispenserBlock.scheduledTick(blockState, (ServerWorld) world, blockPos, world.random);
            world.setBlockState(blockPos, blockState.with(DispenserBlock.TRIGGERED, true), Block.NOTIFY_ALL);
            world.setBlockState(blockPos, blockState.with(DispenserBlock.TRIGGERED, false), Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        // NoteBlock
        if (block instanceof NoteBlock noteBlock) {
            noteBlock.onSyncedBlockEvent(blockState, world, blockPos, 0, 0);
            world.setBlockState(blockPos, blockState.with(NoteBlock.POWERED, true), Block.NOTIFY_ALL);
            world.setBlockState(blockPos, blockState.with(NoteBlock.POWERED, false), Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        // Jukebox
        if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity) {
            ItemStack disk = jukeboxBlockEntity.getStack();

            if (disk != null) {
                world.syncWorldEvent(null, WorldEvents.JUKEBOX_STARTS_PLAYING, blockPos, Item.getRawId(disk.getItem())); // 1010 event
                return ActionResult.SUCCESS;
            }
        }

        // Wooden Blocks
        if (blockState.getSoundGroup() == BlockSoundGroup.WOOD) {
            return ActionResult.FAIL;
        }

        return interactWithBlockProperty(world, blockPos);
    }

    @Override
    public ActionResult interactWithBlockAlternative(World world, PlayerEntity player, EquipmentSlot slot, BlockHitResult hitResult) {
        if (!checkIsValidHitBlock(world.getBlockState(hitResult.getBlockPos()))) return ActionResult.PASS;

        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = world.getBlockState(blockPos);
        Block block = blockState.getBlock();

        // Torch
        if (block instanceof TorchBlock) {
            if (player.isSneaking()) {
                if (!world.isClient) world.breakBlock(blockPos, true);
                return ActionResult.SUCCESS;
            }
        }

        // Glass Pane
        if (block instanceof PaneBlock && blockState.getSoundGroup() == BlockSoundGroup.GLASS) {
            if (player.isSneaking()) {
                if (!world.isClient) world.breakBlock(blockPos, true);
                return ActionResult.SUCCESS;
            }
        }

        // MobSpawnerBlockEntity
        if (block instanceof SpawnerBlock) {
            if (player.isSneaking()) {
                world.breakBlock(blockPos, false);
                world.createExplosion(null, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 2.5F, World.ExplosionSourceType.NONE);
                return ActionResult.SUCCESS;
            }
        }

        // Jukebox
        if (block instanceof JukeboxBlock jukeboxBlock) {
            jukeboxBlock.onUse(blockState, world, blockPos, player, Hand.MAIN_HAND, hitResult);
            return ActionResult.SUCCESS;
        }

        // NoteBlock
        if (block instanceof NoteBlock noteBlock) {
            blockState = player.isSneaking() ? blockState.cycle(NoteBlock.INSTRUMENT) : blockState.cycle(NoteBlock.NOTE);
            world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);

            noteBlock.onSyncedBlockEvent(blockState, world, blockPos, 0, 0);
            world.setBlockState(blockPos, blockState.with(NoteBlock.POWERED, true), Block.NOTIFY_ALL);
            world.setBlockState(blockPos, blockState.with(NoteBlock.POWERED, false), Block.NOTIFY_ALL);
            return ActionResult.SUCCESS;
        }

        return ActionResult.CONSUME;
    }

    @Override
    public ActionResult interactWithEntityNative(World world, PlayerEntity player, EquipmentSlot slot, EntityHitResult hitResult) {
        if (!checkIsValidHitEntity(hitResult.getEntity())) return ActionResult.PASS;

        Entity entity = hitResult.getEntity();

        // Trader Llama
        if (entity instanceof TraderLlamaEntity traderLlama) {
            if (traderLlama.isLeashed()) {
                traderLlama.detachLeash(true, true);
                return ActionResult.SUCCESS;
            }
        }

        // Creeper
        if (entity instanceof CreeperEntity creeper) {
            creeper.ignite();
            return ActionResult.SUCCESS;
        }

        // Slime
        if (entity instanceof SlimeEntity slime) {
            if (slime.getSize() > 1) {
                slime.kill();
                return ActionResult.SUCCESS;
            }
        }

        // Sheep
        if (entity instanceof SheepEntity sheep) {
            if (sheep.isShearable()) {
                sheep.sheared(SoundCategory.PLAYERS);
                sheep.emitGameEvent(GameEvent.SHEAR, player);
            }
        }

        // MooshroomEntity
        if (entity instanceof MooshroomEntity mushroomCow) {
            if (mushroomCow.isShearable()) {
                mushroomCow.sheared(SoundCategory.PLAYERS);
                mushroomCow.emitGameEvent(GameEvent.SHEAR, player);
            }
        }

        // Fox
        if (entity instanceof FoxEntity fox) {
            try {
                Method method = entity.getClass().getDeclaredMethod("spit", ItemStack.class);
                method.setAccessible(true);
                method.invoke(entity, fox.getEquippedStack(EquipmentSlot.MAINHAND));
                entity.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                return ActionResult.SUCCESS;
            } catch (Exception ignored) {
            }
        }

        // Fireball
        if (entity instanceof FireballEntity fireball) {
            Vec3d vec3 = player.getRotationVector();
            fireball.setVelocity(vec3);
            fireball.powerX = vec3.x * 0.1D;
            fireball.powerY = vec3.y * 0.1D;
            fireball.powerZ = vec3.z * 0.1D;
            fireball.setOwner(player);
            return ActionResult.SUCCESS;
        }

        return ActionResult.CONSUME;
    }

    private ActionResult interactWithBlockProperty(World world, BlockPos blockPos) {
        BlockState blockState = world.getBlockState(blockPos);
        Property<?>[] props = new Property<?>[]{
            Properties.POWER,
            Properties.OPEN,
            Properties.POWERED,
            Properties.ENABLED,
            Properties.LIT
        };

        for (Property<?> prop : props) {
            if (blockState.contains(prop)) {
                if (!world.isClient) world.setBlockState(blockPos, blockState.cycle(prop), Block.NOTIFY_ALL);
                return ActionResult.SUCCESS;
            }
        }

        return ActionResult.CONSUME;
    }
}

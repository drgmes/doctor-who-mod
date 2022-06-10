package net.drgmes.dwm.common.screwdriver.modes.setting;

import java.lang.reflect.Method;
import java.util.List;

import net.drgmes.dwm.common.screwdriver.modes.BaseScrewdriverMode;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.animal.Fox;
import net.minecraft.world.entity.animal.MushroomCow;
import net.minecraft.world.entity.animal.Sheep;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DispenserBlock;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.JukeboxBlock;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.SculkSensorBlock;
import net.minecraft.world.level.block.TntBlock;
import net.minecraft.world.level.block.TorchBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.JukeboxBlockEntity;
import net.minecraft.world.level.block.entity.SculkShriekerBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class ScrewdriverSettingMode extends BaseScrewdriverMode {
    public static ScrewdriverSettingMode INSTANCE = new ScrewdriverSettingMode();

    @Override
    public boolean interactWithBlockNative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        if (!this.checkIsValidHitBlock(blockState)) return false;

        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        Block block = blockState.getBlock();

        // SculkShriekerBlock
        if (blockEntity instanceof SculkShriekerBlockEntity sculkShriekerBlockEntity) {
            if (!level.isClientSide) sculkShriekerBlockEntity.tryShriek((ServerLevel) level, (ServerPlayer) player);
            return true;
        }

        // SculkSensorBlock
        if (block instanceof SculkSensorBlock sculkSensorBlock) {
            level.gameEvent(player, GameEvent.BLOCK_ACTIVATE, blockPos);
            return true;
        }

        // Bell
        if (block instanceof BellBlock bellBlock) {
            if (bellBlock.attemptToRing(level, hitResult.getBlockPos(), hitResult.getDirection())) {
                player.awardStat(Stats.BELL_RING);
                return true;
            }
        }

        // TNT
        if (block instanceof TntBlock tntBlock) {
            tntBlock.onCaughtFire(blockState, level, blockPos, null, player);
            level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 11);
            return true;
        }

        // Dispenser
        if (block instanceof DispenserBlock dispenserBlock) {
            if (!level.isClientSide) dispenserBlock.tick(blockState, (ServerLevel) level, blockPos, level.random);
            level.setBlock(blockPos, blockState.setValue(DispenserBlock.TRIGGERED, true), 3);
            level.setBlock(blockPos, blockState.setValue(DispenserBlock.TRIGGERED, false), 3);
            return true;
        }

        // Jukebox
        if (block instanceof JukeboxBlock jukeboxBlock) {
            if (blockEntity instanceof JukeboxBlockEntity jukeboxBlockEntity) {
                ItemStack disk = jukeboxBlockEntity.getRecord();

                if (disk != null) {
                    level.levelEvent((Player) null, 1010, blockPos, Item.getId(disk.getItem()));
                    return true;
                }
            }
        }

        // NoteBlock
        if (block instanceof NoteBlock noteBlock) {
            noteBlock.triggerEvent(blockState, level, blockPos, 0, 0);
            level.setBlock(blockPos, blockState.setValue(NoteBlock.POWERED, true), 3);
            level.setBlock(blockPos, blockState.setValue(NoteBlock.POWERED, false), 3);
            return true;
        }

        // Wooden Blocks
        if (blockState.getMaterial() == Material.WOOD) {
            return false;
        }

        return interactWithBlockProperty(level, player, blockPos);
    }

    @Override
    public boolean interactWithBlockAlternative(Level level, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockPos blockPos = hitResult.getBlockPos();
        BlockState blockState = level.getBlockState(blockPos);
        Block block = blockState.getBlock();

        // Torch
        if (block instanceof TorchBlock) {
            if (player.isShiftKeyDown()) {
                if (!level.isClientSide) level.destroyBlock(blockPos, true);
                return true;
            }
        }

        // Glass Pane
        if (block instanceof IronBarsBlock && blockState.getMaterial() == Material.GLASS) {
            if (player.isShiftKeyDown()) {
                if (!level.isClientSide) level.destroyBlock(blockPos, true);
                return true;
            }
        }

        // Jukebox
        if (block instanceof JukeboxBlock jukeboxBlock) {
            jukeboxBlock.use(blockState, level, blockPos, player, hand, hitResult);
            return true;
        }

        // NoteBlock
        if (block instanceof NoteBlock noteBlock) {
            blockState = player.isShiftKeyDown() ? blockState.cycle(NoteBlock.INSTRUMENT) : blockState.cycle(NoteBlock.NOTE);
            level.setBlock(blockPos, blockState, 3);

            noteBlock.triggerEvent(blockState, level, blockPos, 0, 0);
            level.setBlock(blockPos, blockState.setValue(NoteBlock.POWERED, true), 3);
            level.setBlock(blockPos, blockState.setValue(NoteBlock.POWERED, false), 3);
            return true;
        }

        return false;
    }

    @Override
    public boolean interactWithEntityNative(Level level, Player player, InteractionHand hand, EntityHitResult hitResult) {
        Entity entity = hitResult.getEntity();
        if (!this.checkIsValidHitEntity(entity)) return false;

        // Trader Llama
        if (entity instanceof TraderLlama traderLlama) {
            if (traderLlama.isLeashed()) {
                traderLlama.dropLeash(true, true);
                return true;
            }
        }

        // Creeper
        if (entity instanceof Creeper creeper) {
            creeper.ignite();
            return true;
        }

        // Slime
        if (entity instanceof Slime slime) {
            if (slime.getSize() > 1) {
                slime.kill();
                return true;
            }
        }

        // Sheep
        if (entity instanceof Sheep sheep) {
            if (sheep.readyForShearing()) {
                List<ItemStack> drops = sheep.onSheared(null, null, level, entity.blockPosition(), 0);

                for (ItemStack is : drops) {
                    entity.spawnAtLocation(is, 1.0F);
                    return true;
                }
            }
        }

        // MooshroomEntity
        if (entity instanceof MushroomCow mushroomCow) {
            if (mushroomCow.readyForShearing()) {
                List<ItemStack> drops = mushroomCow.onSheared(null, null, level, entity.blockPosition(), 0);

                for (ItemStack is : drops) {
                    entity.spawnAtLocation(is, 1.0F);
                    return true;
                }
            }
        }

        // Fox
        if (entity instanceof Fox fox) {
            try {
                Method method = entity.getClass().getDeclaredMethod("spitOutItem", ItemStack.class);
                method.setAccessible(true);
                method.invoke(entity, fox.getItemBySlot(EquipmentSlot.MAINHAND));
                entity.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                return true;
            }
            catch (Exception e) {
            }
        }

        // Fireball
        if (entity instanceof Fireball fireball) {
            Vec3 vec3 = player.getLookAngle();
            fireball.setDeltaMovement(vec3);
            fireball.xPower = vec3.x * 0.1D;
            fireball.yPower = vec3.y * 0.1D;
            fireball.zPower = vec3.z * 0.1D;
            fireball.setOwner(player);
            return true;
        }

        return false;
    }

    private boolean interactWithBlockProperty(Level level, Player player, BlockPos blockPos) {
        BlockState blockState = level.getBlockState(blockPos);
        Property<?>[] props = new Property<?>[] {
            BlockStateProperties.POWER,
            BlockStateProperties.OPEN,
            BlockStateProperties.POWERED,
            BlockStateProperties.ENABLED,
            BlockStateProperties.LIT
        };

        for (Property<?> prop : props) {
            if (blockState.hasProperty(prop)) {
                if (!level.isClientSide) level.setBlock(blockPos, blockState.cycle(prop), 3);
                return true;
            }
        }

        return false;
    }
}

package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.common.tardis.consoleunits.TardisConsoleUnitEntry;
import net.drgmes.dwm.network.server.TardisConsoleUnitSoundPacket;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlockWithEntity;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public abstract class BaseTardisConsoleUnitBlock<C extends BaseTardisConsoleUnitBlockEntity> extends BaseRotatableWaterloggedBlockWithEntity {
    protected final TardisConsoleUnitEntry consoleUnitType;

    public BaseTardisConsoleUnitBlock(AbstractBlock.Settings settings, TardisConsoleUnitEntry consoleUnitType) {
        super(settings);
        this.consoleUnitType = consoleUnitType;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return this.consoleUnitType.getBlockEntityType().instantiate(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType != this.consoleUnitType.getBlockEntityType() ? null : (l, bp, bs, blockEntity) -> {
            ((BaseTardisConsoleUnitBlockEntity) blockEntity).tick();
        };
    }

    @Override
    public void randomDisplayTick(BlockState blockState, World world, BlockPos blockPos, Random random) {
        if (!TardisHelper.isTardisDimension(world)) return;

        if (world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            if (tardisConsoleUnitBlockEntity.tardisStateManager.isBroken() && random.nextDouble() < 0.1) {
                new TardisConsoleUnitSoundPacket(blockPos).sendToServer();

                for (int i = 0; i < 3; i++) {
                    Vec3d pos = Vec3d.of(blockPos).add(random.nextBetween(-1, 1) * 0.5 + 0.5, random.nextDouble() * 0.5 + 1, random.nextBetween(-1, 1) * 0.5 + 0.5);
                    world.addParticle(ParticleTypes.FLAME, pos.x, pos.y, pos.z, 0, 0.025, 0);
                    world.addParticle(ParticleTypes.CLOUD, pos.x, pos.y, pos.z, 0, 0, 0);
                }
            }
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState newBlockState, boolean moved) {
        if (!blockState.isOf(newBlockState.getBlock())) {
            if (world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                ItemScatterer.spawn(world, blockPos, DefaultedList.ofSize(1, tardisConsoleUnitBlockEntity.sonicScrewdriverItemStack));
                tardisConsoleUnitBlockEntity.remove();
            }
        }

        super.onStateReplaced(blockState, world, blockPos, newBlockState, moved);
    }

    @Override
    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        if (world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            tardisConsoleUnitBlockEntity.init();
        }

        super.onPlaced(world, blockPos, blockState, entity, itemStack);
    }
}

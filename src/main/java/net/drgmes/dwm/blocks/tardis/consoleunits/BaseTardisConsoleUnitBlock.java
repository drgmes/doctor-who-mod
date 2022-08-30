package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlockWithEntity;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public abstract class BaseTardisConsoleUnitBlock<C extends BaseTardisConsoleUnitBlockEntity> extends BaseRotatableWaterloggedBlockWithEntity {
    private final Supplier<BlockEntityType<C>> blockEntityType;

    public BaseTardisConsoleUnitBlock(AbstractBlock.Settings settings, Supplier<BlockEntityType<C>> blockEntityType) {
        super(settings);
        this.blockEntityType = blockEntityType;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType != this.blockEntityType.get() ? null : (l, bp, bs, blockEntity) -> {
            ((BaseTardisConsoleUnitBlockEntity) blockEntity).tick();
        };
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState newBlockState, boolean moved) {
        if (!blockState.isOf(newBlockState.getBlock())) {
            if (world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
                ItemScatterer.spawn(world, blockPos, DefaultedList.ofSize(1, tardisConsoleUnitBlockEntity.screwdriverItemStack));
            }
        }

        super.onStateReplaced(blockState, world, blockPos, newBlockState, moved);
    }
}

package net.drgmes.dwm.utils.base.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.EnumProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;

public abstract class BaseRotatableWaterloggedDoubleBlockWithEntity extends BaseRotatableWaterloggedBlockWithEntity {
    public static final EnumProperty<DoubleBlockHalf> HALF = Properties.DOUBLE_BLOCK_HALF;

    public BaseRotatableWaterloggedDoubleBlockWithEntity(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        World world = context.getWorld();
        BlockPos blockPos = context.getBlockPos();

        if (world.isInBuildLimit(blockPos) && world.isInBuildLimit(blockPos.up()) && world.getBlockState(blockPos.up()).canReplace(context)) {
            return super.getPlacementState(context).with(HALF, DoubleBlockHalf.LOWER);
        }

        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(HALF);
    }

    @Override
    protected BlockState getDefaultBlockState() {
        return super.getDefaultBlockState().with(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        world.setBlockState(blockPos.up(), blockState.with(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState getStateForNeighborUpdate(BlockState blockState, Direction direction, BlockState neighborBlockState, WorldAccess world, BlockPos blockPos, BlockPos neighborBlockPos) {
        DoubleBlockHalf doubleBlockHalf = blockState.get(HALF);

        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            if (neighborBlockState.isOf(this) && neighborBlockState.get(HALF) != doubleBlockHalf) return this.syncNeighborState(blockState, neighborBlockState);
            return Blocks.AIR.getDefaultState();
        }

        if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canPlaceAt(world, blockPos)) {
            return Blocks.AIR.getDefaultState();
        }

        return super.getStateForNeighborUpdate(blockState, direction, neighborBlockState, world, blockPos, neighborBlockPos);
    }

    protected BlockState syncNeighborState(BlockState blockState, BlockState neighborBlockState) {
        return blockState.with(FACING, neighborBlockState.get(FACING));
    }
}

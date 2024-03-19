package net.drgmes.dwm.utils.base.blocks;

import com.mojang.serialization.MapCodec;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.DirectionProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;

public class BaseRotatableBlock extends HorizontalFacingBlock {
    public static final DirectionProperty FACING = Properties.HORIZONTAL_FACING;

    public BaseRotatableBlock(AbstractBlock.Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultBlockState());
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        return this.getDefaultBlockState().with(FACING, context.getHorizontalPlayerFacing().getOpposite());
    }

    @Override
    protected MapCodec<? extends HorizontalFacingBlock> getCodec() {
        return null;
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(FACING);
    }

    protected BlockState getDefaultBlockState() {
        return this.getDefaultState().with(FACING, Direction.NORTH);
    }
}

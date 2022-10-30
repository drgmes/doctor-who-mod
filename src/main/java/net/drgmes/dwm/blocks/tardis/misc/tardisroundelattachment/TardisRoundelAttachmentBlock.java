package net.drgmes.dwm.blocks.tardis.misc.tardisroundelattachment;

import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class TardisRoundelAttachmentBlock extends BaseRotatableWaterloggedBlock {
    public static final BooleanProperty LIT = Properties.LIT;

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(2.0, 2.0, 14.5, 14.0, 14.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(2.0, 2.0, 0.0, 14.0, 14.0, 1.5);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 2.0, 2.0, 1.5, 14.0, 14.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(14.5, 2.0, 2.0, 16.0, 14.0, 14.0);

    private final boolean doLit;

    public TardisRoundelAttachmentBlock(Settings settings, boolean doLit) {
        super(settings);
        this.doLit = doLit;
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext context) {
        return switch (blockState.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> WEST_SHAPE;
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(LIT);
    }

    @Override
    protected BlockState getDefaultBlockState() {
        return super.getDefaultBlockState().with(LIT, this.doLit);
    }
}

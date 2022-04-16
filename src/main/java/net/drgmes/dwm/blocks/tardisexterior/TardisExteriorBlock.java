package net.drgmes.dwm.blocks.tardisexterior;

import net.drgmes.dwm.utils.base.blockentities.BaseRotatableWaterloggedBlockEntity;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TardisExteriorBlock extends BaseRotatableWaterloggedBlockEntity {
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public TardisExteriorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisExteriorBlockEntity(blockPos, blockState);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext ctx) {
        double size = 15.9;
        return Block.box(16 - size, 0.0D, 16 - size, size, 16.0D, size);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HALF);
    }

    @Override
    protected BlockState getDefaultState() {
        return super.getDefaultState().setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();
        return blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(context) ? super.getStateForPlacement(context) : null;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState newBlockState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos newBlockPos) {
        DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);
        if (direction.getAxis() != Direction.Axis.Y || doubleBlockHalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || newBlockState.is(this) && newBlockState.getValue(HALF) != doubleBlockHalf) {
            return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, newBlockState, levelAccessor, blockPos, newBlockPos);
        }
        else {
            return Blocks.AIR.defaultBlockState();
        }
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        level.setBlock(blockPos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        if (blockState.getValue(HALF) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(blockState, levelReader, blockPos);
        }
        else {
            BlockState blockStateBelow = levelReader.getBlockState(blockPos.below());
            if (blockState.getBlock() != this) return super.canSurvive(blockState, levelReader, blockPos);
            return blockStateBelow.is(this) && blockStateBelow.getValue(HALF) == DoubleBlockHalf.LOWER;
        }
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (level.isClientSide) return;
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) return;
        if (entity.getDirection().getOpposite() != blockState.getValue(FACING)) return;

        MinecraftServer server = level.getServer();
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (server != null && blockEntity instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
            TardisHelper.teleport(entity, TardisHelper.setupTardis(server, tardisExteriorBlockEntity.getTardisDimUUID()));
        }
    }
}

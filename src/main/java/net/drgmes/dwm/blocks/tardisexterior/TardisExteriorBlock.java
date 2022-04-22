package net.drgmes.dwm.blocks.tardisexterior;

import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedEntityBlock;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TardisExteriorBlock extends BaseRotatableWaterloggedEntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public TardisExteriorBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisExteriorBlockEntity(blockPos, blockState);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
        builder.add(HALF);
    }

    @Override
    protected BlockState getDefaultState() {
        return super.getDefaultState()
            .setValue(OPEN, false)
            .setValue(HALF, DoubleBlockHalf.LOWER);
    }

    @Override
    public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext ctx) {
        double size = 15.9;
        return Block.box(16 - size, 0.0D, 16 - size, size, 16.0D, size);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();

        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(context)) {
            return super.getStateForPlacement(context).setValue(HALF, DoubleBlockHalf.LOWER);
        }

        return null;
    }

    @Override
    @SuppressWarnings("deprecation")
    public BlockState updateShape(BlockState blockState, Direction direction, BlockState newBlockState, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos newBlockPos) {
        DoubleBlockHalf doubleBlockHalf = blockState.getValue(HALF);

        if (direction.getAxis() == Direction.Axis.Y && doubleBlockHalf == DoubleBlockHalf.LOWER == (direction == Direction.UP)) {
            return newBlockState.is(this) && newBlockState.getValue(HALF) != doubleBlockHalf
                ? blockState.setValue(FACING, newBlockState.getValue(FACING))
                : Blocks.AIR.defaultBlockState();
        }

        return doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !blockState.canSurvive(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.updateShape(blockState, direction, newBlockState, levelAccessor, blockPos, newBlockPos);
    }

    @Override
    public void setPlacedBy(Level level, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        level.setBlock(blockPos.above(), blockState.setValue(HALF, DoubleBlockHalf.UPPER), 3);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        BlockPos blockPosBelow = blockPos.below();
        BlockState blockStateBelow = levelReader.getBlockState(blockPosBelow);
        return blockState.getValue(HALF) == DoubleBlockHalf.LOWER ? blockStateBelow.isFaceSturdy(levelReader, blockPosBelow, Direction.UP) : blockStateBelow.is(this);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) {
            blockPos = blockPos.below();
            blockState = level.getBlockState(blockPos);
        }

        boolean isDoorsOpened = !blockState.getValue(OPEN);
        level.setBlock(blockPos, blockState.setValue(OPEN, isDoorsOpened), 10);
        level.levelEvent(player, isDoorsOpened ? 1006 : 1012, blockPos, 0);
        level.gameEvent(player, isDoorsOpened ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);

        ServerLevel tardisLevel = this.getTardisDimension(level, blockPos);
        if (tardisLevel != null) {
            tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                if (!provider.isValid()) return;

                provider.updateDoorsState(isDoorsOpened, false);
                provider.updateConsoleTiles();
            });
        }

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!blockState.getValue(OPEN)) return;
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) return;
        if (!blockPos.relative(blockState.getValue(FACING)).closerThan(entity.blockPosition(), 0.1)) return;

        ServerLevel tardisLevel = this.getTardisDimension(level, blockPos);
        if (tardisLevel != null) TardisHelper.teleportToTardis(entity, tardisLevel);
    }

    public ServerLevel getTardisDimension(Level level, BlockPos blockPos) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);

        if (!level.isClientSide && blockEntity instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
            return tardisExteriorBlockEntity.getTardisDimension(level, blockPos);
        }

        return null;
    }
}

package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.setup.ModBlockEntities;
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
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TardisExteriorPoliceBoxBlock extends BaseRotatableWaterloggedEntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public TardisExteriorPoliceBoxBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) return null;
        return new TardisExteriorPoliceBoxBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, ModBlockEntities.TARDIS_EXTERIOR.get(), (l, bp, bs, blockEntity) -> {
            ((TardisExteriorPoliceBoxBlockEntity) blockEntity).tick();
        });
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(OPEN);
        builder.add(LIT);
        builder.add(HALF);
    }

    @Override
    protected BlockState getDefaultState() {
        return super.getDefaultState()
            .setValue(OPEN, false)
            .setValue(LIT, false)
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

        if (level.getBlockEntity(blockPos) instanceof TardisExteriorPoliceBoxBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.remat();
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldBlockState, boolean isMoving) {
        if (level.getBlockEntity(blockPos) instanceof TardisExteriorPoliceBoxBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.loadAll();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (level.getBlockEntity(blockPos) instanceof TardisExteriorPoliceBoxBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.unloadAll();
        }

        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    public boolean canSurvive(BlockState blockState, LevelReader levelReader, BlockPos blockPos) {
        return blockState.getValue(HALF) == DoubleBlockHalf.LOWER || levelReader.getBlockState(blockPos.below()).is(this);
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.OFF_HAND) return InteractionResult.PASS;
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) {
            blockPos = blockPos.below();
            blockState = level.getBlockState(blockPos);
        }

        if (level.getBlockEntity(blockPos) instanceof TardisExteriorPoliceBoxBlockEntity tardisExteriorBlockEntity) {
            if (tardisExteriorBlockEntity.getMaterializedPercent() < 100) return InteractionResult.PASS;
        }

        ServerLevel tardisLevel = this.getTardisDimension(level, blockPos);
        if (tardisLevel == null) return InteractionResult.FAIL;

        tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            if (!provider.isValid()) return;

            if (player.isShiftKeyDown()) {
                provider.setDoorsLockState(player, !provider.isDoorsLocked(), true);
                provider.updateConsoleTiles();
                return;
            }

            if (provider.setDoorsOpenState(!provider.isDoorsOpened(), true)) {
                provider.updateConsoleTiles();
            }
        });

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!blockState.getValue(OPEN)) return;
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) return;
        if (!blockPos.relative(blockState.getValue(FACING)).closerThan(entity.blockPosition(), 0.1)) return;

        if (level.getBlockEntity(blockPos) instanceof TardisExteriorPoliceBoxBlockEntity tardisExteriorBlockEntity) {
            TardisHelper.saveBlocksForBoti(level, blockPos, tardisExteriorBlockEntity.getTardisLevelUUID() + "-exterior");
        }

        ServerLevel tardisLevel = this.getTardisDimension(level, blockPos);
        if (tardisLevel != null) TardisHelper.teleportToTardis(entity, tardisLevel);
    }

    private ServerLevel getTardisDimension(Level level, BlockPos blockPos) {
        if (level.getBlockEntity(blockPos) instanceof TardisExteriorPoliceBoxBlockEntity tardisExteriorBlockEntity) {
            return tardisExteriorBlockEntity.getTardisDimension(level);
        }

        return null;
    }
}

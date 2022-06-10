package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.items.tardiskey.TardisKeyItem;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedEntityBlock;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.registries.RegistryObject;

public abstract class BaseTardisExteriorBlock<C extends BaseTardisExteriorBlockEntity> extends BaseRotatableWaterloggedEntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final BooleanProperty LIT = BlockStateProperties.LIT;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    private final RegistryObject<BlockEntityType<C>> blockEntityTypeObject;

    public BaseTardisExteriorBlock(BlockBehaviour.Properties properties, RegistryObject<BlockEntityType<C>> blockEntityTypeObject) {
        super(properties);
        this.blockEntityTypeObject = blockEntityTypeObject;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, this.blockEntityTypeObject.get(), (l, bp, bs, blockEntity) -> {
            ((BaseTardisExteriorBlockEntity) blockEntity).tick();
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

        if (level.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.remat();

            ServerLevel tardisLevel = this.getTardisLevel(level, blockPos);
            if (tardisLevel == null) return;

            tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                if (tardis.isValid()) tardis.setOwnerUUID(entity.getUUID());
            });
        }
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldBlockState, boolean isMoving) {
        if (level.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.loadAll();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (level.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
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
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) {
            blockPos = blockPos.below();
            blockState = level.getBlockState(blockPos);
        }

        if (level.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (tardisExteriorBlockEntity.getMaterializedPercent() < 100) return InteractionResult.PASS;
        }

        BlockPos finalBlockPos = blockPos;
        ServerLevel tardisLevel = this.getTardisLevel(level, blockPos);
        if (tardisLevel == null) return InteractionResult.SUCCESS;

        tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            if (!tardis.isValid()) return;

            String tardisDimUUID = tardis.getLevel().dimension().location().getPath();
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            CompoundTag heldItemTag = heldItem.getOrCreateTag();

            if (heldItem.getItem() instanceof TardisKeyItem) {
                if (!heldItemTag.contains("tardisDimUUID")) {
                    if (!tardis.getOwnerUUID().equals(player.getUUID())) return;
                    heldItemTag.putString("tardisDimUUID", tardisDimUUID);
                }

                if (!heldItemTag.getString("tardisDimUUID").equalsIgnoreCase(tardisDimUUID)) {
                    return;
                }

                if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), null)) {
                    player.displayClientMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                    level.gameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                    tardis.updateConsoleTiles();
                }

                return;
            }

            if (player.isShiftKeyDown()) {
                if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), player)) {
                    player.displayClientMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                    level.gameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                    tardis.updateConsoleTiles();
                }

                return;
            }

            if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                level.gameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                tardis.updateConsoleTiles();
            }
        });

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (!blockState.getValue(OPEN)) return;
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) return;
        if (!blockPos.relative(blockState.getValue(FACING)).closerThan(entity.blockPosition(), 0.1)) return;

        ServerLevel tardisLevel = this.getTardisLevel(level, blockPos);
        if (tardisLevel != null) {
            tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                if (tardis.isValid() && tardis.isDoorsOpened()) {
                    TardisHelper.teleportToTardis(entity, tardisLevel);
                }
            });
        }
    }

    private ServerLevel getTardisLevel(Level level, BlockPos blockPos) {
        if (level.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            return tardisExteriorBlockEntity.getTardisLevel(level);
        }

        return null;
    }
}

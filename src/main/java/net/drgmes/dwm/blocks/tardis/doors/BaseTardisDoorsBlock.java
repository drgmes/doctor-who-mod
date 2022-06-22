package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.items.tardiskey.TardisKeyItem;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedEntityBlock;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.BlockHitResult;

public abstract class BaseTardisDoorsBlock extends BaseRotatableWaterloggedEntityBlock {
    public static final BooleanProperty OPEN = BlockStateProperties.OPEN;
    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public BaseTardisDoorsBlock(BlockBehaviour.Properties properties) {
        super(properties);
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
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos blockPos = context.getClickedPos();

        if (blockPos.getY() < level.getMaxBuildHeight() - 1 && level.getBlockState(blockPos.above()).canBeReplaced(context)) {
            return super.getStateForPlacement(context)
                .setValue(OPEN, false)
                .setValue(HALF, DoubleBlockHalf.LOWER);
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
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (player.isCreative()) return;
        super.playerWillDestroy(level, blockPos, blockState, player);
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.IGNORE;
    }

    @Override
    public InteractionResult use(BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            if (!tardis.isValid()) return;

            String tardisLevelUUID = tardis.getLevel().dimension().location().getPath();
            ItemStack heldItem = player.getItemInHand(InteractionHand.MAIN_HAND);
            CompoundTag heldItemTag = heldItem.getOrCreateTag();

            if (heldItem.getItem() instanceof TardisKeyItem) {
                if (!heldItemTag.contains("tardisLevelUUID")) {
                    if (!tardis.getOwnerUUID().equals(player.getUUID())) return;
                    heldItemTag.putString("tardisLevelUUID", tardisLevelUUID);
                }

                if (!heldItemTag.getString("tardisLevelUUID").equalsIgnoreCase(tardisLevelUUID)) {
                    return;
                }

                if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), null)) {
                    player.displayClientMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                    level.gameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
                    tardis.updateConsoleTiles();
                }

                return;
            }

            if (player.isShiftKeyDown()) {
                if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), null)) {
                    player.displayClientMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                    level.gameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
                    tardis.updateConsoleTiles();
                }

                return;
            }

            if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                level.gameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
                tardis.updateConsoleTiles();
            }
        });

        return InteractionResult.sidedSuccess(level.isClientSide);
    }

    @Override
    public void entityInside(BlockState blockState, Level level, BlockPos blockPos, Entity entity) {
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) return;
        if (blockPos.relative(blockState.getValue(FACING).getOpposite()).distToCenterSqr(entity.position()) > 1.35D)
            return;

        level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            if (tardis.isValid() && tardis.isDoorsOpened()) {
                TardisHelper.teleportFromTardis(entity, level.getServer());
            }
        });
    }
}

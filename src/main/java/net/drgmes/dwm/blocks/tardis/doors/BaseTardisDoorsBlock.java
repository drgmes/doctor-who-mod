package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.items.tardiskey.TardisKeyItem;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedDoubleBlockWithEntity;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.function.Supplier;

public abstract class BaseTardisDoorsBlock<C extends BaseTardisDoorsBlockEntity> extends BaseRotatableWaterloggedDoubleBlockWithEntity {
    public static final BooleanProperty OPEN = Properties.OPEN;

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape NORTH_OPENED_SHAPE = Block.createCuboidShape(-6.0, 0.0, 13.0, 0.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
    protected static final VoxelShape SOUTH_OPENED_SHAPE = Block.createCuboidShape(-6.0, 0.0, 0.0, 0.0, 16.0, 3.0);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0);
    protected static final VoxelShape EAST_OPENED_SHAPE = Block.createCuboidShape(0.0, 0.0, -6.0, 3.0, 16.0, 0.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0);
    protected static final VoxelShape WEST_OPENED_SHAPE = Block.createCuboidShape(13.0, 0.0, -6.0, 16.0, 16.0, 0.0);

    private final Supplier<BlockEntityType<C>> blockEntityTypeSupplier;

    public BaseTardisDoorsBlock(AbstractBlock.Settings settings, Supplier<BlockEntityType<C>> blockEntityTypeSupplier) {
        super(settings);
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType != this.blockEntityTypeSupplier.get() ? null : (l, bp, bs, blockEntity) -> ((BaseTardisDoorsBlockEntity) blockEntity).tick();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) blockPos = blockPos.down();
        BlockPos finalBlockPos = blockPos;

        if (world instanceof ServerWorld serverWorld && TardisHelper.isTardisDimension(world)) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;

                String tardisId = DimensionHelper.getWorldId(tardis.getWorld());
                ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);
                NbtCompound heldItemTag = heldItem.getOrCreateNbt();

                if (heldItem.getItem() instanceof TardisKeyItem) {
                    if (!heldItemTag.contains("tardisId")) {
                        if (!tardis.getOwner().equals(player.getUuid())) return;
                        heldItemTag.putString("tardisId", tardisId);
                    }

                    if (!heldItemTag.getString("tardisId").equalsIgnoreCase(tardisId)) {
                        return;
                    }

                    if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), null)) {
                        player.sendMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                        world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                        tardis.updateConsoleTiles();
                    }

                    return;
                }

                if (player.isSneaking()) {
                    if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), null)) {
                        player.sendMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                        world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                        tardis.updateConsoleTiles();
                    }

                    return;
                }

                if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                    world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                    tardis.updateConsoleTiles();
                }
            });
        }

        return ActionResult.success(world.isClient);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext context) {
        if (blockState.get(OPEN)) {
            return switch (blockState.get(FACING)) {
                case NORTH -> VoxelShapes.union(NORTH_OPENED_SHAPE, NORTH_OPENED_SHAPE.offset(1.5, 0.0, 0.0));
                case SOUTH -> VoxelShapes.union(SOUTH_OPENED_SHAPE, SOUTH_OPENED_SHAPE.offset(1.5, 0.0, 0.0));
                case EAST -> VoxelShapes.union(EAST_OPENED_SHAPE, EAST_OPENED_SHAPE.offset(0, 0, 1.5));
                default -> VoxelShapes.union(WEST_OPENED_SHAPE, WEST_OPENED_SHAPE.offset(0, 0, 1.5));
            };
        }

        return switch (blockState.get(FACING)) {
            case NORTH -> NORTH_SHAPE;
            case SOUTH -> SOUTH_SHAPE;
            case EAST -> EAST_SHAPE;
            default -> WEST_SHAPE;
        };
    }

    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity player) {
        if (player.isCreative()) return;
        super.onBreak(world, blockPos, blockState, player);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN);
    }

    @Override
    protected BlockState getDefaultBlockState() {
        return super.getDefaultState().with(OPEN, false);
    }

    @Override
    protected BlockState syncNeighborState(BlockState blockState, BlockState neighborBlockState) {
        return super.syncNeighborState(blockState, neighborBlockState).with(OPEN, neighborBlockState.get(OPEN));
    }
}

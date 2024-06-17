package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.doors.TardisDoorsTypeEntry;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedDoubleBlockWithEntity;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
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
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.event.GameEvent;

public abstract class BaseTardisDoorsBlock<C extends BaseTardisDoorsBlockEntity> extends BaseRotatableWaterloggedDoubleBlockWithEntity {
    public static final BooleanProperty OPEN = Properties.OPEN;

    protected final TardisDoorsTypeEntry doorsType;
    protected final float shapeOffset;

    protected final VoxelShape shapeNorth;
    protected final VoxelShape shapeSouth;
    protected final VoxelShape shapeEast;
    protected final VoxelShape shapeWest;

    protected final VoxelShape shapeNorthOpened;
    protected final VoxelShape shapeSouthOpened;
    protected final VoxelShape shapeEastOpened;
    protected final VoxelShape shapeWestOpened;

    public BaseTardisDoorsBlock(AbstractBlock.Settings settings, TardisDoorsTypeEntry doorsType, VoxelShape shapeNorth, VoxelShape shapeSouth, VoxelShape shapeEast, VoxelShape shapeWest, VoxelShape shapeNorthOpened, VoxelShape shapeSouthOpened, VoxelShape shapeEastOpened, VoxelShape shapeWestOpened, float shapeOffset) {
        super(settings);

        this.doorsType = doorsType;
        this.shapeNorth = shapeNorth;
        this.shapeSouth = shapeSouth;
        this.shapeEast = shapeEast;
        this.shapeWest = shapeWest;
        this.shapeNorthOpened = shapeNorthOpened;
        this.shapeSouthOpened = shapeSouthOpened;
        this.shapeEastOpened = shapeEastOpened;
        this.shapeWestOpened = shapeWestOpened;
        this.shapeOffset = shapeOffset;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) return null;
        return this.doorsType.getBlockEntityType().instantiate(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType != this.doorsType.getBlockEntityType() ? null : (l, bp, bs, blockEntity) -> {
            ((BaseTardisDoorsBlockEntity) blockEntity).tick();
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN);
    }

    @Override
    protected BlockState getDefaultBlockState() {
        return super.getDefaultBlockState().with(OPEN, false);
    }

    @Override
    protected BlockState syncNeighborState(BlockState blockState, BlockState neighborBlockState) {
        return super.syncNeighborState(blockState, neighborBlockState).with(OPEN, neighborBlockState.get(OPEN));
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext context) {
        if (blockState.get(OPEN)) {
            return switch (blockState.get(FACING)) {
                case NORTH -> VoxelShapes.union(this.shapeNorthOpened, this.shapeNorthOpened.offset(this.shapeOffset, 0.0, 0.0));
                case SOUTH -> VoxelShapes.union(this.shapeSouthOpened, this.shapeSouthOpened.offset(this.shapeOffset, 0.0, 0.0));
                case EAST -> VoxelShapes.union(this.shapeEastOpened, this.shapeEastOpened.offset(0, 0, this.shapeOffset));
                default -> VoxelShapes.union(this.shapeWestOpened, this.shapeWestOpened.offset(0, 0, this.shapeOffset));
            };
        }

        return switch (blockState.get(FACING)) {
            case NORTH -> this.shapeNorth;
            case SOUTH -> this.shapeSouth;
            case EAST -> this.shapeEast;
            default -> this.shapeWest;
        };
    }

    @Override
    public void onBreak(World world, BlockPos blockPos, BlockState blockState, PlayerEntity player) {
        if (!world.isClient && player.isCreative()) {
            BlockPos tmpBlockPos;
            BlockState tmpBlockState;
            DoubleBlockHalf doubleBlockHalf = blockState.get(HALF);

            if (doubleBlockHalf == DoubleBlockHalf.UPPER && (tmpBlockState = world.getBlockState(tmpBlockPos = blockPos.down())).isOf(blockState.getBlock()) && tmpBlockState.get(HALF) == DoubleBlockHalf.LOWER) {
                BlockState tmpBlockState2 = tmpBlockState.contains(Properties.WATERLOGGED) && tmpBlockState.get(Properties.WATERLOGGED) ? Blocks.WATER.getDefaultState() : Blocks.AIR.getDefaultState();
                world.setBlockState(tmpBlockPos, tmpBlockState2, Block.NOTIFY_ALL | Block.SKIP_DROPS);
                world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, tmpBlockPos, Block.getRawIdFromState(tmpBlockState)); // 2001 event
            }
        }

        super.onBreak(world, blockPos, blockState, player);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) blockPos = blockPos.down();
        BlockPos finalBlockPos = blockPos;

        if (world instanceof ServerWorld serverWorld && TardisHelper.isTardisDimension(world)) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                String tardisId = tardis.getId();
                ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);
                NbtCompound heldItemTag = heldItem.getOrCreateNbt();

                if (heldItem.getItem() instanceof TardisKeyItem) {
                    if (!heldItemTag.contains("tardisId")) {
                        if (tardis.getOwner() != null && !tardis.getOwner().equals(player.getUuid())) return;
                        if (tardis.getOwner() == null) tardis.setOwner(player.getUuid());
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
    public void onEntityCollision(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
        if (ModCompats.immersivePortals()) return;
        if (!entity.canUsePortals()) return;

        if (world instanceof ServerWorld serverWorld && TardisHelper.isTardisDimension(world)) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                if (!tardis.isDoorsOpened()) return;

                Vec3d pos = Vec3d.ofBottomCenter(tardis.getCurrentExteriorPosition().offset(tardis.getCurrentExteriorFacing()));
                CommonHelper.teleport(entity, DimensionHelper.getWorld(tardis.getCurrentExteriorDimension(), serverWorld.getServer()), pos, tardis.getCurrentExteriorFacing().asRotation());
            });
        }
    }

    public boolean isWooden() {
        return false;
    }
}

package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorEntry;
import net.drgmes.dwm.enums.TardisExteriorState;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedDoubleBlockWithEntity;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public abstract class BaseTardisExteriorBlock<C extends BaseTardisExteriorBlockEntity> extends BaseRotatableWaterloggedDoubleBlockWithEntity implements Portal {
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty LIT = Properties.LIT;

    protected final TardisExteriorEntry exteriorType;

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.1, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 15.9);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 15.9, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.1, 0.0, 0.0, 16.0, 16.0, 16.0);

    public BaseTardisExteriorBlock(AbstractBlock.Settings settings, TardisExteriorEntry exteriorType) {
        super(settings);
        this.exteriorType = exteriorType;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) return null;
        return this.exteriorType.getBlockEntityType().instantiate(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType != this.exteriorType.getBlockEntityType() ? null : (l, bp, bs, blockEntity) -> {
            ((BaseTardisExteriorBlockEntity) blockEntity).tick();
        };
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(OPEN);
        builder.add(LIT);
    }

    @Override
    protected BlockState getDefaultBlockState() {
        return super.getDefaultBlockState()
            .with(OPEN, false)
            .with(LIT, false);
    }

    @Override
    protected BlockState syncNeighborState(BlockState blockState, BlockState neighborBlockState) {
        return super.syncNeighborState(blockState, neighborBlockState)
            .with(OPEN, neighborBlockState.get(OPEN))
            .with(LIT, neighborBlockState.get(LIT));
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
    @SuppressWarnings("deprecation")
    public void onStateReplaced(BlockState blockState, World world, BlockPos blockPos, BlockState newBlockState, boolean moved) {
        if (!blockState.isOf(newBlockState.getBlock()) && world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            TardisStateManager.get(tardisExteriorBlockEntity.getTardisWorld()).ifPresent((tardis) -> {
                tardis.setDoorsOpenState(false);
            });
        }

        super.onStateReplaced(blockState, world, blockPos, newBlockState, moved);
    }

    @Override
    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.init();
            tardisExteriorBlockEntity.remat();

            TardisStateManager.get(tardisExteriorBlockEntity.getOrCreateTardisWorld()).ifPresent((tardis) -> {
                tardis.setOwner(entity.getUuid());
                tardis.setExteriorType(this.exteriorType);
                tardis.setBrokenState(false);
                tardis.setDoorsLockState(false, null);
            });
        }

        super.onPlaced(world, blockPos, blockState, entity, itemStack);
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, BlockHitResult hit) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) blockPos = blockPos.down();
        BlockPos finalBlockPos = blockPos;

        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (tardisExteriorBlockEntity.getExteriorState() != TardisExteriorState.MATERIALIZED) return ActionResult.PASS;

            ItemStack heldItemStack = player.getStackInHand(Hand.MAIN_HAND);
            NbtCompound heldItemTag = CommonHelper.getItemStackData(heldItemStack).copyNbt();

            if (tardisExteriorBlockEntity.tardisId == null && !(heldItemStack.getItem() instanceof TardisKeyItem)) {
                player.sendMessage(DWM.TEXTS.TARDIS_LOCKED, true);
                ModSounds.playTardisDoorsKnockSound(world, finalBlockPos);
                return ActionResult.success(world.isClient);
            }

            TardisStateManager.get(tardisExteriorBlockEntity.getOrCreateTardisWorld()).ifPresent((tardis) -> {
                String tardisId = tardis.getId();

                tardis.init();
                if (tardis.getExteriorType() == null) tardis.setExteriorType(this.exteriorType);

                if (heldItemStack.getItem() instanceof TardisKeyItem) {
                    if (!heldItemTag.contains("tardisId")) {
                        if (tardis.getOwner() != null && !tardis.getOwner().equals(player.getUuid())) return;
                        if (tardis.getOwner() == null) tardis.setOwner(player.getUuid());

                        CommonHelper.updateItemStackData(heldItemStack, (tag) -> {
                            tag.putString("tardisId", tardisId);
                        });
                    }

                    if (!heldItemTag.getString("tardisId").equalsIgnoreCase(tardisId)) {
                        return;
                    }

                    CommonHelper.updateItemStackData(heldItemStack, (tag) -> {
                        tag.putString("tardisPos", tardis.getCurrentExteriorPosition().toShortString());
                    });

                    if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), null)) {
                        player.sendMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                        world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                        tardis.markConsoleTilesUpdated();
                    }

                    return;
                }

                if (player.isSneaking()) {
                    if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), player)) {
                        player.sendMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                        world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                        tardis.markConsoleTilesUpdated();
                    }

                    return;
                }

                if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                    world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                    tardis.markConsoleTilesUpdated();
                    return;
                }

                if (tardis.isDoorsLocked()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_LOCKED, true);
                    ModSounds.playTardisDoorsKnockSound(world, finalBlockPos);
                    ModSounds.playTardisDoorsKnockSound(tardis.getWorld(), tardis.getEntrancePosition());
                }
            });
        }

        return ActionResult.success(world.isClient);
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onEntityCollision(BlockState blockState, World world, BlockPos blockPos, Entity entity) {
        if (ModCompats.immersivePortals()) return;
        if (world.isClient || !entity.canUsePortals(false)) return;

        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            TardisStateManager.get(tardisExteriorBlockEntity.getTardisWorld()).ifPresent((tardis) -> {
                if (!tardis.isDoorsOpened()) return;
                entity.tryUsePortal(this, blockPos);
            });
        }
    }

    @Override
    public @Nullable TeleportTarget createTeleportTarget(ServerWorld world, Entity entity, BlockPos blockPos) {
        if (entity instanceof ServerPlayerEntity player && player.isInTeleportationState()) return null;
        if (!entity.canUsePortals(false)) return null;

        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            Optional<TardisStateManager> tardisHolder = TardisStateManager.get(tardisExteriorBlockEntity.getTardisWorld());
            if (tardisHolder.isEmpty() || !tardisHolder.get().isDoorsOpened()) return null;

            Direction facing = tardisHolder.get().getEntranceFacing();
            ServerWorld destination = tardisHolder.get().getWorld();
            Vec3d position = Vec3d.ofBottomCenter(tardisHolder.get().getEntrancePosition().offset(facing));
            TeleportTarget.PostDimensionTransition transition = TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET);

            return new TeleportTarget(destination, position, entity.getVelocity(), facing.asRotation(), 0, transition);
        }

        return null;
    }

    public boolean isWooden() {
        return false;
    }
}

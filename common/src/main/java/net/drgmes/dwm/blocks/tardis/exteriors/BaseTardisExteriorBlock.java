package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedDoubleBlockWithEntity;
import net.drgmes.dwm.utils.builders.BlockEntityBuilder;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.function.Supplier;

public abstract class BaseTardisExteriorBlock<C extends BaseTardisExteriorBlockEntity> extends BaseRotatableWaterloggedDoubleBlockWithEntity {
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty LIT = Properties.LIT;

    protected static final VoxelShape NORTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.1, 16.0, 16.0, 16.0);
    protected static final VoxelShape SOUTH_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 15.9);
    protected static final VoxelShape EAST_SHAPE = Block.createCuboidShape(0.0, 0.0, 0.0, 15.9, 16.0, 16.0);
    protected static final VoxelShape WEST_SHAPE = Block.createCuboidShape(0.1, 0.0, 0.0, 16.0, 16.0, 16.0);

    private final Supplier<BlockEntityBuilder<C>> blockEntityBuilderSupplier;

    public BaseTardisExteriorBlock(AbstractBlock.Settings settings, Supplier<BlockEntityBuilder<C>> blockEntityBuilderSupplier) {
        super(settings);
        this.blockEntityBuilderSupplier = blockEntityBuilderSupplier;
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) return null;
        return this.blockEntityBuilderSupplier.get().getBlockEntityType().instantiate(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType != this.blockEntityBuilderSupplier.get().getBlockEntityType() ? null : (l, bp, bs, blockEntity) -> {
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
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) blockPos = blockPos.down();
        BlockPos finalBlockPos = blockPos;

        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (tardisExteriorBlockEntity.getMaterializedPercent() < 100) return ActionResult.PASS;

            ItemStack heldItem = player.getStackInHand(Hand.MAIN_HAND);
            NbtCompound heldItemTag = heldItem.getOrCreateNbt();

            if (tardisExteriorBlockEntity.tardisId == null && !(heldItem.getItem() instanceof TardisKeyItem)) {
                player.sendMessage(DWM.TEXTS.TARDIS_LOCKED, true);
                ModSounds.playTardisDoorsKnockSound(world, finalBlockPos);
                return ActionResult.success(world.isClient);
            }

            TardisStateManager.get(tardisExteriorBlockEntity.getOrCreateTardisWorld()).ifPresent((tardis) -> {
                String tardisId = tardis.getId();

                tardis.init();
                tardisExteriorBlockEntity.update();

                if (heldItem.getItem() instanceof TardisKeyItem) {
                    if (!heldItemTag.contains("tardisId")) {
                        if (tardis.getOwner() != null && !tardis.getOwner().equals(player.getUuid())) return;
                        if (tardis.getOwner() == null) tardis.setOwner(player.getUuid());
                        heldItemTag.putString("tardisId", tardisId);
                    }

                    if (!heldItemTag.getString("tardisId").equalsIgnoreCase(tardisId)) {
                        return;
                    }

                    heldItemTag.putString("tardisPos", tardis.getCurrentExteriorPosition().toShortString());

                    if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), null)) {
                        player.sendMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                        world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                        tardis.updateConsoleTiles();
                    }

                    return;
                }

                if (player.isSneaking()) {
                    if (tardis.setDoorsLockState(!tardis.isDoorsLocked(), player)) {
                        player.sendMessage(tardis.isDoorsLocked() ? DWM.TEXTS.TARDIS_DOORS_LOCKED : DWM.TEXTS.TARDIS_DOORS_UNLOCKED, true);
                        world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                        tardis.updateConsoleTiles();
                    }

                    return;
                }

                if (tardis.setDoorsOpenState(!tardis.isDoorsOpened())) {
                    world.emitGameEvent(player, tardis.isDoorsOpened() ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, finalBlockPos);
                    tardis.updateConsoleTiles();
                    return;
                }

                if (tardis.isDoorsLocked()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_LOCKED, true);
                    ModSounds.playTardisDoorsKnockSound(world, finalBlockPos);
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

        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            TardisStateManager.get(tardisExteriorBlockEntity.getOrCreateTardisWorld()).ifPresent((tardis) -> {
                if (!tardis.isDoorsOpened()) return;
                Vec3d pos = Vec3d.ofBottomCenter(tardis.getEntrancePosition().offset(tardis.getEntranceFacing()));
                CommonHelper.teleport(entity, tardis.getWorld(), pos, tardis.getEntranceFacing().asRotation());
            });
        }
    }

    @Override
    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.remat();

            TardisStateManager.get(tardisExteriorBlockEntity.getOrCreateTardisWorld()).ifPresent((tardis) -> {
                tardis.setOwner(entity.getUuid());
                tardis.setBrokenState(false);
                tardis.setDoorsLockState(false, null);
            });
        }

        super.onPlaced(world, blockPos, blockState, entity, itemStack);
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

    public boolean isWooden() {
        return false;
    }
}

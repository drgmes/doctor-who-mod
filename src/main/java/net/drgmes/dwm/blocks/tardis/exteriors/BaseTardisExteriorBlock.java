package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedDoubleBlockWithEntity;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.block.enums.DoubleBlockHalf;
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
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;

import java.util.function.Supplier;

public abstract class BaseTardisExteriorBlock<C extends BaseTardisExteriorBlockEntity> extends BaseRotatableWaterloggedDoubleBlockWithEntity {
    public static final BooleanProperty OPEN = Properties.OPEN;
    public static final BooleanProperty LIT = Properties.LIT;

    private final Supplier<BlockEntityType<C>> blockEntityTypeSupplier;

    public BaseTardisExteriorBlock(AbstractBlock.Settings settings, Supplier<BlockEntityType<C>> blockEntityTypeSupplier) {
        super(settings);
        this.blockEntityTypeSupplier = blockEntityTypeSupplier;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(World world, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return blockEntityType != this.blockEntityTypeSupplier.get() ? null : (l, bp, bs, blockEntity) -> ((BaseTardisExteriorBlockEntity) blockEntity).tick();
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) blockPos = blockPos.down();
        BlockPos finalBlockPos = blockPos;

        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (tardisExteriorBlockEntity.getMaterializedPercent() < 100) return ActionResult.PASS;

            TardisStateManager.get(tardisExteriorBlockEntity.getTardisWorld(true)).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;

                String tardisId = DimensionHelper.getWorldId(tardis.getWorld());
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
                }
            });
        }

        return ActionResult.success(world.isClient);
    }

    @Override
    @SuppressWarnings("deprecation")
    public VoxelShape getOutlineShape(BlockState blockState, BlockView blockView, BlockPos blockPos, ShapeContext context) {
        return VoxelShapes.cuboid(VoxelShapes.fullCube().getBoundingBox().contract(0.1));
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
    public void onPlaced(World world, BlockPos blockPos, BlockState blockState, LivingEntity entity, ItemStack itemStack) {
        if (world.getBlockEntity(blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.remat();

            TardisStateManager.get(tardisExteriorBlockEntity.getTardisWorld(false)).ifPresent((tardis) -> {
                tardis.setOwner(entity.getUuid());
            });
        }

        super.onPlaced(world, blockPos, blockState, entity, itemStack);
    }

    @Override
    protected BlockState syncNeighborState(BlockState blockState, BlockState neighborBlockState) {
        return super.syncNeighborState(blockState, neighborBlockState)
            .with(OPEN, neighborBlockState.get(OPEN))
            .with(LIT, neighborBlockState.get(LIT));
    }

    public boolean isWooden() {
        return false;
    }
}

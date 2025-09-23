package net.drgmes.dwm.blocks.tardis.misc.tardisteleporter;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.enums.TardisTeleporterEntityTypes;
import net.drgmes.dwm.network.client.TardisTeleporterOpenPacket;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.EntityHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Optional;
import java.util.UUID;

public class TardisTeleporterBlock extends Block implements Waterloggable, BlockEntityProvider {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public TardisTeleporterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultBlockState());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return ModBlockEntities.TARDIS_TELEPORTER.getBlockEntityType().instantiate(blockPos, blockState);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public BlockState getPlacementState(ItemPlacementContext context) {
        BlockState blockState = super.getPlacementState(context);
        FluidState fluidState = context.getWorld().getFluidState(context.getBlockPos());

        return blockState.with(WATERLOGGED, fluidState.isIn(FluidTags.WATER));
    }

    @Override
    @SuppressWarnings("deprecation")
    public FluidState getFluidState(BlockState blockState) {
        return blockState.get(WATERLOGGED) ? Fluids.WATER.getStill(false) : super.getFluidState(blockState);
    }

    @Override
    public void onSteppedOn(World world, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (world.isClient) return;

        if (world.getBlockEntity(blockPos) instanceof TardisTeleporterBlockEntity tardisTeleporterBlockEntity) {
            if (tardisTeleporterBlockEntity.destinationBlockPos == null || tardisTeleporterBlockEntity.allowedEntityTypes.isEmpty()) return;

            if (!(entity instanceof ItemEntity) && !(entity instanceof MobEntity) && !(entity instanceof ServerPlayerEntity)) return;
            if (entity instanceof ItemEntity && !tardisTeleporterBlockEntity.allowedEntityTypes.contains(TardisTeleporterEntityTypes.ITEMS)) return;
            if (entity instanceof MobEntity && !tardisTeleporterBlockEntity.allowedEntityTypes.contains(TardisTeleporterEntityTypes.MOBS)) return;
            if (entity instanceof ServerPlayerEntity && !tardisTeleporterBlockEntity.allowedEntityTypes.contains(TardisTeleporterEntityTypes.PLAYERS)) return;

            Vec3d pos = Vec3d.ofBottomCenter(tardisTeleporterBlockEntity.destinationBlockPos);
            Direction facing = tardisTeleporterBlockEntity.destinationFacing;
            if (facing == null) facing = Direction.NORTH;

            ModSounds.playTardisTeleporterSentSound(world, blockPos);
            EntityHelper.teleport(entity, (ServerWorld) world, pos, facing.asRotation());
            ModSounds.playTardisTeleporterReceivedSound(world, BlockPos.ofFloored(pos));
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public ActionResult onUse(BlockState blockState, World world, BlockPos blockPos, PlayerEntity player, BlockHitResult blockHitResult) {
        if (!(world instanceof ServerWorld serverWorld) || player.isSpectator()) return ActionResult.PASS;

        if (TardisHelper.isTardisDimension(serverWorld)) {
            Optional<TardisStateManager> tardisHolder = TardisStateManager.get(serverWorld);

            if (!tardisHolder.isEmpty()) {
                UUID ownerId = tardisHolder.get().getOwner();

                if (ownerId != null && !ownerId.equals(player.getUuid())) {
                    if (player.isSneaking()) player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
                    return ActionResult.PASS;
                }
            }
        }

        if (!player.isSneaking()) {
            player.sendMessage(DWM.TEXTS.TARDIS_TELEPORTER_MUST_BE_SNEAKING, true);
            return ActionResult.PASS;
        }

        if (!(player instanceof ServerPlayerEntity serverPlayer)) {
            return ActionResult.SUCCESS;
        }

        if (world.getBlockEntity(blockPos) instanceof TardisTeleporterBlockEntity tardisTeleporterBlockEntity) {
            new TardisTeleporterOpenPacket(
                blockPos,
                tardisTeleporterBlockEntity.destinationBlockPos,
                tardisTeleporterBlockEntity.isLocked,
                tardisTeleporterBlockEntity.allowedEntityTypes
            ).sendTo(serverPlayer);
        }

        return ActionResult.SUCCESS;
    }

    protected BlockState getDefaultBlockState() {
        return this.getDefaultState().with(WATERLOGGED, false);
    }
}

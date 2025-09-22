package net.drgmes.dwm.blocks.tardis.misc.tardisteleporter;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.EntityHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockEntityProvider;
import net.minecraft.block.BlockState;
import net.minecraft.block.Waterloggable;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

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
            if (tardisTeleporterBlockEntity.destinationBlockPos == null) return;

            Vec3d pos = Vec3d.ofBottomCenter(tardisTeleporterBlockEntity.destinationBlockPos);
            Direction facing = tardisTeleporterBlockEntity.destinationFacing;
            if (facing == null) facing = Direction.NORTH;

            ModSounds.playTardisTeleporterSentSound(world, blockPos);
            EntityHelper.teleport(entity, (ServerWorld) world, pos, facing.asRotation());
            ModSounds.playTardisTeleporterReceivedSound(world, BlockPos.ofFloored(pos));
        }
    }

    protected BlockState getDefaultBlockState() {
        return this.getDefaultState().with(WATERLOGGED, false);
    }
}

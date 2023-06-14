package net.drgmes.dwm.blocks.tardis.misc.tardisteleporter;

import net.drgmes.dwm.setup.ModSounds;
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
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TardisTeleporterBlock extends Block implements Waterloggable, BlockEntityProvider {
    public static final BooleanProperty WATERLOGGED = Properties.WATERLOGGED;

    public TardisTeleporterBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.getDefaultBlockState());
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisTeleporterBlockEntity(blockPos, blockState);
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
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        super.appendProperties(builder);
        builder.add(WATERLOGGED);
    }

    @Override
    public void onSteppedOn(World world, BlockPos blockPos, BlockState blockState, Entity entity) {
        if (world.getBlockEntity(blockPos) instanceof TardisTeleporterBlockEntity tardisTeleporterBlockEntity) {
            BlockPos bp = tardisTeleporterBlockEntity.destinationBlockPos;

            ModSounds.playTardisTeleporterSentSound(world, blockPos);
            entity.setPitch(0);
            entity.setYaw(tardisTeleporterBlockEntity.destinationFacing.asRotation());
            entity.teleport(bp.getX() + 0.5, bp.getY(), bp.getZ() + 0.5);
            ModSounds.playTardisTeleporterReceivedSound(world, bp);
        }
    }

    protected BlockState getDefaultBlockState() {
        return this.getDefaultState().with(WATERLOGGED, false);
    }
}
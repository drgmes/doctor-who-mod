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
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.Set;

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
        if (world.isClient) return;

        if (world.getBlockEntity(blockPos) instanceof TardisTeleporterBlockEntity tardisTeleporterBlockEntity) {
            Vec3d pos = Vec3d.ofBottomCenter(tardisTeleporterBlockEntity.destinationBlockPos);
            ModSounds.playTardisTeleporterSentSound(world, blockPos);
            entity.teleport((ServerWorld) world, pos.x, pos.y, pos.z, Set.of(), tardisTeleporterBlockEntity.destinationFacing.asRotation(), 0);
            ModSounds.playTardisTeleporterReceivedSound(world, BlockPos.ofFloored(pos));
        }
    }

    protected BlockState getDefaultBlockState() {
        return this.getDefaultState().with(WATERLOGGED, false);
    }
}

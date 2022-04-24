package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.DWMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TardisSystemMaterialization implements ITardisSystem {
    public static enum TardisSystemMaterializationSafeDirection {
        NONE,
        TOP,
        BOTTOM
    }

    public boolean isMaterialized = true;
    private final ITardisLevelData tardisData;

    public TardisSystemMaterialization(ITardisLevelData tardisData) {
        this.tardisData = tardisData;
    }

    @Override
    public void load(CompoundTag tag) {
        this.isMaterialized = tag.getBoolean("isMaterialized");
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("isMaterialized", this.isMaterialized);
        return tag;
    }

    @Override
    public void tick() {
    }

    public boolean demat() {
        if (!this.isMaterialized) return true;

        ServerLevel level = this.tardisData.getLevel();
        if (level != null) {
            ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
            if (exteriorLevel == null) return false;

            this.tardisData.setDoorsState(false, true);
            this.isMaterialized = false;
            this.tardisData.updateConsoleTiles();

            DWMUtils.runInThread("materialization", () -> {
                BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
                BlockState exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);
                if (!Thread.currentThread().isAlive() || Thread.currentThread().isInterrupted()) return;

                if (exteriorBlockState.getBlock() instanceof TardisExteriorBlock) {
                    exteriorLevel.setBlock(exteriorBlockPos, Blocks.AIR.defaultBlockState(), 3);
                    exteriorLevel.setBlock(exteriorBlockPos.above(), Blocks.AIR.defaultBlockState(), 3);
                }
            });

            return true;
        }

        return false;
    }

    public boolean remat() {
        if (this.isMaterialized) return true;

        ServerLevel level = this.tardisData.getLevel();
        if (level != null) {
            ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
            if (exteriorLevel == null) return false;

            if (this.findSafePosition()) {
                BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
                BlockState exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);
                BlockState exteriorUpBlockState = exteriorLevel.getBlockState(exteriorBlockPos.above());
                BlockState tardisExteriorBlockState = ModBlocks.TARDIS_EXTERIOR.get().defaultBlockState();
                BlockState tardisExteriorDownBlockState = ModBlocks.TARDIS_EXTERIOR.get().defaultBlockState();

                tardisExteriorBlockState = tardisExteriorBlockState.setValue(TardisExteriorBlock.FACING, this.tardisData.getCurrentExteriorFacing());
                tardisExteriorBlockState = tardisExteriorBlockState.setValue(TardisExteriorBlock.WATERLOGGED, exteriorBlockState.getFluidState().is(FluidTags.WATER));

                tardisExteriorDownBlockState = tardisExteriorBlockState.setValue(TardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
                tardisExteriorDownBlockState = tardisExteriorDownBlockState.setValue(TardisExteriorBlock.WATERLOGGED, exteriorUpBlockState.getFluidState().is(FluidTags.WATER));

                exteriorLevel.setBlock(exteriorBlockPos, tardisExteriorBlockState, 3);
                exteriorLevel.setBlock(exteriorBlockPos.above(), tardisExteriorDownBlockState, 3);

                if (exteriorLevel.getBlockEntity(exteriorBlockPos) instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
                    tardisExteriorBlockEntity.tardisLevelUUID = level.dimension().location().getPath();

                    this.isMaterialized = true;
                    this.tardisData.updateConsoleTiles();
                    return true;
                }

                this.demat();
            }
        }

        return false;
    }

    private boolean findSafePosition() {
        ServerLevel level = this.tardisData.getLevel();
        if (level == null) return false;

        ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
        if (exteriorLevel == null) return false;

        BlockPos safePosition = this.getSafePosition(exteriorLevel, TardisSystemMaterializationSafeDirection.TOP);
        if (safePosition == null) safePosition = this.getSafePosition(exteriorLevel, TardisSystemMaterializationSafeDirection.BOTTOM);

        if (safePosition != null) {
            this.tardisData.setPosition(safePosition, false);
            this.tardisData.setDestinationPosition(safePosition);
            return true;
        }

        return false;
    }

    private BlockPos getSafePosition(ServerLevel exteriorLevel, TardisSystemMaterializationSafeDirection safeDirection) {
        if (safeDirection == TardisSystemMaterializationSafeDirection.NONE) return null;

        BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
        boolean freeSpaceFound = false;
        boolean maxHeightReached = false;
        BlockPos exteriorFrontBlockPos;
        BlockState exteriorBlockState;
        BlockState exteriorUpBlockState;
        BlockState exteriorDownBlockState;
        BlockState exteriorFrontDownBlockState;
        BlockState exteriorFrontBlockState;
        BlockState exteriorFrontUpBlockState;

        if (safeDirection == TardisSystemMaterializationSafeDirection.TOP) exteriorBlockPos = exteriorBlockPos.below();
        else if (safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) exteriorBlockPos = exteriorBlockPos.above();
        else return null;

        do {
            if (safeDirection == TardisSystemMaterializationSafeDirection.TOP) exteriorBlockPos = exteriorBlockPos.above();
            else if (safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) exteriorBlockPos = exteriorBlockPos.below();

            exteriorFrontBlockPos = exteriorBlockPos.relative(this.tardisData.getCurrentExteriorFacing());

            exteriorDownBlockState = exteriorLevel.getBlockState(exteriorBlockPos.below());
            exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);
            exteriorUpBlockState = exteriorLevel.getBlockState(exteriorBlockPos.above());

            exteriorFrontDownBlockState = exteriorLevel.getBlockState(exteriorFrontBlockPos.below());
            exteriorFrontBlockState = exteriorLevel.getBlockState(exteriorFrontBlockPos);
            exteriorFrontUpBlockState = exteriorLevel.getBlockState(exteriorFrontBlockPos.above());

            boolean isBottomSolid = !this.checkBlockIsEmpty(exteriorDownBlockState) && exteriorFrontDownBlockState.getFluidState().isEmpty();
            boolean isEmpty = this.checkBlockIsEmpty(exteriorBlockState);
            boolean isUpEmpty = this.checkBlockIsEmpty(exteriorUpBlockState);

            boolean isFrontBottomSolid = !this.checkBlockIsEmpty(exteriorFrontDownBlockState) && exteriorFrontDownBlockState.getFluidState().isEmpty();
            boolean isFrontEmpty = this.checkBlockIsEmpty(exteriorFrontBlockState);
            boolean isFrontUpEmpty = this.checkBlockIsEmpty(exteriorFrontUpBlockState);

            freeSpaceFound = isBottomSolid && isEmpty && isUpEmpty && isFrontBottomSolid && isFrontEmpty && isFrontUpEmpty;
            if (safeDirection == TardisSystemMaterializationSafeDirection.TOP) maxHeightReached = exteriorBlockPos.getY() >= exteriorLevel.getMaxBuildHeight() - 1;
            else if (safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) maxHeightReached = exteriorBlockPos.getY() < exteriorLevel.getMinBuildHeight();
        } while (!freeSpaceFound && !maxHeightReached);

        return freeSpaceFound ? exteriorBlockPos : null;
    }

    private boolean checkBlockIsEmpty(BlockState blockState) {
        return blockState.getBlock() instanceof AirBlock || blockState.getFluidState().is(FluidTags.WATER);
    }
}

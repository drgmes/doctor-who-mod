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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TardisSystemMaterialization implements ITardisSystem {
    public static enum TardisSystemMaterializationSafeDirection {
        TOP,
        BOTTOM,
        NONE
    }

    public boolean isMaterialized = true;
    public TardisSystemMaterializationSafeDirection safeDirection;
    private final ITardisLevelData tardisData;

    public TardisSystemMaterialization(ITardisLevelData tardisData) {
        this.tardisData = tardisData;
        this.safeDirection = TardisSystemMaterializationSafeDirection.TOP;
    }

    @Override
    public void load(CompoundTag tag) {
        this.isMaterialized = tag.getBoolean("isMaterialized");
        this.safeDirection = TardisSystemMaterializationSafeDirection.valueOf(tag.getString("safeDirection"));
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("isMaterialized", this.isMaterialized);
        tag.putString("safeDirection", this.safeDirection.name());

        return tag;
    }

    @Override
    public void tick() {
    }

    public void setSafeDirection(int value) {
        if (value == 0) this.safeDirection = TardisSystemMaterializationSafeDirection.TOP;
        else if (value == 1) this.safeDirection = TardisSystemMaterializationSafeDirection.BOTTOM;
        else if (value == 2) this.safeDirection = TardisSystemMaterializationSafeDirection.NONE;
    }

    public boolean setupMaterializationState(boolean flag) {
        if (this.tardisData.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
            if (flightSystem.isInFligth()) return false;
        }

        if (flag) return this.remat();
        return this.demat();
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

        BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
        BlockPos safePosition = this.checkBlockIsSafe(exteriorLevel, exteriorBlockPos) ? exteriorBlockPos : null;

        if (this.safeDirection == TardisSystemMaterializationSafeDirection.TOP) {
            if (safePosition == null) safePosition = this.getSafePosition(exteriorLevel, exteriorBlockPos, TardisSystemMaterializationSafeDirection.TOP);
            if (safePosition == null) safePosition = this.getSafePosition(exteriorLevel, exteriorBlockPos, TardisSystemMaterializationSafeDirection.BOTTOM);
        }
        else if (this.safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) {
            if (safePosition == null) safePosition = this.getSafePosition(exteriorLevel, exteriorBlockPos, TardisSystemMaterializationSafeDirection.BOTTOM);
            if (safePosition == null) safePosition = this.getSafePosition(exteriorLevel, exteriorBlockPos, TardisSystemMaterializationSafeDirection.TOP);
        }

        if (safePosition != null) {
            this.tardisData.setPosition(safePosition, false);
            this.tardisData.setDestinationPosition(safePosition);
            return true;
        }

        return false;
    }

    private BlockPos getSafePosition(ServerLevel exteriorLevel, BlockPos exteriorBlockPos, TardisSystemMaterializationSafeDirection safeDirection) {
        if (safeDirection == TardisSystemMaterializationSafeDirection.TOP) exteriorBlockPos = exteriorBlockPos.below();
        else if (safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) exteriorBlockPos = exteriorBlockPos.above();
        else return null;

        boolean freeSpaceFound = false;
        do {
            if (safeDirection == TardisSystemMaterializationSafeDirection.TOP) exteriorBlockPos = exteriorBlockPos.above();
            else if (safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) exteriorBlockPos = exteriorBlockPos.below();
            freeSpaceFound = this.checkBlockIsSafe(exteriorLevel, exteriorBlockPos);
        } while (!freeSpaceFound && !this.checkBlockIsReachedMaxHeight(exteriorLevel, exteriorBlockPos, safeDirection));

        return freeSpaceFound ? exteriorBlockPos : null;
    }

    private boolean checkBlockIsReachedMaxHeight(Level level, BlockPos blockPos, TardisSystemMaterializationSafeDirection safeDirection) {
        if (safeDirection == TardisSystemMaterializationSafeDirection.TOP) return blockPos.getY() >= level.getMaxBuildHeight() - 1;
        else if (safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) return blockPos.getY() < level.getMinBuildHeight();

        return false;
    }

    private boolean checkBlockIsSafe(Level level, BlockPos blockPos) {
        BlockPos frontBlockPos = blockPos.relative(this.tardisData.getCurrentExteriorFacing());

        boolean isBottomSolid = this.checkBlockIsSolid(level.getBlockState(blockPos.below()));
        boolean isEmpty = this.checkBlockIsEmpty(level.getBlockState(blockPos));
        boolean isUpEmpty = this.checkBlockIsEmpty(level.getBlockState(blockPos.above()));

        boolean isFrontBottomSolid = this.checkBlockIsSolid(level.getBlockState(frontBlockPos.below()));
        boolean isFrontEmpty = this.checkBlockIsEmpty(level.getBlockState(frontBlockPos));
        boolean isFrontUpEmpty = this.checkBlockIsEmpty(level.getBlockState(frontBlockPos.above()));

        return isBottomSolid && isEmpty && isUpEmpty && isFrontBottomSolid && isFrontEmpty && isFrontUpEmpty;
    }

    private boolean checkBlockIsEmpty(BlockState blockState) {
        return blockState.getBlock() instanceof AirBlock || blockState.getFluidState().is(FluidTags.WATER);
    }

    private boolean checkBlockIsSolid(BlockState blockState) {
        return !this.checkBlockIsEmpty(blockState) && blockState.getFluidState().isEmpty();
    }
}

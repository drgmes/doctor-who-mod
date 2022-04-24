package net.drgmes.dwm.common.tardis.systems;

import java.util.ArrayList;
import java.util.List;

import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.network.ClientboundTardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.DWMUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AirBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TardisSystemMaterialization implements ITardisSystem {
    public static enum TardisSystemMaterializationSafeDirection {
        TOP,
        BOTTOM,
        NONE
    }

    private final List<Runnable> dematConsumers = new ArrayList<>();
    private final List<Runnable> rematConsumers = new ArrayList<>();

    private final ITardisLevelData tardisData;
    private boolean isMaterialized = true;

    public int dematTickInProgress = 0;
    public int rematTickInProgress = 0;
    public int dematTickInProgressGoal = 0;
    public int rematTickInProgressGoal = 0;
    public TardisSystemMaterializationSafeDirection safeDirection;

    public TardisSystemMaterialization(ITardisLevelData tardisData) {
        this.tardisData = tardisData;
        this.safeDirection = TardisSystemMaterializationSafeDirection.TOP;
    }

    @Override
    public void load(CompoundTag tag) {
        this.isMaterialized = tag.getBoolean("isMaterialized");
        this.dematTickInProgress = tag.getInt("dematTickInProgress");
        this.rematTickInProgress = tag.getInt("rematTickInProgress");
        this.dematTickInProgressGoal = tag.getInt("dematTickInProgressGoal");
        this.rematTickInProgressGoal = tag.getInt("rematTickInProgressGoal");
        this.safeDirection = TardisSystemMaterializationSafeDirection.valueOf(tag.getString("safeDirection"));
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("isMaterialized", this.isMaterialized);
        tag.putInt("dematTickInProgress", this.dematTickInProgress);
        tag.putInt("rematTickInProgress", this.rematTickInProgress);
        tag.putInt("dematTickInProgressGoal", this.dematTickInProgressGoal);
        tag.putInt("rematTickInProgressGoal", this.rematTickInProgressGoal);
        tag.putString("safeDirection", this.safeDirection.name());

        return tag;
    }

    @Override
    public void tick() {
        if (this.inProgress()) {
            if (this.inDematProgress()) {
                this.dematTickInProgress--;
                if (!this.inDematProgress()) this.demat();
            }
            else if (this.inRematProgress()) {
                this.rematTickInProgress--;
            }

            this.tardisData.updateConsoleTiles();
            this.sendExteriorUpdatePacket(
                this.dematTickInProgress == this.dematTickInProgressGoal - 1,
                this.rematTickInProgress == this.rematTickInProgressGoal - 1
            );
        }
    }

    public int getProgressPercent() {
        if (this.dematTickInProgress > 0) return (int) Math.ceil(this.dematTickInProgress / (float) this.dematTickInProgressGoal * 100);
        if (this.rematTickInProgress > 0) return (int) Math.ceil((this.rematTickInProgressGoal - this.rematTickInProgress) / (float) this.rematTickInProgressGoal * 100);
        return this.isMaterialized ? 100 : 0;
    }

    public boolean inProgress() {
        return this.inDematProgress() || this.inRematProgress();
    }

    public boolean inDematProgress() {
        return this.dematTickInProgress > 0;
    }

    public boolean inRematProgress() {
        return this.rematTickInProgress > 0;
    }

    public boolean isMaterialized() {
        return !this.inProgress() && this.isMaterialized;
    }

    public void setSafeDirection(int value) {
        if (value == 0) this.safeDirection = TardisSystemMaterializationSafeDirection.TOP;
        else if (value == 1) this.safeDirection = TardisSystemMaterializationSafeDirection.BOTTOM;
        else if (value == 2) this.safeDirection = TardisSystemMaterializationSafeDirection.NONE;
    }

    public boolean setMaterializationState(boolean flag) {
        if (this.tardisData.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
            if (flightSystem.inProgress()) return false;
        }

        if (flag) return this.remat();
        return this.demat();
    }

    public boolean demat() {
        if (this.inProgress()) return false;

        if (!this.isMaterialized) {
            this.runDematConsumers();
            return true;
        }

        if (this.dematTickInProgressGoal == 0) {
            this.dematTickInProgressGoal = 240;
            this.dematTickInProgress = this.dematTickInProgressGoal;
            this.tardisData.setDoorsState(false, true);
            this.playTakeoffSound();
            return false;
        }

        ServerLevel level = this.tardisData.getLevel();
        if (level != null) {
            ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
            if (exteriorLevel == null) return false;

            this.isMaterialized = false;
            this.dematTickInProgressGoal = 0;
            this.tardisData.updateConsoleTiles();

            DWMUtils.runInThread("materialization", () -> {
                BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
                BlockState exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);
                if (!Thread.currentThread().isAlive() || Thread.currentThread().isInterrupted()) return;

                if (exteriorBlockState.getBlock() instanceof TardisExteriorBlock) {
                    exteriorLevel.removeBlock(exteriorBlockPos.above(), true);
                    exteriorLevel.removeBlock(exteriorBlockPos, true);
                }
            });

            this.runDematConsumers();
            return true;
        }

        return false;
    }

    public boolean demat(Runnable consumer) {
        this.dematConsumers.add(consumer);
        return this.demat();
    }

    public boolean remat() {
        if (this.inProgress()) return false;

        if (this.isMaterialized) {
            this.runRematConsumers();
            return true;
        }

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
                    this.rematTickInProgressGoal = 180;
                    this.rematTickInProgress = this.rematTickInProgressGoal;
                    this.playLandingSound();
                    return true;
                }

                this.demat();
            }

            this.playErrorSound();
        }

        return false;
    }

    public boolean remat(Runnable consumer) {
        this.rematConsumers.add(consumer);
        return this.remat();
    }

    private void runDematConsumers() {
        this.dematConsumers.forEach((consumer) -> consumer.run());
        this.dematConsumers.clear();
    }

    private void runRematConsumers() {
        this.rematConsumers.forEach((consumer) -> consumer.run());
        this.rematConsumers.clear();
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

    private void sendExteriorUpdatePacket(boolean dematSound, boolean rematSound) {
        ServerLevel level = this.tardisData.getLevel();
        if (level == null) return;

        BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
        ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
        if (exteriorLevel == null) return;

        ModPackets.send(exteriorLevel.getChunkAt(exteriorBlockPos), new ClientboundTardisExteriorUpdatePacket(
            exteriorBlockPos,
            this.getProgressPercent(),
            this.tardisData.isDoorsOpened(),
            dematSound,
            rematSound
        ));
    }

    private void playSound(SoundEvent soundEvent) {
        BlockPos consoleTileBlockPos = this.tardisData.getMainConsoleTile().getBlockPos();
        this.tardisData.getLevel().playSound(null, consoleTileBlockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void playErrorSound() {
        this.playSound(ModSounds.TARDIS_ERROR.get());
    }

    private void playTakeoffSound() {
        this.playSound(ModSounds.TARDIS_TAKEOFF.get());
    }

    private void playLandingSound() {
        this.playSound(ModSounds.TARDIS_LAND.get());
    }
}

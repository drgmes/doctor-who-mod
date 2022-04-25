package net.drgmes.dwm.common.tardis.systems;

import java.util.ArrayList;
import java.util.List;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.network.ClientboundTardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCapabilities;
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

    public float dematTickInProgress = 0;
    public float rematTickInProgress = 0;
    public float dematTickInProgressGoal = 0;
    public float rematTickInProgressGoal = 0;
    public TardisSystemMaterializationSafeDirection safeDirection;

    public TardisSystemMaterialization(ITardisLevelData tardisData) {
        this.tardisData = tardisData;
        this.safeDirection = TardisSystemMaterializationSafeDirection.TOP;
    }

    @Override
    public void load(CompoundTag tag) {
        this.isMaterialized = tag.getBoolean("isMaterialized");
        this.dematTickInProgress = tag.getFloat("dematTickInProgress");
        this.rematTickInProgress = tag.getFloat("rematTickInProgress");
        this.dematTickInProgressGoal = tag.getFloat("dematTickInProgressGoal");
        this.rematTickInProgressGoal = tag.getFloat("rematTickInProgressGoal");
        this.safeDirection = TardisSystemMaterializationSafeDirection.valueOf(tag.getString("safeDirection"));
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putBoolean("isMaterialized", this.isMaterialized);
        tag.putFloat("dematTickInProgress", this.dematTickInProgress);
        tag.putFloat("rematTickInProgress", this.rematTickInProgress);
        tag.putFloat("dematTickInProgressGoal", this.dematTickInProgressGoal);
        tag.putFloat("rematTickInProgressGoal", this.rematTickInProgressGoal);
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
                if (!this.inRematProgress()) this.remat();
            }

            this.tardisData.updateConsoleTiles();
        }
    }

    public int getProgressPercent() {
        if (this.dematTickInProgress > 0) return (int) Math.ceil(this.dematTickInProgress / this.dematTickInProgressGoal * 100);
        if (this.rematTickInProgress > 0) return (int) Math.ceil((this.rematTickInProgressGoal - this.rematTickInProgress) / this.rematTickInProgressGoal * 100);
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
            this.dematTickInProgressGoal = DWM.TIMINGS.DEMAT;
            this.dematTickInProgress = this.dematTickInProgressGoal;
            this.tardisData.setLightState(false, true);
            this.tardisData.setDoorsState(false, true);
            this.sendExteriorUpdatePacket(true, false);
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
            this.sendExteriorUpdatePacket(false, false);
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
                    this.rematTickInProgressGoal = DWM.TIMINGS.REMAT;
                    this.rematTickInProgress = this.rematTickInProgressGoal;
                    this.sendExteriorUpdatePacket(false, true);
                    this.playLandingSound();
                    return true;
                }
                else {
                    this.playErrorSound();
                    this.demat();
                }
            }
            else if (!this.tryLandToForeignTardis()) {
                this.playErrorSound();
            }
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

    private boolean tryLandToForeignTardis() {
        if (this.safeDirection != TardisSystemMaterializationSafeDirection.NONE) return false;

        ServerLevel level = this.tardisData.getLevel();
        if (level == null) return false;

        ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
        if (exteriorLevel == null) return false;

        if (exteriorLevel.getBlockEntity(this.tardisData.getCurrentExteriorPosition()) instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
            ServerLevel foreignTardisLevel = tardisExteriorBlockEntity.getTardisDimension(exteriorLevel);
            if (foreignTardisLevel != null) {
                foreignTardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
                    if (levelProvider.isValid() && !levelProvider.isShieldsEnabled()) {
                        this.tardisData.setDimension(levelProvider.getLevel().dimension(), false);
                        this.tardisData.setFacing(levelProvider.getEntraceFacing(), false);
                        this.tardisData.setPosition(levelProvider.getEntracePosition().relative(levelProvider.getEntraceFacing()), false);
                        this.remat();
                    } else {
                        this.playErrorSound();
                    }
                });

                return true;
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
        return (
            blockState.isAir()
            || blockState.getFluidState().is(FluidTags.WATER)
            || (blockState.getMaterial().isReplaceable() && blockState.getFluidState().isEmpty())
        );
    }

    private boolean checkBlockIsSolid(BlockState blockState) {
        return !this.checkBlockIsEmpty(blockState) && blockState.getFluidState().isEmpty();
    }

    private void sendExteriorUpdatePacket(boolean demat, boolean remat) {
        ServerLevel level = this.tardisData.getLevel();
        if (level == null) return;

        BlockPos exteriorBlockPos = this.tardisData.getCurrentExteriorPosition();
        ServerLevel exteriorLevel = level.getServer().getLevel(this.tardisData.getCurrentExteriorDimension());
        if (exteriorLevel == null) return;

        if (exteriorLevel.getBlockEntity(exteriorBlockPos) instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (demat) tardisExteriorBlockEntity.demat();
            else if (remat) tardisExteriorBlockEntity.remat();
            else if (!this.inProgress()) tardisExteriorBlockEntity.resetMaterializationState(this.isMaterialized);
        }

        ModPackets.send(exteriorLevel.getChunkAt(exteriorBlockPos), new ClientboundTardisExteriorUpdatePacket(
            exteriorBlockPos,
            this.tardisData.isLightEnabled(),
            this.tardisData.isDoorsOpened(),
            demat,
            remat
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

package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.network.ClientboundTardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.DWMUtils;
import net.drgmes.dwm.utils.helpers.TardisHelper.TardisTeleporter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;

public class TardisSystemMaterialization implements ITardisSystem {
    public enum TardisSystemMaterializationSafeDirection {
        TOP,
        BOTTOM,
        NONE
    }

    public TardisSystemMaterializationSafeDirection safeDirection;
    public float dematTickInProgress = 0;
    public float rematTickInProgress = 0;
    public float dematTickInProgressGoal = 0;
    public float rematTickInProgressGoal = 0;

    private final ITardisLevelData tardis;
    private final List<Runnable> dematConsumers = new ArrayList<>();
    private final List<Runnable> rematConsumers = new ArrayList<>();
    private final List<Runnable> failConsumers = new ArrayList<>();

    private boolean isMaterialized = true;
    private boolean needsRerunRemat = false;

    public TardisSystemMaterialization(ITardisLevelData tardis) {
        this.tardis = tardis;
        this.safeDirection = TardisSystemMaterializationSafeDirection.TOP;
    }

    @Override
    public boolean isEnabled() {
        return this.tardis.isSystemEnabled(this.getClass());
    }

    @Override
    public boolean inProgress() {
        return this.inDematProgress() || this.inRematProgress();
    }

    @Override
    public void load(CompoundTag tag) {
        this.isMaterialized = tag.getBoolean("isMaterialized");
        this.needsRerunRemat = tag.getBoolean("needsRerunRemat");
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
        tag.putBoolean("needsRerunRemat", this.needsRerunRemat);
        tag.putFloat("dematTickInProgress", this.dematTickInProgress);
        tag.putFloat("rematTickInProgress", this.rematTickInProgress);
        tag.putFloat("dematTickInProgressGoal", this.dematTickInProgressGoal);
        tag.putFloat("rematTickInProgressGoal", this.rematTickInProgressGoal);
        tag.putString("safeDirection", this.safeDirection.name());

        return tag;
    }

    @Override
    public void tick() {
        if (this.needsRerunRemat) {
            this.remat();
            this.needsRerunRemat = false;
        }

        if (this.inProgress()) {
            if (this.inDematProgress()) {
                this.dematTickInProgress--;
                if (!this.inDematProgress()) this.demat();
                else if (this.dematTickInProgress % 3 == 0) this.tardis.updateConsoleTiles();
            }
            else if (this.inRematProgress()) {
                this.rematTickInProgress--;
                if (!this.inRematProgress()) this.remat();
                else if (this.rematTickInProgress % 3 == 0) this.tardis.updateConsoleTiles();
            }
        }
    }

    public int getProgressPercent() {
        if (this.dematTickInProgress > 0) return (int) Math.ceil(this.dematTickInProgress / this.dematTickInProgressGoal * 100);
        if (this.rematTickInProgress > 0) return (int) Math.ceil((this.rematTickInProgressGoal - this.rematTickInProgress) / this.rematTickInProgressGoal * 100);
        return this.isMaterialized ? 100 : 0;
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

    public void setSafeDirection(TardisSystemMaterializationSafeDirection value) {
        this.safeDirection = value;
    }

    public void setSafeDirection(int value) {
        if (value == 0) this.safeDirection = TardisSystemMaterializationSafeDirection.TOP;
        else if (value == 1) this.safeDirection = TardisSystemMaterializationSafeDirection.BOTTOM;
        else if (value == 2) this.safeDirection = TardisSystemMaterializationSafeDirection.NONE;
    }

    public boolean setMaterializationState(boolean flag) {
        if (this.tardis.getSystem(TardisSystemFlight.class).inProgress()) return false;
        if (flag) return this.remat();
        return this.demat();
    }

    public boolean demat() {
        if (!this.isEnabled()) return false;
        if (this.inProgress()) return false;

        if (!this.isMaterialized) {
            this.runDematConsumers();
            return true;
        }

        if (this.dematTickInProgressGoal == 0) {
            this.dematTickInProgressGoal = DWM.TIMINGS.DEMAT;
            this.dematTickInProgress = this.dematTickInProgressGoal;

            this.tardis.setDoorsOpenState(false);
            this.tardis.setLightState(false);
            this.tardis.setShieldsState(false);
            this.tardis.setEnergyArtronHarvesting(false);
            this.tardis.setEnergyForgeHarvesting(false);
            this.tardis.updateConsoleTiles();

            this.updateExterior(true, false);
            ModSounds.playTardisTakeoffSound(this.tardis.getLevel(), this.tardis.getCorePosition());
            return false;
        }

        ServerLevel level = this.tardis.getLevel();
        if (level != null) {
            ServerLevel exteriorLevel = level.getServer().getLevel(this.tardis.getCurrentExteriorDimension());
            if (exteriorLevel == null) return false;

            this.isMaterialized = false;
            this.dematTickInProgressGoal = 0;
            this.tardis.updateConsoleTiles();

            DWMUtils.runInThread("materialization", () -> {
                BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
                BlockState exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);
                if (!Thread.currentThread().isAlive() || Thread.currentThread().isInterrupted()) return;

                if (exteriorBlockState.getBlock() instanceof BaseTardisExteriorBlock) {
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
        if (!this.isEnabled()) return false;
        if (this.inProgress()) return false;

        if (!this.tardis.isValid()) {
            this.runFailConsumers();
            ModSounds.playTardisFailSound(this.tardis.getLevel(), this.tardis.getCorePosition());
            return false;
        }

        if (this.isMaterialized) {
            this.runRematConsumers();
            return true;
        }

        ServerLevel level = this.tardis.getLevel();
        if (level != null) {
            ServerLevel exteriorLevel = level.getServer().getLevel(this.tardis.getCurrentExteriorDimension());
            if (exteriorLevel == null) return false;

            BlockPos initialExteriorBlockPos = this.tardis.getCurrentExteriorPosition();
            if (!this.needsRerunRemat) {
                this.addChunkToLoader(exteriorLevel, initialExteriorBlockPos);
                this.needsRerunRemat = true;
                return false;
            }

            if (this.findSafePosition()) {
                BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
                BlockState exteriorBlockState = exteriorLevel.getBlockState(exteriorBlockPos);
                BlockState exteriorUpBlockState = exteriorLevel.getBlockState(exteriorBlockPos.above());

                BlockState tardisExteriorBlockState = ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.get().defaultBlockState();
                tardisExteriorBlockState = tardisExteriorBlockState.setValue(BaseTardisExteriorBlock.FACING, this.tardis.getCurrentExteriorFacing());
                tardisExteriorBlockState = tardisExteriorBlockState.setValue(BaseTardisExteriorBlock.WATERLOGGED, exteriorBlockState.getFluidState().is(FluidTags.WATER));
                exteriorLevel.setBlock(exteriorBlockPos, tardisExteriorBlockState, 3);

                BlockState tardisExteriorUpBlockState = tardisExteriorBlockState.setValue(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
                tardisExteriorUpBlockState = tardisExteriorUpBlockState.setValue(BaseTardisExteriorBlock.WATERLOGGED, exteriorUpBlockState.getFluidState().is(FluidTags.WATER));
                exteriorLevel.setBlock(exteriorBlockPos.above(), tardisExteriorUpBlockState, 3);

                if (exteriorLevel.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
                    tardisExteriorBlockEntity.tardisLevelUUID = level.dimension().location().getPath();

                    this.isMaterialized = true;
                    this.needsRerunRemat = false;
                    this.rematTickInProgressGoal = DWM.TIMINGS.REMAT;
                    this.rematTickInProgress = this.rematTickInProgressGoal;
                    this.removeChunkFromLoader(exteriorLevel, initialExteriorBlockPos);
                    this.updateExterior(false, true);
                    ModSounds.playTardisLandingSound(this.tardis.getLevel(), this.tardis.getCorePosition());

                    this.rematConsumers.add(() -> {
                        AABB aabb = AABB.ofSize(Vec3.atBottomCenterOf(exteriorBlockPos), 0.5, 1, 0.5);
                        BlockPos entrancePosition = this.tardis.getEntrancePosition().relative(this.tardis.getEntranceFacing());
                        List<Entity> entities = exteriorLevel.getEntitiesOfClass(Entity.class, aabb);

                        for (Entity entity : entities) {
                            entity.changeDimension(this.tardis.getLevel(), new TardisTeleporter(entrancePosition));
                        }

                        this.tardis.updateConsoleTiles();
                        this.updateExterior(false, false);
                    });

                    return true;
                }
                else {
                    ModSounds.playTardisFailSound(this.tardis.getLevel(), this.tardis.getCorePosition());
                    this.demat();
                }
            }
            else if (!this.tryLandToForeignTardis()) {
                this.runFailConsumers();
                ModSounds.playTardisFailSound(this.tardis.getLevel(), this.tardis.getCorePosition());
            }

            this.removeChunkFromLoader(exteriorLevel, initialExteriorBlockPos);
        }

        return false;
    }

    public boolean remat(Runnable consumer) {
        this.rematConsumers.add(consumer);
        return this.remat();
    }

    public void onFail(Runnable consumer) {
        this.failConsumers.add(consumer);
    }

    private void runDematConsumers() {
        this.dematConsumers.forEach(Runnable::run);
        this.dematConsumers.clear();
    }

    private void runRematConsumers() {
        this.rematConsumers.forEach(Runnable::run);
        this.rematConsumers.clear();
    }

    private void runFailConsumers() {
        this.failConsumers.forEach(Runnable::run);
        this.failConsumers.clear();
    }

    private boolean tryLandToForeignTardis() {
        if (this.safeDirection != TardisSystemMaterializationSafeDirection.NONE) return false;

        ServerLevel level = this.tardis.getLevel();
        if (level == null) return false;

        ServerLevel exteriorLevel = level.getServer().getLevel(this.tardis.getCurrentExteriorDimension());
        if (exteriorLevel == null) return false;

        if (exteriorLevel.getBlockEntity(this.tardis.getCurrentExteriorPosition()) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            ServerLevel foreignTardisLevel = tardisExteriorBlockEntity.getTardisLevel(exteriorLevel);
            if (foreignTardisLevel != null) {
                foreignTardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                    if (tardis.isValid() && !tardis.getSystem(TardisSystemShields.class).inProgress()) {
                        this.tardis.setDimension(tardis.getLevel().dimension(), false);
                        this.tardis.setFacing(tardis.getEntranceFacing(), false);
                        this.tardis.setPosition(tardis.getEntrancePosition().relative(tardis.getEntranceFacing()), false);
                        this.remat();
                    }
                    else {
                        ModSounds.playTardisFailSound(this.tardis.getLevel(), this.tardis.getCorePosition());
                    }
                });

                return true;
            }
        }

        return false;
    }

    private void addChunkToLoader(ServerLevel level, BlockPos blockPos) {
        level.getCapability(ModCapabilities.TARDIS_CHUNK_LOADER).ifPresent((levelProvider) -> {
            SectionPos pos = SectionPos.of(blockPos);
            int radius = 1;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius && Math.abs(z) == radius) continue;
                    levelProvider.add(pos.offset(x, 0, z), blockPos);
                }
            }
        });
    }

    private void removeChunkFromLoader(ServerLevel level, BlockPos blockPos) {
        level.getCapability(ModCapabilities.TARDIS_CHUNK_LOADER).ifPresent((levelProvider) -> {
            SectionPos pos = SectionPos.of(blockPos);
            int radius = 1;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    if (Math.abs(x) == radius && Math.abs(z) == radius) continue;
                    levelProvider.remove(pos.offset(x, 0, z), blockPos);
                }
            }
        });
    }

    private boolean findSafePosition() {
        ServerLevel level = this.tardis.getLevel();
        if (level == null) return false;

        ServerLevel exteriorLevel = level.getServer().getLevel(this.tardis.getCurrentExteriorDimension());
        if (exteriorLevel == null) return false;

        Direction exteriorFacing = this.tardis.getCurrentExteriorFacing();
        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        BlockPos safePosition = null;

        if (this.safeDirection == TardisSystemMaterializationSafeDirection.TOP) {
            safePosition = this.getSafePosition(exteriorLevel, exteriorBlockPos, exteriorFacing, TardisSystemMaterializationSafeDirection.TOP);
            if (safePosition == null) safePosition = this.getSafePosition(exteriorLevel, exteriorBlockPos, exteriorFacing, TardisSystemMaterializationSafeDirection.BOTTOM);
        }
        else if (this.safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) {
            safePosition = this.getSafePosition(exteriorLevel, exteriorBlockPos, exteriorFacing, TardisSystemMaterializationSafeDirection.BOTTOM);
            if (safePosition == null) safePosition = this.getSafePosition(exteriorLevel, exteriorBlockPos, exteriorFacing, TardisSystemMaterializationSafeDirection.TOP);
        }

        if (safePosition != null) {
            this.tardis.setPosition(safePosition, false);
            this.tardis.setDestinationPosition(safePosition);
            return true;
        }

        return false;
    }

    private BlockPos getSafePosition(ServerLevel exteriorLevel, BlockPos exteriorBlockPos, Direction exteriorFacing, TardisSystemMaterializationSafeDirection safeDirection) {
        if (safeDirection == TardisSystemMaterializationSafeDirection.TOP) exteriorBlockPos = exteriorBlockPos.below();
        else if (safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) exteriorBlockPos = exteriorBlockPos.above();
        else return null;

        boolean freeSpaceFound;
        do {
            exteriorBlockPos = safeDirection == TardisSystemMaterializationSafeDirection.TOP ? exteriorBlockPos.above() : exteriorBlockPos.below();
            freeSpaceFound = this.checkBlockIsSafe(exteriorLevel, exteriorBlockPos, exteriorFacing);

            if (!freeSpaceFound) {
                for (Direction direction : Direction.values()) {
                    if (direction == exteriorFacing) continue;
                    freeSpaceFound = this.checkBlockIsSafe(exteriorLevel, exteriorBlockPos, direction);

                    if (freeSpaceFound) {
                        this.tardis.setFacing(direction, false);
                        this.tardis.setDestinationFacing(direction);
                        break;
                    }
                }
            }
        } while (!freeSpaceFound && !this.checkBlockIsReachedMaxHeight(exteriorLevel, exteriorBlockPos, safeDirection));

        return freeSpaceFound ? exteriorBlockPos : null;
    }

    private boolean checkBlockIsReachedMaxHeight(Level level, BlockPos blockPos, TardisSystemMaterializationSafeDirection safeDirection) {
        if (safeDirection == TardisSystemMaterializationSafeDirection.TOP) return blockPos.getY() >= level.getMaxBuildHeight() - 1;
        else if (safeDirection == TardisSystemMaterializationSafeDirection.BOTTOM) return blockPos.getY() < level.getMinBuildHeight();
        return false;
    }

    private boolean checkBlockIsSafe(Level level, BlockPos blockPos, Direction direction) {
        BlockPos frontBlockPos = blockPos.relative(direction);

        boolean isEmpty = DWMUtils.checkBlockIsEmpty(level.getBlockState(blockPos));
        boolean isUpEmpty = DWMUtils.checkBlockIsEmpty(level.getBlockState(blockPos.above()));
        boolean isBottomSolid = DWMUtils.checkBlockIsSolid(level.getBlockState(blockPos.below()));

        boolean isFrontEmpty = DWMUtils.checkBlockIsEmpty(level.getBlockState(frontBlockPos));
        boolean isFrontUpEmpty = DWMUtils.checkBlockIsEmpty(level.getBlockState(frontBlockPos.above()));
        boolean isFrontBottomSolid = DWMUtils.checkBlockIsSolid(level.getBlockState(frontBlockPos.below()));

        return isBottomSolid && isEmpty && isUpEmpty && isFrontBottomSolid && isFrontEmpty && isFrontUpEmpty;
    }

    private void updateExterior(boolean demat, boolean remat) {
        ServerLevel level = this.tardis.getLevel();
        if (level == null) return;

        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        ServerLevel exteriorLevel = level.getServer().getLevel(this.tardis.getCurrentExteriorDimension());
        if (exteriorLevel == null) return;

        if (exteriorLevel.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (demat) tardisExteriorBlockEntity.demat();
            else if (remat) tardisExteriorBlockEntity.remat();
            else if (!this.inProgress()) tardisExteriorBlockEntity.resetMaterializationState(this.isMaterialized);
        }

        if (!demat) return;
        ModPackets.send(exteriorLevel, new ClientboundTardisExteriorUpdatePacket(exteriorBlockPos, this.tardis.isDoorsOpened(), true));
    }
}

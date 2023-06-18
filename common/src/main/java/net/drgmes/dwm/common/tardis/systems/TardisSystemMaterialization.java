package net.drgmes.dwm.common.tardis.systems;

import com.google.common.collect.ImmutableSet;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.network.client.TardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TardisSystemMaterialization implements ITardisSystem {
    public enum ESafeDirection {
        TOP,
        BOTTOM,
        DIRECT,
        NONE
    }

    public ESafeDirection safeDirection;
    public float dematTickInProgress = 0;
    public float rematTickInProgress = 0;
    public float dematTickInProgressGoal = 0;
    public float rematTickInProgressGoal = 0;

    private final TardisStateManager tardis;
    private final List<Runnable> dematConsumers = new ArrayList<>();
    private final List<Runnable> rematConsumers = new ArrayList<>();
    private final List<Runnable> failConsumers = new ArrayList<>();

    private boolean isMaterialized = true;
    private boolean runDematConsumers = false;
    private boolean runRematConsumers = false;
    private boolean runFailConsumers = false;
    private boolean tryPlaceTardisExterior = false;

    public TardisSystemMaterialization(TardisStateManager tardis) {
        this.tardis = tardis;
        this.safeDirection = ESafeDirection.TOP;
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
    public void load(NbtCompound tag) {
        this.dematTickInProgress = tag.getFloat("dematTickInProgress");
        this.rematTickInProgress = tag.getFloat("rematTickInProgress");
        this.dematTickInProgressGoal = tag.getFloat("dematTickInProgressGoal");
        this.rematTickInProgressGoal = tag.getFloat("rematTickInProgressGoal");
        this.safeDirection = ESafeDirection.valueOf(tag.getString("safeDirection"));

        this.isMaterialized = tag.getBoolean("isMaterialized");
        this.runDematConsumers = tag.getBoolean("runDematConsumers");
        this.runRematConsumers = tag.getBoolean("runRematConsumers");
        this.runFailConsumers = tag.getBoolean("runFailConsumers");
        this.tryPlaceTardisExterior = tag.getBoolean("tryPlaceTardisExterior");
    }

    @Override
    public NbtCompound save() {
        NbtCompound tag = new NbtCompound();

        tag.putFloat("dematTickInProgress", this.dematTickInProgress);
        tag.putFloat("rematTickInProgress", this.rematTickInProgress);
        tag.putFloat("dematTickInProgressGoal", this.dematTickInProgressGoal);
        tag.putFloat("rematTickInProgressGoal", this.rematTickInProgressGoal);
        tag.putString("safeDirection", this.safeDirection.name());

        tag.putBoolean("isMaterialized", this.isMaterialized);
        tag.putBoolean("runDematConsumers", this.runDematConsumers);
        tag.putBoolean("runRematConsumers", this.runRematConsumers);
        tag.putBoolean("runFailConsumers", this.runFailConsumers);
        tag.putBoolean("tryPlaceTardisExterior", this.tryPlaceTardisExterior);

        return tag;
    }

    @Override
    public void tick() {
        if (this.runDematConsumers) this.runDematConsumers();
        if (this.runRematConsumers) this.runRematConsumers();
        if (this.runFailConsumers) this.runFailConsumers();
        if (this.tryPlaceTardisExterior) this.tryPlaceTardisExterior();

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

    public void setSafeDirection(ESafeDirection value) {
        this.safeDirection = value;
    }

    public void setSafeDirection(int value) {
        if (value == 0) this.safeDirection = ESafeDirection.TOP;
        else if (value == 1) this.safeDirection = ESafeDirection.BOTTOM;
        else if (value == 2) this.safeDirection = ESafeDirection.DIRECT;
        else if (value == 3) this.safeDirection = ESafeDirection.NONE;
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

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension(), this.tardis.getWorld().getServer());
        if (exteriorWorld == null) return false;

        if (this.dematTickInProgressGoal == 0) {
            this.dematTickInProgressGoal = DWM.TIMINGS.DEMAT;
            this.dematTickInProgress = this.dematTickInProgressGoal;

            this.tardis.setDoorsOpenState(false);
            this.tardis.setLightState(false);
            this.tardis.setShieldsState(false);
            this.tardis.setFuelHarvesting(false);
            this.tardis.setEnergyHarvesting(false);
            this.tardis.updateConsoleTiles();

            this.updateExterior(exteriorWorld, true, false);
            ModSounds.playTardisTakeoffSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        this.isMaterialized = false;
        this.dematTickInProgressGoal = 0;
        this.tardis.updateConsoleTiles();

        CommonHelper.runInThread("dematerialization", () -> {
            BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
            BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);
            if (!Thread.currentThread().isAlive() || Thread.currentThread().isInterrupted()) return;

            if (exteriorBlockState.getBlock() instanceof BaseTardisExteriorBlock<?>) {
                exteriorWorld.removeBlock(exteriorBlockPos.up(), false);
                exteriorWorld.removeBlock(exteriorBlockPos, false);
                this.runDematConsumers = true;
            }
            else {
                this.setupDeferredFail();
            }
        });

        return true;
    }

    public boolean demat(Runnable consumer) {
        this.dematConsumers.add(consumer);
        return this.demat();
    }

    public boolean remat() {
        if (!this.isEnabled()) return false;
        if (this.inProgress()) return false;
        if (!this.tardis.isValid()) return this.setupFail();

        if (this.isMaterialized) {
            this.runRematConsumers();
            return true;
        }

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension(), this.tardis.getWorld().getServer());
        if (exteriorWorld == null) return false;

        BlockPos initialExteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        if (!exteriorWorld.isInBuildLimit(initialExteriorBlockPos)) {
            if (this.safeDirection == ESafeDirection.TOP) initialExteriorBlockPos = initialExteriorBlockPos.withY(exteriorWorld.getTopY() - 2);
            else if (this.safeDirection == ESafeDirection.BOTTOM) initialExteriorBlockPos = initialExteriorBlockPos.withY(exteriorWorld.getBottomY());

            this.tardis.setPosition(initialExteriorBlockPos, false);
            this.tardis.setDestinationPosition(initialExteriorBlockPos);
        }

        if ((this.safeDirection == ESafeDirection.DIRECT || this.safeDirection == ESafeDirection.NONE) && this.tryLandToForeignTardis(exteriorWorld)) {
            return true;
        }

        CommonHelper.runInThread("materialization", () -> {
            if (this.findSafePosition(exteriorWorld)) {
                if (!Thread.currentThread().isAlive() || Thread.currentThread().isInterrupted()) return;
                this.tryPlaceTardisExterior = true;
            }
            else {
                this.setupDeferredFail();
            }
        });

        return true;
    }

    public boolean remat(Runnable consumer) {
        this.rematConsumers.add(consumer);
        return this.remat();
    }

    public void onFail(Runnable consumer) {
        this.failConsumers.add(consumer);
    }

    private void runDematConsumers() {
        this.runDematConsumers = false;
        this.dematConsumers.forEach(Runnable::run);
        this.dematConsumers.clear();
    }

    private void runRematConsumers() {
        this.runRematConsumers = false;
        this.rematConsumers.forEach(Runnable::run);
        this.rematConsumers.clear();
    }

    private void runFailConsumers() {
        this.runFailConsumers = false;
        this.failConsumers.forEach(Runnable::run);
        this.failConsumers.clear();
    }

    private void tryPlaceTardisExterior() {
        this.tryPlaceTardisExterior = false;

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension(), this.tardis.getWorld().getServer());
        if (exteriorWorld == null) return;

        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);
        BlockState exteriorUpBlockState = exteriorWorld.getBlockState(exteriorBlockPos.up());

        BlockState tardisExteriorBlockState = ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlock().getDefaultState();
        tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.LOWER);
        tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.FACING, this.tardis.getCurrentExteriorFacing());
        tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.WATERLOGGED, exteriorBlockState.getFluidState().isIn(FluidTags.WATER));
        exteriorWorld.setBlockState(exteriorBlockPos, tardisExteriorBlockState, Block.NOTIFY_ALL);

        BlockState tardisExteriorUpBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
        tardisExteriorUpBlockState = tardisExteriorUpBlockState.with(BaseTardisExteriorBlock.WATERLOGGED, exteriorUpBlockState.getFluidState().isIn(FluidTags.WATER));
        exteriorWorld.setBlockState(exteriorBlockPos.up(), tardisExteriorUpBlockState, Block.NOTIFY_ALL);

        if (exteriorWorld.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            tardisExteriorBlockEntity.tardisId = this.tardis.getId();

            this.isMaterialized = true;
            this.rematTickInProgressGoal = DWM.TIMINGS.REMAT;
            this.rematTickInProgress = this.rematTickInProgressGoal;
            this.updateExterior(exteriorWorld, false, true);
            ModSounds.playTardisLandingSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());

            this.rematConsumers.add(() -> {
                Box box = Box.of(Vec3d.ofBottomCenter(exteriorBlockPos), 0.5D, 1, 0.5D);
                Vec3d pos = Vec3d.ofBottomCenter(this.tardis.getEntrancePosition().offset(this.tardis.getEntranceFacing()));
                float yaw = tardis.getEntranceFacing().asRotation();

                List<Entity> entities = exteriorWorld.getEntitiesByClass(Entity.class, box, EntityPredicates.VALID_ENTITY);
                for (Entity entity : entities) {
                    entity.teleport(tardis.getWorld(), pos.x, pos.y, pos.z, ImmutableSet.of(), yaw, 0);
                }

                this.tardis.updateConsoleTiles();
                this.updateExterior(exteriorWorld, false, false);
            });
        }
        else {
            exteriorWorld.removeBlock(exteriorBlockPos.up(), false);
            exteriorWorld.removeBlock(exteriorBlockPos, false);
            this.setupFail();
        }
    }

    private boolean tryLandToForeignTardis(ServerWorld exteriorWorld) {
        if (exteriorWorld.getBlockEntity(this.tardis.getCurrentExteriorPosition()) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            ServerWorld foreignTardisWorld = tardisExteriorBlockEntity.getTardisWorld(true);

            if (foreignTardisWorld != null) {
                Optional<TardisStateManager> tardisHolder = TardisStateManager.get(foreignTardisWorld);
                if (tardisHolder.isEmpty() || !tardisHolder.get().isValid()) return false;
                if (tardisHolder.get().getSystem(TardisSystemShields.class).inProgress()) return false;

                this.tardis.setDimension(tardisHolder.get().getWorld().getRegistryKey(), false);
                this.tardis.setFacing(tardisHolder.get().getEntranceFacing(), false);
                this.tardis.setPosition(tardisHolder.get().getEntrancePosition().offset(tardisHolder.get().getEntranceFacing()), false);
                this.remat();
                return true;
            }
        }

        return false;
    }

    private boolean findSafePosition(ServerWorld exteriorWorld) {
        if (exteriorWorld == null) return false;

        Direction exteriorFacing = this.tardis.getCurrentExteriorFacing();
        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        BlockPos safePosition = null;

        if (this.safeDirection == ESafeDirection.TOP) {
            safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, ESafeDirection.TOP);
            if (safePosition == null) safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, ESafeDirection.BOTTOM);
        }
        else if (this.safeDirection == ESafeDirection.BOTTOM) {
            safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, ESafeDirection.BOTTOM);
            if (safePosition == null) safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, ESafeDirection.TOP);
        }
        else {
            safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, this.safeDirection);
        }

        if (safePosition != null) {
            this.tardis.setPosition(safePosition, false);
            this.tardis.setDestinationPosition(safePosition);
            return true;
        }

        return false;
    }

    private BlockPos getSafePosition(ServerWorld exteriorWorld, BlockPos exteriorBlockPos, Direction exteriorFacing, ESafeDirection safeDirection) {
        if (safeDirection == ESafeDirection.TOP) exteriorBlockPos = exteriorBlockPos.down();
        else if (safeDirection == ESafeDirection.BOTTOM) exteriorBlockPos = exteriorBlockPos.up();

        boolean checkBottom = safeDirection != ESafeDirection.NONE;
        boolean freeSpaceFound;
        boolean isBuildLimitValid;

        do {
            exteriorBlockPos = safeDirection == ESafeDirection.TOP
                ? exteriorBlockPos.up()
                : safeDirection == ESafeDirection.BOTTOM
                    ? exteriorBlockPos.down()
                    : exteriorBlockPos;

            freeSpaceFound = this.checkBlockIsSafe(exteriorWorld, exteriorBlockPos, exteriorFacing, checkBottom);
            if (!freeSpaceFound) {
                for (Direction direction : Direction.values()) {
                    if (direction == exteriorFacing) continue;
                    freeSpaceFound = this.checkBlockIsSafe(exteriorWorld, exteriorBlockPos, direction, checkBottom);

                    if (freeSpaceFound) {
                        this.tardis.setFacing(direction, false);
                        this.tardis.setDestinationFacing(direction);
                        break;
                    }
                }
            }

            isBuildLimitValid = exteriorWorld.isInBuildLimit(exteriorBlockPos) && exteriorWorld.isInBuildLimit(exteriorBlockPos.up());
            if (safeDirection == ESafeDirection.DIRECT || safeDirection == ESafeDirection.NONE) break;
        } while (!freeSpaceFound && isBuildLimitValid);

        return freeSpaceFound && isBuildLimitValid ? exteriorBlockPos : null;
    }

    private boolean checkBlockIsSafe(World world, BlockPos blockPos, Direction direction, boolean checkBottom) {
        boolean isEmpty = WorldHelper.checkBlockIsEmpty(world.getBlockState(blockPos), true);
        boolean isUpEmpty = WorldHelper.checkBlockIsEmpty(world.getBlockState(blockPos.up()), true);
        boolean isBottomSolid = WorldHelper.checkBlockIsSolid(world.getBlockState(blockPos.down()));

        if (ModCompats.immersivePortals()) return (!checkBottom || isBottomSolid) && isEmpty && isUpEmpty;

        boolean isFrontEmpty = WorldHelper.checkBlockIsEmpty(world.getBlockState(blockPos.offset(direction)), false);
        boolean isFrontUpEmpty = WorldHelper.checkBlockIsEmpty(world.getBlockState(blockPos.offset(direction).up()), false);
        boolean isFrontBottomSolid = WorldHelper.checkBlockIsSolid(world.getBlockState(blockPos.offset(direction).down()));

        return (!checkBottom || isBottomSolid) && isEmpty && isUpEmpty && (!checkBottom || isFrontBottomSolid) && isFrontEmpty && isFrontUpEmpty;
    }

    private void updateExterior(ServerWorld exteriorWorld, boolean demat, boolean remat) {
        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();

        if (exteriorWorld.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (demat) tardisExteriorBlockEntity.demat();
            else if (remat) tardisExteriorBlockEntity.remat();
            else if (!this.inProgress()) tardisExteriorBlockEntity.resetMaterializationState(this.isMaterialized);
        }

        if (!demat) return;

        new TardisExteriorUpdatePacket(exteriorBlockPos, this.tardis.isDoorsOpened(), this.tardis.isLightEnabled(), true)
            .sendToChunkListeners(exteriorWorld.getWorldChunk(exteriorBlockPos));
    }

    private boolean setupFail() {
        this.runFailConsumers();
        ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        return false;
    }

    private boolean setupDeferredFail() {
        this.runFailConsumers = true;
        ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        return false;
    }
}

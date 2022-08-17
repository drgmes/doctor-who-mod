package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.network.TardisExteriorRemoteCallablePackets;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.api.PortalAPI;

import java.util.ArrayList;
import java.util.List;

public class TardisSystemMaterialization implements ITardisSystem {
    public enum ESafeDirection {
        TOP,
        BOTTOM,
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
    private boolean needsRerunRemat = false;

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
        this.isMaterialized = tag.getBoolean("isMaterialized");
        this.needsRerunRemat = tag.getBoolean("needsRerunRemat");
        this.dematTickInProgress = tag.getFloat("dematTickInProgress");
        this.rematTickInProgress = tag.getFloat("rematTickInProgress");
        this.dematTickInProgressGoal = tag.getFloat("dematTickInProgressGoal");
        this.rematTickInProgressGoal = tag.getFloat("rematTickInProgressGoal");
        this.safeDirection = ESafeDirection.valueOf(tag.getString("safeDirection"));
    }

    @Override
    public NbtCompound save() {
        NbtCompound tag = new NbtCompound();

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

    public void setSafeDirection(ESafeDirection value) {
        this.safeDirection = value;
    }

    public void setSafeDirection(int value) {
        if (value == 0) this.safeDirection = ESafeDirection.TOP;
        else if (value == 1) this.safeDirection = ESafeDirection.BOTTOM;
        else if (value == 2) this.safeDirection = ESafeDirection.NONE;
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
            ModSounds.playTardisTakeoffSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension());
        if (exteriorWorld == null) return false;

        this.isMaterialized = false;
        this.dematTickInProgressGoal = 0;
        this.tardis.updateConsoleTiles();

        CommonHelper.runInThread("materialization", () -> {
            BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
            BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);
            if (!Thread.currentThread().isAlive() || Thread.currentThread().isInterrupted()) return;

            if (exteriorBlockState.getBlock() instanceof BaseTardisExteriorBlock) {
                exteriorWorld.removeBlock(exteriorBlockPos.up(), true);
                exteriorWorld.removeBlock(exteriorBlockPos, true);
            }
        });

        this.runDematConsumers();
        return true;
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
            ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        if (this.isMaterialized) {
            this.runRematConsumers();
            return true;
        }

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension());
        if (exteriorWorld == null) return false;

        BlockPos initialExteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        if (!this.needsRerunRemat) {
            this.addChunkToLoader(exteriorWorld, initialExteriorBlockPos);
            this.needsRerunRemat = true;
            return false;
        }

        if (this.findSafePosition()) {
            BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
            BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);
            BlockState exteriorUpBlockState = exteriorWorld.getBlockState(exteriorBlockPos.up());

            BlockState tardisExteriorBlockState = ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlock().getDefaultState();
            tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.LOWER);
            tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.FACING, this.tardis.getCurrentExteriorFacing());
            tardisExteriorBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.WATERLOGGED, exteriorBlockState.getFluidState().isIn(FluidTags.WATER));
            exteriorWorld.setBlockState(exteriorBlockPos, tardisExteriorBlockState, 3);

            BlockState tardisExteriorUpBlockState = tardisExteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
            tardisExteriorUpBlockState = tardisExteriorUpBlockState.with(BaseTardisExteriorBlock.WATERLOGGED, exteriorUpBlockState.getFluidState().isIn(FluidTags.WATER));
            exteriorWorld.setBlockState(exteriorBlockPos.up(), tardisExteriorUpBlockState, 3);

            if (exteriorWorld.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
                tardisExteriorBlockEntity.tardisId = DimensionHelper.getWorldId(this.tardis.getWorld());

                this.isMaterialized = true;
                this.needsRerunRemat = false;
                this.rematTickInProgressGoal = DWM.TIMINGS.REMAT;
                this.rematTickInProgress = this.rematTickInProgressGoal;
                this.removeChunkFromLoader(exteriorWorld, initialExteriorBlockPos);
                this.updateExterior(false, true);
                ModSounds.playTardisLandingSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());

                this.rematConsumers.add(() -> {
                    Box box = Box.of(Vec3d.ofBottomCenter(exteriorBlockPos), 0.5D, 1, 0.5D);
                    BlockPos entrancePosition = this.tardis.getEntrancePosition().offset(this.tardis.getEntranceFacing());
                    List<Entity> entities = exteriorWorld.getEntitiesByClass(Entity.class, box, EntityPredicates.VALID_ENTITY);

                    for (Entity entity : entities) {
                        PortalAPI.teleportEntity(entity, this.tardis.getWorld(), Vec3d.ofBottomCenter(entrancePosition));
                    }

                    this.tardis.updateConsoleTiles();
                    this.updateExterior(false, false);
                });

                return true;
            }
            else {
                this.demat();
                this.runFailConsumers();
                ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            }
        }
        else if (!this.tryLandToForeignTardis()) {
            this.runFailConsumers();
            ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        }

        this.removeChunkFromLoader(exteriorWorld, initialExteriorBlockPos);
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
        if (this.safeDirection != ESafeDirection.NONE) return false;

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension());
        if (exteriorWorld == null) return false;

        if (exteriorWorld.getBlockEntity(this.tardis.getCurrentExteriorPosition()) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            ServerWorld foreignTardisWorld = tardisExteriorBlockEntity.getTardisWorld();

            if (foreignTardisWorld != null) {
                TardisStateManager.get(foreignTardisWorld).ifPresent((tardis) -> {
                    if (tardis.isValid() && !tardis.getSystem(TardisSystemShields.class).inProgress()) {
                        this.tardis.setDimension(tardis.getWorld().getRegistryKey(), false);
                        this.tardis.setFacing(tardis.getEntranceFacing(), false);
                        this.tardis.setPosition(tardis.getEntrancePosition().offset(tardis.getEntranceFacing()), false);
                        this.remat();
                    }
                    else {
                        this.runFailConsumers();
                        ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
                    }
                });

                return true;
            }
        }

        return false;
    }

    private void addChunkToLoader(ServerWorld world, BlockPos blockPos) {
        // TODO chunk loader
    }

    private void removeChunkFromLoader(ServerWorld world, BlockPos blockPos) {
        // TODO chunk loader
    }

    private boolean findSafePosition() {
        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension());
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
        else return null;

        boolean freeSpaceFound;
        do {
            exteriorBlockPos = safeDirection == ESafeDirection.TOP ? exteriorBlockPos.up() : exteriorBlockPos.down();
            freeSpaceFound = this.checkBlockIsSafe(exteriorWorld, exteriorBlockPos, exteriorFacing);
        } while (!freeSpaceFound && exteriorWorld.isInBuildLimit(exteriorBlockPos));

        return freeSpaceFound ? exteriorBlockPos : null;
    }

    private boolean checkBlockIsSafe(World world, BlockPos blockPos, Direction direction) {
        boolean isEmpty = CommonHelper.checkBlockIsEmpty(world.getBlockState(blockPos), true);
        boolean isUpEmpty = CommonHelper.checkBlockIsEmpty(world.getBlockState(blockPos.up()), true);
        boolean isBottomSolid = CommonHelper.checkBlockIsSolid(world.getBlockState(blockPos.down()));
        return isBottomSolid && isEmpty && isUpEmpty;
    }

    private void updateExterior(boolean demat, boolean remat) {
        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension());
        if (exteriorWorld == null) return;

        if (exteriorWorld.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            if (demat) tardisExteriorBlockEntity.demat();
            else if (remat) tardisExteriorBlockEntity.remat();
            else if (!this.inProgress()) tardisExteriorBlockEntity.resetMaterializationState(this.isMaterialized);
        }

        if (!demat) return;

        PacketHelper.sendToClient(
            TardisExteriorRemoteCallablePackets.class,
            "updateTardisExteriorData",
            exteriorWorld.getWorldChunk(exteriorBlockPos),
            exteriorBlockPos, this.tardis.isDoorsOpened(), true
        );
    }
}

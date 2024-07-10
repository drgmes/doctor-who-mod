package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorEntry;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriors;
import net.drgmes.dwm.enums.TardisExteriorAction;
import net.drgmes.dwm.enums.TardisVerticalScanning;
import net.drgmes.dwm.network.client.TardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.CommonHelper;
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
    public TardisVerticalScanning verticalScanning = TardisVerticalScanning.TOP;

    private final TardisStateManager tardis;
    private final List<Runnable> dematConsumers = new ArrayList<>();
    private final List<Runnable> rematConsumers = new ArrayList<>();
    private final List<Runnable> failConsumers = new ArrayList<>();

    private boolean isMaterialized = true;
    private float dematTickInProgress = 0;
    private float rematTickInProgress = 0;
    private float dematTickInProgressGoal = 0;
    private float rematTickInProgressGoal = 0;

    public TardisSystemMaterialization(TardisStateManager tardis) {
        this.tardis = tardis;
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
    public void readNbt(NbtCompound tag) {
        if (tag.contains("isMaterialized")) this.isMaterialized = tag.getBoolean("isMaterialized");
        if (tag.contains("dematTickInProgress")) this.dematTickInProgress = tag.getFloat("dematTickInProgress");
        if (tag.contains("rematTickInProgress")) this.rematTickInProgress = tag.getFloat("rematTickInProgress");
        if (tag.contains("dematTickInProgressGoal")) this.dematTickInProgressGoal = tag.getFloat("dematTickInProgressGoal");
        if (tag.contains("rematTickInProgressGoal")) this.rematTickInProgressGoal = tag.getFloat("rematTickInProgressGoal");
        if (tag.contains("verticalScanning")) this.verticalScanning = TardisVerticalScanning.valueOf(tag.getString("verticalScanning"));
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putBoolean("isMaterialized", this.isMaterialized);
        tag.putFloat("dematTickInProgress", this.dematTickInProgress);
        tag.putFloat("rematTickInProgress", this.rematTickInProgress);
        tag.putFloat("dematTickInProgressGoal", this.dematTickInProgressGoal);
        tag.putFloat("rematTickInProgressGoal", this.rematTickInProgressGoal);
        tag.putString("verticalScanning", this.verticalScanning.name());

        return tag;
    }

    @Override
    public void tick() {
        if (this.inProgress()) {
            if (this.inDematProgress()) {
                this.dematTickInProgress--;
                if (!this.inDematProgress()) this.demat();
                else if (this.dematTickInProgress % 3 == 0) this.tardis.markConsoleTilesUpdated();
            }
            else if (this.inRematProgress()) {
                this.rematTickInProgress--;
                if (!this.inRematProgress()) this.remat();
                else if (this.rematTickInProgress % 3 == 0) this.tardis.markConsoleTilesUpdated();
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

    public void setVerticalScanning(TardisVerticalScanning value) {
        this.verticalScanning = value;
    }

    public void setVerticalScanning(int value) {
        if (value == 0) this.verticalScanning = TardisVerticalScanning.TOP;
        else if (value == 1) this.verticalScanning = TardisVerticalScanning.BOTTOM;
        else if (value == 2) this.verticalScanning = TardisVerticalScanning.DIRECT;
        else if (value == 3) this.verticalScanning = TardisVerticalScanning.NONE;
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

        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return false;

        if (this.dematTickInProgressGoal == 0) {
            this.dematTickInProgressGoal = DWM.TIMINGS.DEMAT_DURATION;
            this.dematTickInProgress = this.dematTickInProgressGoal;

            this.tardis.setDoorsOpenState(false);
            this.tardis.setLightState(false);
            this.tardis.setShieldsState(false);
            this.tardis.markConsoleTilesUpdated();

            this.updateExterior(exteriorWorld, TardisExteriorAction.DEMAT);
            ModSounds.playTardisTakeoffSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        this.isMaterialized = false;
        this.dematTickInProgressGoal = 0;
        this.tardis.markConsoleTilesUpdated();

        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);

        if (exteriorBlockState.getBlock() instanceof BaseTardisExteriorBlock<?>) {
            exteriorWorld.removeBlock(exteriorBlockPos.up(), false);
            exteriorWorld.removeBlock(exteriorBlockPos, false);
            this.runDematConsumers();
        }
        else {
            this.setupFail();
        }

        return true;
    }

    public boolean demat(Runnable consumer) {
        this.dematConsumers.add(consumer);
        return this.demat();
    }

    public boolean remat() {
        if (!this.isEnabled()) return false;
        if (this.inProgress()) return false;

        if (this.isMaterialized) {
            this.runRematConsumers();
            return true;
        }

        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return false;

        // Clamping pos in build limit
        BlockPos initialExteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        if (!exteriorWorld.isInBuildLimit(initialExteriorBlockPos)) {
            if (this.verticalScanning == TardisVerticalScanning.TOP) initialExteriorBlockPos = initialExteriorBlockPos.withY(exteriorWorld.getTopY() - 2);
            else if (this.verticalScanning == TardisVerticalScanning.BOTTOM) initialExteriorBlockPos = initialExteriorBlockPos.withY(exteriorWorld.getBottomY());

            this.tardis.setPosition(initialExteriorBlockPos, false);
            this.tardis.setDestinationPosition(initialExteriorBlockPos);
        }

        // Try to land into another TARDIS
        if ((this.verticalScanning == TardisVerticalScanning.DIRECT || this.verticalScanning == TardisVerticalScanning.NONE) && this.tryLandToForeignTardis(exteriorWorld)) {
            return true;
        }

        if (this.findSafePosition(exteriorWorld)) this.tryPlaceTardisExterior();
        else this.setupFail();

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

    private void tryPlaceTardisExterior() {
        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return;

        TardisExteriorEntry exteriorType = this.tardis.getExteriorType();
        if (exteriorType == null) exteriorType = TardisExteriors.CAPSULE;

        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);
        BlockState exteriorUpBlockState = exteriorWorld.getBlockState(exteriorBlockPos.up());

        BlockState tardisExteriorBlockState = exteriorType.getBlock().getDefaultState();
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
            this.rematTickInProgressGoal = DWM.TIMINGS.REMAT_DURATION;
            this.rematTickInProgress = this.rematTickInProgressGoal;

            this.updateExterior(exteriorWorld, TardisExteriorAction.REMAT);
            ModSounds.playTardisLandingSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());

            this.rematConsumers.add(() -> {
                Box box = Box.of(Vec3d.ofBottomCenter(exteriorBlockPos), 0.5D, 1, 0.5D);
                Vec3d pos = Vec3d.ofBottomCenter(this.tardis.getEntrancePosition().offset(this.tardis.getEntranceFacing()));
                float yaw = this.tardis.getEntranceFacing().asRotation();

                List<Entity> entities = exteriorWorld.getEntitiesByClass(Entity.class, box, EntityPredicates.VALID_ENTITY);
                for (Entity entity : entities) {
                    CommonHelper.teleport(entity, this.tardis.getWorld(), pos, yaw);
                }

                this.tardis.markConsoleTilesUpdated();
                this.updateExterior(exteriorWorld, TardisExteriorAction.NONE);
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
            ServerWorld foreignTardisWorld = tardisExteriorBlockEntity.getOrCreateTardisWorld();

            if (foreignTardisWorld != null) {
                Optional<TardisStateManager> tardisHolder = TardisStateManager.get(foreignTardisWorld);
                if (tardisHolder.isEmpty() || tardisHolder.get().getSystem(TardisSystemShields.class).inProgress()) return false;

                tardisHolder.get().init();
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

        if (this.verticalScanning == TardisVerticalScanning.TOP) {
            safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, TardisVerticalScanning.TOP);
            if (safePosition == null) safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, TardisVerticalScanning.BOTTOM);
        }
        else if (this.verticalScanning == TardisVerticalScanning.BOTTOM) {
            safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, TardisVerticalScanning.BOTTOM);
            if (safePosition == null) safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, TardisVerticalScanning.TOP);
        }
        else {
            safePosition = this.getSafePosition(exteriorWorld, exteriorBlockPos, exteriorFacing, this.verticalScanning);
        }

        if (safePosition != null) {
            this.tardis.setPosition(safePosition, false);
            this.tardis.setDestinationPosition(safePosition);
            return true;
        }

        return false;
    }

    private BlockPos getSafePosition(ServerWorld exteriorWorld, BlockPos exteriorBlockPos, Direction exteriorFacing, TardisVerticalScanning verticalScanning) {
        if (verticalScanning == TardisVerticalScanning.TOP) exteriorBlockPos = exteriorBlockPos.down();
        else if (verticalScanning == TardisVerticalScanning.BOTTOM) exteriorBlockPos = exteriorBlockPos.up();

        boolean checkBottom = verticalScanning != TardisVerticalScanning.NONE;
        boolean freeSpaceFound;
        boolean isBuildLimitValid;

        do {
            exteriorBlockPos = verticalScanning == TardisVerticalScanning.TOP
                ? exteriorBlockPos.up()
                : verticalScanning == TardisVerticalScanning.BOTTOM
                    ? exteriorBlockPos.down()
                    : exteriorBlockPos;

            freeSpaceFound = this.checkBlockIsSafe(exteriorWorld, exteriorBlockPos, exteriorFacing, checkBottom);
            if (!freeSpaceFound) {
                for (Direction direction : Direction.Type.HORIZONTAL) {
                    if (direction == exteriorFacing) continue;
                    if (direction.getAxis() != Direction.Axis.X && direction.getAxis() != Direction.Axis.Z) continue;
                    freeSpaceFound = this.checkBlockIsSafe(exteriorWorld, exteriorBlockPos, direction, checkBottom);

                    if (freeSpaceFound) {
                        this.tardis.setFacing(direction, false);
                        this.tardis.setDestinationFacing(direction);
                        break;
                    }
                }
            }

            isBuildLimitValid = exteriorWorld.isInBuildLimit(exteriorBlockPos) && exteriorWorld.isInBuildLimit(exteriorBlockPos.up());
            if (verticalScanning == TardisVerticalScanning.DIRECT || verticalScanning == TardisVerticalScanning.NONE) break;
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

    private void updateExterior(ServerWorld exteriorWorld, TardisExteriorAction exteriorAction) {
        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();

        if (exteriorWorld.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            switch (exteriorAction) {
                case DEMAT -> tardisExteriorBlockEntity.demat();
                case REMAT -> tardisExteriorBlockEntity.remat();

                default -> {
                    if (!this.inProgress()) tardisExteriorBlockEntity.reset();
                }
            }
        }

        new TardisExteriorUpdatePacket(exteriorBlockPos, exteriorAction)
            .sendToChunkListeners(exteriorWorld.getWorldChunk(exteriorBlockPos));
    }

    private boolean setupFail() {
        this.runFailConsumers();
        ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        return false;
    }
}

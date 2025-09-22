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
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Consumer;

public class TardisSystemMaterialization implements ITardisSystem {
    private enum EStep {
        NONE,
        INITED,
        PROCESSING
    }

    private enum EMode {
        NONE,
        DEMAT,
        REMAT
    }

    private final TardisStateManager tardis;
    private final List<Consumer<Boolean>> callbacks = new ArrayList<>();

    private UUID initiatorId;
    private EStep step = EStep.NONE;
    private EMode mode = EMode.NONE;
    private TardisVerticalScanning verticalScanning = TardisVerticalScanning.TOP;

    private boolean isMaterialized = true;
    private float tick = -1;

    public TardisSystemMaterialization(TardisStateManager tardis) {
        this.tardis = tardis;
    }

    @Override
    public boolean isEnabled() {
        return this.tardis.isSystemEnabled(this.getClass());
    }

    @Override
    public boolean inProgress() {
        return this.step != EStep.NONE;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        if (tag.contains("initiatorId")) this.initiatorId = tag.getUuid("initiatorId");
        if (tag.contains("step")) this.step = EStep.valueOf(tag.getString("step"));
        if (tag.contains("mode")) this.mode = EMode.valueOf(tag.getString("mode"));
        if (tag.contains("verticalScanning")) this.verticalScanning = TardisVerticalScanning.valueOf(tag.getString("verticalScanning"));
        if (tag.contains("isMaterialized")) this.isMaterialized = tag.getBoolean("isMaterialized");
        if (tag.contains("tick")) this.tick = tag.getFloat("tick");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if (this.initiatorId != null) tag.putUuid("initiatorId", this.initiatorId);
        tag.putString("step", this.step.name());
        tag.putString("mode", this.mode.name());
        tag.putString("verticalScanning", this.verticalScanning.name());
        tag.putBoolean("isMaterialized", this.isMaterialized);
        tag.putFloat("tick", this.tick);
        return tag;
    }

    @Override
    public void tick() {
        if (!this.isEnabled() || !this.inProgress()) return;
        if (this.tick > 0) this.tick -= 1;

        switch (this.step) {
            case INITED -> {
                boolean isSuccessful = switch (this.mode) {
                    case DEMAT -> this.initDemat();
                    case REMAT -> this.initRemat();
                    default -> false;
                };

                if (!isSuccessful) {
                    this.reset();
                    this.applyCallbacks(false);
                }
            }

            case PROCESSING -> {
                if (this.tick % 3 == 0) {
                    this.tardis.markConsoleTilesUpdated();
                }

                if (this.tick == 0) {
                    boolean isSuccessful = switch (this.mode) {
                        case DEMAT -> this.finishDemat();
                        case REMAT -> this.finishRemat();
                        default -> false;
                    };

                    this.reset();
                    this.applyCallbacks(isSuccessful);
                }
            }
        }
    }

    public boolean init(boolean flag, PlayerEntity initiator) {
        if (!this.isEnabled() || this.inProgress() || this.isMaterialized == flag) return false;
        if (this.tardis.getSystem(TardisSystemFlight.class).inProgress()) return false;

        this.step = EStep.INITED;
        this.mode = flag ? EMode.REMAT : EMode.DEMAT;
        this.tick = 0;
        this.initiatorId = initiator.getUuid();
        return true;
    }

    public boolean initDemat() {
        if (!this.isEnabled() || !this.isMaterialized || this.tick > 0) return false;

        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return false;

        this.step = EStep.PROCESSING;
        this.mode = EMode.DEMAT;
        this.tick = DWM.TIMINGS.DEMAT_DURATION;

        this.tardis.setDoorsOpenState(false);
        this.tardis.setLightState(false);
        this.tardis.setShieldsState(false);
        this.tardis.markConsoleTilesUpdated();

        this.sendExteriorUpdatePacket(TardisExteriorAction.DEMAT);
        ModSounds.playTardisTakeoffSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        return true;
    }

    public boolean finishDemat() {
        if (!this.isEnabled() || !this.inProgress()) return false;

        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return false;

        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);

        if (exteriorBlockState.getBlock() instanceof BaseTardisExteriorBlock<?>) {
            exteriorWorld.removeBlock(exteriorBlockPos.up(), false);
            exteriorWorld.removeBlock(exteriorBlockPos, false);
        }

        this.isMaterialized = false;
        this.tardis.markConsoleTilesUpdated();
        return true;
    }

    public boolean initRemat() {
        if (!this.isEnabled() || this.isMaterialized || this.tick > 0) return false;

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

        boolean isValidForLandingInsideAnotherTardis = this.verticalScanning == TardisVerticalScanning.DIRECT || this.verticalScanning == TardisVerticalScanning.NONE;

        // Try to land into another TARDIS
        if ((isValidForLandingInsideAnotherTardis && this.tryLandToForeignTardis(exteriorWorld)) || (!isValidForLandingInsideAnotherTardis && this.tryPlaceTardisExterior())) {
            this.isMaterialized = true;
            this.step = EStep.PROCESSING;
            this.mode = EMode.REMAT;
            this.tick = DWM.TIMINGS.REMAT_DURATION;

            this.sendExteriorUpdatePacket(TardisExteriorAction.REMAT);
            ModSounds.playTardisLandingSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        }
        else {
            this.playFailSound();
            return false;
        }

        return true;
    }

    public boolean finishRemat() {
        if (!this.isEnabled() || !this.inProgress()) return false;

        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return false;

        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        Box box = Box.of(Vec3d.ofBottomCenter(exteriorBlockPos), 0.5D, 1, 0.5D);
        Vec3d pos = Vec3d.ofBottomCenter(this.tardis.getEntrancePosition().offset(this.tardis.getEntranceFacing()));
        float yaw = this.tardis.getEntranceFacing().asRotation();

        List<Entity> entities = exteriorWorld.getEntitiesByClass(Entity.class, box, EntityPredicates.VALID_ENTITY);
        for (Entity entity : entities) {
            CommonHelper.teleport(entity, this.tardis.getWorld(), pos, yaw);
        }

        this.sendExteriorUpdatePacket(TardisExteriorAction.NONE);
        this.tardis.markConsoleTilesUpdated();
        return true;
    }

    public void reset() {
        this.step = EStep.NONE;
        this.mode = EMode.NONE;
        this.tick = -1;
        this.initiatorId = null;
    }

    public void putCallback(Consumer<Boolean> callback) {
        this.callbacks.add(callback);
    }

    public void applyCallbacks(boolean isSuccessful) {
        this.callbacks.forEach((callback) -> callback.accept(isSuccessful));
        this.callbacks.clear();
    }

    public boolean isMaterialized() {
        return !this.inProgress() && this.isMaterialized;
    }

    public int getProgressPercent() {
        return switch (this.mode) {
            case DEMAT -> (int) Math.ceil(this.tick / DWM.TIMINGS.DEMAT_DURATION * 100);
            case REMAT -> 100 - (int) Math.ceil(this.tick / DWM.TIMINGS.REMAT_DURATION * 100);
            default -> this.isMaterialized ? 100 : 0;
        };
    }

    public TardisVerticalScanning getVerticalScanning() {
        return this.verticalScanning;
    }

    public void setVerticalScanning(TardisVerticalScanning value) {
        this.verticalScanning = value;
    }

    public void setVerticalScanning(int value) {
        switch (value) {
            case 1 -> this.setVerticalScanning(TardisVerticalScanning.BOTTOM);
            case 2 -> this.setVerticalScanning(TardisVerticalScanning.DIRECT);
            case 3 -> this.setVerticalScanning(TardisVerticalScanning.NONE);
            default -> this.setVerticalScanning(TardisVerticalScanning.TOP);
        }
    }

    private void notify(Text message) {
        if (this.initiatorId == null) return;

        PlayerEntity initiator = this.tardis.getWorld().getServer().getPlayerManager().getPlayer(this.initiatorId);
        if (initiator != null) initiator.sendMessage(message, true);
    }

    private void playFailSound() {
        ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
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
                return true;
            }
        }

        return false;
    }

    private boolean tryPlaceTardisExterior() {
        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return false;

        if (!this.findSafePosition(exteriorWorld)) return false;

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
            return true;
        }
        else {
            exteriorWorld.removeBlock(exteriorBlockPos.up(), false);
            exteriorWorld.removeBlock(exteriorBlockPos, false);
            this.playFailSound();
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
                : (verticalScanning == TardisVerticalScanning.BOTTOM
                    ? exteriorBlockPos.down()
                    : exteriorBlockPos);

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

    private void sendExteriorUpdatePacket(TardisExteriorAction exteriorAction) {
        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return;

        if (exteriorWorld.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            switch (exteriorAction) {
                case DEMAT -> tardisExteriorBlockEntity.demat();
                case REMAT -> tardisExteriorBlockEntity.remat();
                default -> tardisExteriorBlockEntity.reset();
            }
        }

        new TardisExteriorUpdatePacket(exteriorBlockPos, exteriorAction)
            .sendToAll(exteriorWorld.getServer());
    }
}

package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemConsoleRoom;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.enums.TardisExteriorState;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.UUID;

public abstract class BaseTardisExteriorBlockEntity extends BlockEntity {
    public String tardisId;

    private TardisExteriorState exteriorState = TardisExteriorState.MATERIALIZED;
    private boolean inited;
    private int tick = -1;

    public BaseTardisExteriorBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if (tag.contains("tardisId")) this.tardisId = tag.getString("tardisId");
        if (tag.contains("exteriorState")) this.exteriorState = TardisExteriorState.valueOf(tag.getString("exteriorState"));
        if (tag.contains("tick")) this.tick = tag.getInt("tick");
    }

    @Override
    public void writeNbt(NbtCompound tag) {
        if (!this.inited) this.init();
        super.writeNbt(tag);

        if (this.tardisId != null) tag.putString("tardisId", this.tardisId);
        tag.putString("exteriorState", this.exteriorState.name());
        tag.putInt("tick", this.tick);
    }

    public String getOrCreateTardisId() {
        if (this.tardisId == null || this.tardisId.isEmpty()) this.tardisId = UUID.randomUUID().toString();
        return this.tardisId;
    }

    public ServerWorld getTardisWorld() {
        if (this.tardisId == null || this.tardisId.isEmpty()) return null;
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) return null;
        return DimensionHelper.getModWorld(this.getOrCreateTardisId(), serverWorld.getServer());
    }

    public ServerWorld getOrCreateTardisWorld() {
        return TardisHelper.getOrCreateTardisWorld(this);
    }

    public TardisExteriorState getExteriorState() {
        return this.exteriorState;
    }

    public float getMaterializedStateValue() {
        return switch (this.exteriorState) {
            case MATERIALIZED -> 1;
            case DEMATERIALIZED -> 0;
            case PROCESS_DEMAT -> (float) this.tick / DWM.TIMINGS.DEMAT_DURATION;
            case PROCESS_REMAT -> (float) (DWM.TIMINGS.REMAT_DURATION - this.tick) / DWM.TIMINGS.REMAT_DURATION;
            case PROCESS_PULSE -> (float) (DWM.TIMINGS.PULSE_LOOP - this.tick) / DWM.TIMINGS.PULSE_LOOP;
        };
    }

    public void init() {
        if (this.inited) return;
        this.inited = true;

        TardisStateManager.get(this.getTardisWorld()).ifPresent((tardis) -> {
            TardisSystemConsoleRoom consoleRoomSystem = tardis.getSystem(TardisSystemConsoleRoom.class);
            TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);

            if (!consoleRoomSystem.inProgress() && !materializationSystem.inProgress() && materializationSystem.isMaterialized()) {
                this.normalize();
            }
        });
    }

    public void reset() {
        this.tick = -1;

        this.exteriorState = switch (this.exteriorState) {
            case MATERIALIZED, DEMATERIALIZED -> this.exteriorState;
            case PROCESS_DEMAT -> TardisExteriorState.DEMATERIALIZED;
            case PROCESS_REMAT -> TardisExteriorState.MATERIALIZED;

            case PROCESS_PULSE -> {
                ModSounds.playTardisConsoleCrackSound(this.world, this.getPos());
                this.tick = DWM.TIMINGS.PULSE_LOOP;
                yield this.exteriorState;
            }
        };

        this.markDirty();
    }

    public void normalize() {
        this.tick = -1;
        this.exteriorState = TardisExteriorState.MATERIALIZED;
        this.markDirty();
    }

    public void demat() {
        this.tick = DWM.TIMINGS.DEMAT_DURATION;
        this.exteriorState = TardisExteriorState.PROCESS_DEMAT;
        ModSounds.playTardisTakeoffSound(this.world, this.getPos());
        this.markDirty();
    }

    public void remat() {
        this.tick = DWM.TIMINGS.REMAT_DURATION;
        this.exteriorState = TardisExteriorState.PROCESS_REMAT;
        ModSounds.playTardisLandingSound(this.world, this.getPos());
        this.markDirty();
    }

    public void pulse() {
        this.tick = DWM.TIMINGS.PULSE_LOOP;
        this.exteriorState = TardisExteriorState.PROCESS_PULSE;
        ModSounds.playTardisConsoleCrackSound(this.world, this.getPos());
        this.markDirty();
    }

    public void tick() {
        if (this.tick < 0) return;
        if (this.tick > 0) this.tick -= 1;
        else this.reset();
    }

    protected void update() {
    }
}

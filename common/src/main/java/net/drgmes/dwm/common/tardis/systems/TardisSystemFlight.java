package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.nbt.NbtCompound;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

public class TardisSystemFlight extends TardisBaseSystem {
    private enum EStep {
        NONE,
        INITED,
        WAIT_FOR_DEMAT,
        PROCESSING,
    }

    private final List<Consumer<Boolean>> callbacks = new ArrayList<>();

    private EStep step = EStep.NONE;
    private UUID initiatorId;

    private int tick = -1;
    private int soundTick = -1;

    public TardisSystemFlight(TardisStateManager tardis) {
        super(tardis);
    }

    @Override
    public boolean inProgress() {
        return this.step != EStep.NONE;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        if (tag.contains("step")) this.step = EStep.valueOf(tag.getString("step"));
        if (tag.contains("initiatorId")) this.initiatorId = tag.getUuid("initiatorId");
        if (tag.contains("tick")) this.tick = tag.getInt("tick");
        if (tag.contains("soundTick")) this.soundTick = tag.getInt("soundTick");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if (this.initiatorId != null) tag.putUuid("initiatorId", this.initiatorId);

        tag.putString("step", this.step.name());
        tag.putInt("tick", this.tick);
        tag.putInt("soundTick", this.soundTick);

        return tag;
    }

    @Override
    public void tick() {
        if (!this.isEnabled() || !this.inProgress()) return;
        if (this.tick > 0) this.tick -= 1;

        switch (this.step) {
            case INITED -> {
                if (!this.takeoff()) {
                    this.reset();
                    this.applyCallbacks(false);
                    this.tardis.markConsoleTilesUpdated();
                }
            }

            case PROCESSING -> {
                this.playFlightSound();

                if (this.tick % 3 == 0) {
                    this.tardis.markConsoleTilesUpdated();
                }

                if (this.tick == 0) {
                    if (!this.land()) {
                        this.reset();
                        this.applyCallbacks(false);
                        this.tardis.markConsoleTilesUpdated();
                    }
                }
            }
        }
    }

    public boolean init(boolean flag, UUID initiatorId) {
        if (!this.isEnabled() || this.isInFlight() || flag == this.inProgress()) return false;

        if (!flag) {
            this.reset();
            this.resetCallbacks();
            return true;
        }

        TardisSystemMaterialization materializationSystem = this.tardis.getSystem(TardisSystemMaterialization.class);
        if (materializationSystem.inProgress()) return false;

        this.step = EStep.INITED;
        this.initiatorId = initiatorId;
        this.tick = 0;
        this.soundTick = 0;
        return true;
    }

    public void reset() {
        this.step = EStep.NONE;
        this.initiatorId = null;
        this.tick = -1;
        this.soundTick = -1;
    }

    public void putCallback(Consumer<Boolean> callback) {
        this.callbacks.add(callback);
    }

    public void applyCallbacks(boolean isSuccessful) {
        this.callbacks.forEach((callback) -> callback.accept(isSuccessful));
        this.resetCallbacks();
    }

    public void resetCallbacks() {
        this.callbacks.clear();
    }

    public boolean isInFlight() {
        return this.inProgress() && this.tick > 0;
    }

    public int getProgressPercent() {
        return 100 - (int) Math.ceil((float) this.tick / this.getFlightDuration() * 100);
    }

    public int getFlightDuration() {
        return DWM.TIMINGS.FLIGHT_LOOP;
    }

    private boolean takeoff() {
        if (!this.isEnabled() || !this.inProgress() || this.tick > 0) return false;

        TardisSystemMaterialization materializationSystem = this.tardis.getSystem(TardisSystemMaterialization.class);
        if (!materializationSystem.isEnabled() || materializationSystem.inProgress()) return false;

        materializationSystem.putCallback((flag) -> {
            if (!flag || this.step == EStep.NONE) return;

            this.step = EStep.PROCESSING;
            this.tick = this.getFlightDuration();
            this.tardis.markConsoleTilesUpdated();
        });

        if (!materializationSystem.isMaterialized()) {
            materializationSystem.applyCallbacks(true);
        } else if (materializationSystem.init(false, this.initiatorId)) {
            this.step = EStep.WAIT_FOR_DEMAT;
        } else {
            materializationSystem.reset();
            materializationSystem.applyCallbacks(false);
            return false;
        }

        return true;
    }

    private boolean land() {
        if (!this.isEnabled() || !this.inProgress() || this.tick > 0) return false;

        TardisSystemMaterialization materializationSystem = this.tardis.getSystem(TardisSystemMaterialization.class);
        if (!materializationSystem.isEnabled() || materializationSystem.inProgress()) return false;

        this.tardis.setDimension(this.tardis.getDestinationExteriorDimension(), true);
        this.tardis.setFacing(this.tardis.getDestinationExteriorFacing(), true);
        this.tardis.setPosition(this.tardis.getDestinationExteriorPosition(), true);
        this.tardis.markConsoleTilesUpdated();

        materializationSystem.putCallback((flag) -> {
            this.reset();
            this.applyCallbacks(flag);
        });

        if (!materializationSystem.init(true, this.initiatorId)) {
            materializationSystem.reset();
            materializationSystem.applyCallbacks(false);
            return false;
        }

        return true;
    }

    private void playFlightSound() {
        if (this.soundTick > 0) {
            this.soundTick -= 1;
        }
        else if (this.soundTick == 0) {
            this.soundTick = DWM.TIMINGS.FLIGHT_LOOP;
            ModSounds.playTardisFlightSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        }
    }
}

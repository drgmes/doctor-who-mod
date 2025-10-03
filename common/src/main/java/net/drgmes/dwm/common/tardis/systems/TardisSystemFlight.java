package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.flight.TardisFlightHistoryEntry;
import net.drgmes.dwm.common.tardis.systems.flight.TardisFlightWaypointEntry;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.minecraft.nbt.NbtCompound;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class TardisSystemFlight extends TardisBaseSystem {
    public static final int HISTORY_SIZE = 100;

    private enum EStep {
        NONE,
        INITED,
        WAIT_FOR_DEMAT,
        PROCESSING,
    }

    private final List<Consumer<Boolean>> callbacks = new ArrayList<>();
    private List<TardisFlightHistoryEntry> history = new ArrayList<>();
    private List<TardisFlightWaypointEntry> waypoints = new ArrayList<>();

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

        if (tag.contains("history")) {
            this.history.clear();

            NbtCompound historyTag = tag.getCompound("history");
            List<String> keys = new ArrayList<>(historyTag.getKeys());
            keys.sort(Comparator.comparing((key) -> key));

            keys.forEach((key) -> {
                TardisFlightHistoryEntry entry = TardisFlightHistoryEntry.createFromNbt(historyTag.getCompound(key));
                if (!this.history.contains(entry)) this.history.add(entry);
            });
        }

        if (tag.contains("waypoints")) {
            this.waypoints.clear();

            NbtCompound waypointsTag = tag.getCompound("waypoints");
            List<String> keys = new ArrayList<>(waypointsTag.getKeys());
            keys.sort(Comparator.comparing((key) -> key));

            keys.forEach((key) -> {
                TardisFlightWaypointEntry entry = TardisFlightWaypointEntry.createFromNbt(waypointsTag.getCompound(key));
                if (!this.waypoints.contains(entry)) this.waypoints.add(entry);
            });
        }
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if (this.initiatorId != null) tag.putUuid("initiatorId", this.initiatorId);

        tag.putString("step", this.step.name());
        tag.putInt("tick", this.tick);
        tag.putInt("soundTick", this.soundTick);

        AtomicInteger i1 = new AtomicInteger();
        NbtCompound historyTag = new NbtCompound();
        this.history.forEach((entry) -> historyTag.put(CommonHelper.formatIndexString(i1.incrementAndGet()), entry.writeNbt(new NbtCompound())));
        tag.put("history", historyTag);

        AtomicInteger i2 = new AtomicInteger();
        NbtCompound waypointsTag = new NbtCompound();
        this.waypoints.forEach((entry) -> waypointsTag.put(CommonHelper.formatIndexString(i2.incrementAndGet()), entry.writeNbt(new NbtCompound())));
        tag.put("waypoints", waypointsTag);

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
                    boolean isSuccessful = this.land();

                    this.reset();
                    this.applyCallbacks(isSuccessful);
                    this.tardis.markConsoleTilesUpdated();
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

    public boolean takeoff() {
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
        }
        else if (materializationSystem.init(false, this.initiatorId)) {
            this.step = EStep.WAIT_FOR_DEMAT;
        }
        else {
            materializationSystem.reset();
            materializationSystem.applyCallbacks(false);
            return false;
        }

        return true;
    }

    public boolean land() {
        if (!this.isEnabled() || !this.inProgress() || this.tick > 0) return false;

        TardisSystemMaterialization materializationSystem = this.tardis.getSystem(TardisSystemMaterialization.class);
        if (!materializationSystem.isEnabled() || materializationSystem.inProgress()) return false;

        this.tardis.setDimension(this.tardis.getDestinationExteriorDimension(), true);
        this.tardis.setPosition(this.tardis.getDestinationExteriorPosition(), true);
        this.tardis.setFacing(this.tardis.getDestinationExteriorFacing(), true);

        materializationSystem.putCallback((flag) -> {
            this.reset();
            this.applyCallbacks(flag);

            TardisSystemResearch researchSystem = this.tardis.getSystem(TardisSystemResearch.class);
            researchSystem.updateVisitedStructures(this.initiatorId);
            researchSystem.updateVisitedBiomes(this.initiatorId);
            researchSystem.updateVisitedWorlds(this.initiatorId);

            this.addHistoryEntry(new TardisFlightHistoryEntry(
                this.tardis.getCurrentExteriorDimension(),
                this.tardis.getCurrentExteriorPosition(),
                this.tardis.getCurrentExteriorFacing()
            ));
        });

        if (!materializationSystem.init(true, this.initiatorId)) {
            materializationSystem.reset();
            materializationSystem.applyCallbacks(false);
            return false;
        }

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

    // ////////////////////// //
    // Flight History methods //
    // ////////////////////// //

    public void addHistoryEntry(TardisFlightHistoryEntry historyEntry) {
        this.history.addFirst(historyEntry);
        if (this.history.size() > HISTORY_SIZE) this.history = this.history.subList(0, HISTORY_SIZE);

        this.tardis.markConsoleTilesUpdated();
        this.tardis.markDirty();
    }

    public boolean deleteHistoryEntry(TardisFlightHistoryEntry historyEntry) {
        Optional<TardisFlightHistoryEntry> foundEntryHolder = this.history.stream().filter((entry) -> entry.equals(historyEntry)).findFirst();
        if (foundEntryHolder.isEmpty()) return false;

        this.history.remove(foundEntryHolder.get());
        this.tardis.markConsoleTilesUpdated();
        this.tardis.markDirty();
        return true;
    }

    public void clearHistory() {
        this.history.clear();
        this.tardis.markConsoleTilesUpdated();
        this.tardis.markDirty();
    }

    // ///////////////// //
    // Waypoints methods //
    // ///////////////// //

    public void addWaypointEntry(TardisFlightWaypointEntry waypointEntry) {
        this.waypoints.add(waypointEntry);
        this.tardis.markConsoleTilesUpdated();
        this.tardis.markDirty();
    }

    public boolean updateWaypointEntry(TardisFlightWaypointEntry oldWaypointEntry, TardisFlightWaypointEntry newWaypointEntry) {
        int index = this.waypoints.indexOf(oldWaypointEntry);
        if (index < 0) return false;

        this.waypoints.set(index, newWaypointEntry);
        this.tardis.markConsoleTilesUpdated();
        this.tardis.markDirty();
        return true;
    }

    public boolean deleteWaypointEntry(TardisFlightWaypointEntry waypointEntry) {
        Optional<TardisFlightWaypointEntry> foundEntryHolder = this.waypoints.stream().filter((entry) -> entry.equals(waypointEntry)).findFirst();
        if (foundEntryHolder.isEmpty()) return false;

        this.waypoints.remove(foundEntryHolder.get());
        this.tardis.markConsoleTilesUpdated();
        this.tardis.markDirty();
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

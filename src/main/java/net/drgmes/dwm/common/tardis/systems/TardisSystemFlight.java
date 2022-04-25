package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;

public class TardisSystemFlight implements ITardisSystem {
    private final ITardisLevelData tardisData;
    private boolean isFlightLaunched = false;
    private boolean isSoundFlyPlayed = false;

    public float tickInProgress = 0;
    public float tickInProgressGoal = 0;

    public TardisSystemFlight(ITardisLevelData tardisData) {
        this.tardisData = tardisData;
    }

    @Override
    public void load(CompoundTag tag) {
        this.tickInProgress = tag.getFloat("tickInProgress");
        this.tickInProgressGoal = tag.getFloat("tickInProgressGoal");
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putFloat("tickInProgress", this.tickInProgress);
        tag.putFloat("tickInProgressGoal", this.tickInProgressGoal);

        return tag;
    }

    @Override
    public void tick() {
        if (this.tickInProgress > 0) {
            this.tickInProgress--;

            this.playFlySound();
            if (this.tickInProgress % DWM.TIMINGS.FLIGHT_LOOP == 0) this.isSoundFlyPlayed = false;

            if (this.tickInProgress == 1) this.land();
            this.tardisData.updateConsoleTiles();
        }
    }

    public int getProgressPercent() {
        return (int) Math.ceil((this.tickInProgressGoal - this.tickInProgress) / this.tickInProgressGoal * 100);
    }

    public boolean inProgress() {
        return this.isFlightLaunched || this.tickInProgress > 0;
    }

    public void setFlight(boolean flag) {
        if (flag ? this.takeoff() : this.land()) {
            this.tardisData.updateConsoleTiles();
        }
    }

    public boolean takeoff() {
        if (this.inProgress()) return false;
        this.isFlightLaunched = true;

        if (this.tardisData.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization rematSystem) {
            return rematSystem.demat(() -> {
                this.isSoundFlyPlayed = false;
                this.tickInProgressGoal = DWM.TIMINGS.FLIGHT_LOOP * 2;
                this.tickInProgress = this.tickInProgressGoal;
            });
        }

        return false;
    }

    public boolean land() {
        if (!this.inProgress()) return false;

        if (this.tardisData.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization rematSystem) {
            this.isFlightLaunched = false;
            this.isSoundFlyPlayed = false;
            this.tickInProgress = 0;
            this.tardisData.getConsoleTiles().forEach((tile) -> tile.controlsStorage.values.put(TardisConsoleControlRoles.STARTER, false));
            this.tardisData.setDimension(this.tardisData.getDestinationExteriorDimension(), true);
            this.tardisData.setFacing(this.tardisData.getDestinationExteriorFacing(), true);
            this.tardisData.setPosition(this.tardisData.getDestinationExteriorPosition(), true);
            this.tardisData.updateConsoleTiles();

            return rematSystem.remat();
        }

        return false;
    }

    public void playSound(SoundEvent soundEvent) {
        BlockPos consoleTileBlockPos = this.tardisData.getMainConsoleTile().getBlockPos();
        this.tardisData.getLevel().playSound(null, consoleTileBlockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void playFlySound() {
        if (this.isSoundFlyPlayed) return;
        this.playSound(ModSounds.TARDIS_FLY.get());
        this.isSoundFlyPlayed = true;
    }
}

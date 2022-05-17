package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TardisSystemFlight implements ITardisSystem {
    private final ITardisLevelData tardisData;
    private boolean isFlightLaunched = false;
    private boolean isSoundFlyPlayed = false;

    public float tickInProgress = 0;
    public float tickInProgressGoal = 0;
    public float destinationDistanceRate = 0;

    public TardisSystemFlight(ITardisLevelData tardisData) {
        this.tardisData = tardisData;
    }

    @Override
    public void load(CompoundTag tag) {
        this.tickInProgress = tag.getFloat("tickInProgress");
        this.tickInProgressGoal = tag.getFloat("tickInProgressGoal");
        this.destinationDistanceRate = tag.getFloat("destinationDistanceRate");
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putFloat("tickInProgress", this.tickInProgress);
        tag.putFloat("tickInProgressGoal", this.tickInProgressGoal);
        tag.putFloat("destinationDistanceRate", this.destinationDistanceRate);

        return tag;
    }

    @Override
    public void tick() {
        if (this.tickInProgress > 0) {
            this.tickInProgress -= this.destinationDistanceRate;

            this.playFlySound();
            if ((int) this.tickInProgress <= 1) this.land();
            if ((int) (this.tickInProgress / this.destinationDistanceRate) % 3 == 0) this.tardisData.updateConsoleTiles();
            if ((int) (this.tickInProgress / this.destinationDistanceRate) % DWM.TIMINGS.FLIGHT_LOOP == 0) this.isSoundFlyPlayed = false;
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
                if (!this.isFlightLaunched) return;

                BlockPos currExteriorPosition = this.tardisData.getCurrentExteriorPosition();
                BlockPos destExteriorPosition = this.tardisData.getDestinationExteriorPosition();
                ResourceKey<Level> currExteriorDimension = this.tardisData.getCurrentExteriorDimension();
                ResourceKey<Level> destExteriorDimension = this.tardisData.getDestinationExteriorDimension();
                float distance = Math.max(1, currExteriorPosition.distManhattan(destExteriorPosition) / 200);
                float timeToFly = DWM.TIMINGS.FLIGHT_LOOP * distance * (currExteriorDimension != destExteriorDimension ? 2 : 1);

                this.isSoundFlyPlayed = false;
                this.tickInProgress = timeToFly;
                this.tickInProgressGoal = timeToFly;
                this.destinationDistanceRate = timeToFly / Math.min(4000, timeToFly);
            });
        }

        return false;
    }

    public boolean land() {
        if (!this.inProgress()) return false;
        this.isFlightLaunched = false;

        if (this.tickInProgress > 1) {
            BlockPos currExteriorPosition = this.tardisData.getCurrentExteriorPosition();
            BlockPos destExteriorPosition = this.tardisData.getDestinationExteriorPosition();
            Vec3 resultPosition = Vec3.atLowerCornerOf(destExteriorPosition.subtract(currExteriorPosition)).scale(this.getProgressPercent() / 100F);
            this.tardisData.setDestinationPosition(currExteriorPosition.offset(resultPosition.x, resultPosition.y, resultPosition.z));
        }

        if (this.tardisData.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization rematSystem) {
            this.isSoundFlyPlayed = false;
            this.tickInProgress = 0;
            this.destinationDistanceRate = 0;
            this.tardisData.getConsoleTiles().forEach((tile) -> tile.controlsStorage.values.put(TardisConsoleControlRoles.STARTER, false));
            this.tardisData.setDimension(this.tardisData.getDestinationExteriorDimension(), true);
            this.tardisData.setFacing(this.tardisData.getDestinationExteriorFacing(), true);
            this.tardisData.setPosition(this.tardisData.getDestinationExteriorPosition(), true);
            this.tardisData.updateConsoleTiles();

            return rematSystem.remat();
        }

        return false;
    }

    private void playFlySound() {
        if (this.isSoundFlyPlayed) return;
        ModSounds.playTardisFlySound(this.tardisData.getLevel(), this.tardisData.getCorePosition());
        this.isSoundFlyPlayed = true;
    }
}

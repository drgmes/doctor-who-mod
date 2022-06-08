package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class TardisSystemFlight implements ITardisSystem {
    private final ITardisLevelData tardis;
    private boolean isFlightLaunched = false;
    private boolean isSoundFlyPlayed = false;

    public float tickInProgress = 0;
    public float tickInProgressGoal = 0;
    public float destinationDistanceRate = 0;

    public TardisSystemFlight(ITardisLevelData tardis) {
        this.tardis = tardis;
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
            if ((int) (this.tickInProgress / this.destinationDistanceRate) % 3 == 0) this.tardis.updateConsoleTiles();
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
            this.tardis.updateConsoleTiles();
        }
    }

    public boolean takeoff() {
        if (this.inProgress()) return false;
        this.isFlightLaunched = true;

        if (this.tardis.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            return materializationSystem.demat(() -> {
                if (!this.isFlightLaunched) return;

                BlockPos currExteriorPosition = this.tardis.getCurrentExteriorPosition();
                BlockPos destExteriorPosition = this.tardis.getDestinationExteriorPosition();
                ResourceKey<Level> currExteriorDimension = this.tardis.getCurrentExteriorDimension();
                ResourceKey<Level> destExteriorDimension = this.tardis.getDestinationExteriorDimension();
                float distance = Math.max(1, currExteriorPosition.distManhattan(destExteriorPosition) / 200);
                float timeToFly = DWM.TIMINGS.FLIGHT_LOOP * distance * (currExteriorDimension != destExteriorDimension ? 2 : 1);

                this.isSoundFlyPlayed = false;
                this.tickInProgress = timeToFly;
                this.tickInProgressGoal = timeToFly;
                this.destinationDistanceRate = timeToFly / Math.min(ModConfig.COMMON.tardisMaxFlightTime.get(), timeToFly);
            });
        }

        return false;
    }

    public boolean land() {
        if (!this.inProgress()) return false;
        this.isFlightLaunched = false;

        if (this.tickInProgress > 1) {
            BlockPos currExteriorPosition = this.tardis.getCurrentExteriorPosition();
            BlockPos destExteriorPosition = this.tardis.getDestinationExteriorPosition();
            Vec3 resultPosition = Vec3.atLowerCornerOf(destExteriorPosition.subtract(currExteriorPosition)).scale(this.getProgressPercent() / 100F);
            this.tardis.setDestinationPosition(currExteriorPosition.offset(resultPosition.x, resultPosition.y, resultPosition.z));
        }

        if (this.tardis.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            this.isSoundFlyPlayed = false;
            this.tickInProgress = 0;
            this.destinationDistanceRate = 0;
            this.tardis.getConsoleTiles().forEach((tile) -> tile.controlsStorage.values.put(TardisConsoleControlRoles.STARTER, false));
            this.tardis.setDimension(this.tardis.getDestinationExteriorDimension(), true);
            this.tardis.setFacing(this.tardis.getDestinationExteriorFacing(), true);
            this.tardis.setPosition(this.tardis.getDestinationExteriorPosition(), true);
            this.tardis.updateConsoleTiles();

            return materializationSystem.remat(() -> {
                this.tardis.updateBoti();
            });
        }

        return false;
    }

    private void playFlySound() {
        if (this.isSoundFlyPlayed) return;
        ModSounds.playTardisFlySound(this.tardis.getLevel(), this.tardis.getCorePosition());
        this.isSoundFlyPlayed = true;
    }
}

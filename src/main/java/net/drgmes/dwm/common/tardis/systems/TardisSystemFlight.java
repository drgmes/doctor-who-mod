package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

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
            if (this.tickInProgress == 1) this.land();
            if (this.tickInProgress % 3 == 0) this.tardisData.updateConsoleTiles();
            if (this.tickInProgress % DWM.TIMINGS.FLIGHT_LOOP == 0) this.isSoundFlyPlayed = false;
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
                float distance = Math.max(1, currExteriorPosition.distManhattan(destExteriorPosition) / 10);
                float timeToFly = Math.min(4000, DWM.TIMINGS.FLIGHT_LOOP * distance) * (currExteriorDimension != destExteriorDimension ? 2 : 1);

                this.isSoundFlyPlayed = false;
                this.tickInProgressGoal = timeToFly;
                this.tickInProgress = this.tickInProgressGoal;
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
        BaseTardisConsoleBlockEntity consoleTile = this.tardisData.getMainConsoleTile();
        BlockPos blockPos = consoleTile != null ? consoleTile.getBlockPos() : this.tardisData.getEntracePosition();
        this.tardisData.getLevel().playSound(null, blockPos, soundEvent, SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    private void playFlySound() {
        if (this.isSoundFlyPlayed) return;
        this.playSound(ModSounds.TARDIS_FLY.get());
        this.isSoundFlyPlayed = true;
    }
}

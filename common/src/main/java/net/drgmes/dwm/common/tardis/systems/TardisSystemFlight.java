package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;

public class TardisSystemFlight implements ITardisSystem {
    private final TardisStateManager tardis;
    private final List<Runnable> failConsumers = new ArrayList<>();

    private boolean isInFlight = false;
    private boolean isLaunched = false;
    private boolean isSoundPlayed = false;
    private float tickInProgress = 0;
    private float tickInProgressGoal = 0;
    private float destinationDistanceRate = 0;

    public TardisSystemFlight(TardisStateManager tardis) {
        this.tardis = tardis;
    }

    @Override
    public boolean isEnabled() {
        return this.tardis.isSystemEnabled(this.getClass());
    }

    @Override
    public boolean inProgress() {
        return this.isInFlight || this.isLaunched || this.tickInProgress > 0;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        if (tag.contains("tickInProgress")) this.tickInProgress = tag.getFloat("tickInProgress");
        if (tag.contains("tickInProgressGoal")) this.tickInProgressGoal = tag.getFloat("tickInProgressGoal");
        if (tag.contains("destinationDistanceRate")) this.destinationDistanceRate = tag.getFloat("destinationDistanceRate");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putFloat("tickInProgress", this.tickInProgress);
        tag.putFloat("tickInProgressGoal", this.tickInProgressGoal);
        tag.putFloat("destinationDistanceRate", this.destinationDistanceRate);

        return tag;
    }

    @Override
    public void tick() {
        if (this.tickInProgress <= 0) return;

        if (!this.tardis.getWorld().isClient && this.tardis.getWorld().getTime() % ModConfig.COMMON.tardisFuelConsumeTiming.get() == 0) {
            int fuelAmount = this.tardis.getFuelAmount();
            int energyAmount = this.tardis.getEnergyAmount();

            if (fuelAmount >= 1) {
                this.tardis.setFuelAmount(fuelAmount - 1);
                this.tardis.markConsoleTilesUpdated();
            }
            else if (energyAmount >= ModConfig.COMMON.tardisFuelToEnergyRating.get()) {
                this.tardis.setEnergyAmount(energyAmount - ModConfig.COMMON.tardisFuelToEnergyRating.get());
                this.tardis.markConsoleTilesUpdated();
            }
            else {
                ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
                this.land();
                return;
            }
        }

        this.tickInProgress -= this.destinationDistanceRate;

        this.playSound();
        if ((int) this.tickInProgress <= 1) this.land();
        if ((int) (this.tickInProgress / this.destinationDistanceRate) % 3 == 0) this.tardis.markConsoleTilesUpdated();
        if ((int) (this.tickInProgress / this.destinationDistanceRate) % DWM.TIMINGS.FLIGHT_LOOP == 0) this.isSoundPlayed = false;
    }

    public int getProgressPercent() {
        return (int) Math.ceil((this.tickInProgressGoal - this.tickInProgress) / this.tickInProgressGoal * 100);
    }

    public boolean setFlight(boolean flag) {
        if (flag ? this.takeoff() : this.land()) {
            this.tardis.markConsoleTilesUpdated();
            return true;
        }

        return false;
    }

    public boolean takeoff() {
        if (!this.isEnabled() || this.inProgress()) return false;

        if (!this.tardis.getSystem(TardisSystemMaterialization.class).isEnabled()) {
            ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        if (this.tardis.getFuelAmount() <= 0 && this.tardis.getEnergyAmount() <= 0) {
            ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        TardisSystemMaterialization materializationSystem = this.tardis.getSystem(TardisSystemMaterialization.class);
        this.isLaunched = true;

        materializationSystem.putCallback((flag) -> {
            if (!flag || !this.isLaunched) return;

            this.tardis.setFuelHarvesting(false);
            this.tardis.setEnergyHarvesting(false);
            this.tardis.markConsoleTilesUpdated();

            float timeToFly = this.getFlightDuration();
            this.isSoundPlayed = false;
            this.isInFlight = true;
            this.tickInProgress = timeToFly;
            this.tickInProgressGoal = timeToFly;
            this.destinationDistanceRate = timeToFly / Math.min(ModConfig.COMMON.tardisMaxFlightTime.get(), timeToFly);
        });

        return materializationSystem.initDemat();
    }

    public boolean land() {
        if (!this.isEnabled() || !this.inProgress()) return false;

        if (!this.tardis.getSystem(TardisSystemMaterialization.class).isEnabled()) {
            ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        boolean isFailed = false;
        this.isLaunched = false;

        if (this.tickInProgress > 1) {
            isFailed = true;
            BlockPos currExteriorPosition = this.tardis.getCurrentExteriorPosition();
            BlockPos destExteriorPosition = this.tardis.getDestinationExteriorPosition();
            Vec3d resultPosition = Vec3d.of(destExteriorPosition.subtract(currExteriorPosition)).multiply(this.getProgressPercent() / 100D);
            this.tardis.setDestinationPosition(currExteriorPosition.add((int) resultPosition.x, (int) resultPosition.y, (int) resultPosition.z));
        }

        this.isSoundPlayed = false;
        this.tickInProgress = 0;
        this.destinationDistanceRate = 0;
        this.tardis.setDimension(this.tardis.getDestinationExteriorDimension(), true);
        this.tardis.setFacing(this.tardis.getDestinationExteriorFacing(), true);
        this.tardis.setPosition(this.tardis.getDestinationExteriorPosition(), true);
        this.tardis.markConsoleTilesUpdated();
        if (!isFailed) this.failConsumers.clear();

        TardisSystemMaterialization materializationSystem = this.tardis.getSystem(TardisSystemMaterialization.class);

        materializationSystem.putCallback((flag) -> {
            this.isInFlight = false;
            this.tardis.markConsoleTilesUpdated();
            this.failConsumers.forEach(Runnable::run);
        });

        return materializationSystem.initRemat();
    }

    public void onFail(Runnable consumer) {
        this.failConsumers.add(consumer);
    }

    public float getFlightDuration() {
        BlockPos currExteriorPosition = this.tardis.getCurrentExteriorPosition();
        BlockPos destExteriorPosition = this.tardis.getDestinationExteriorPosition();
        RegistryKey<World> currExteriorDimension = this.tardis.getCurrentExteriorDimension();
        RegistryKey<World> destExteriorDimension = this.tardis.getDestinationExteriorDimension();
        float distance = Math.max(1, currExteriorPosition.getManhattanDistance(destExteriorPosition) / ModConfig.COMMON.tardisFlightDistanceRate.get());
        return DWM.TIMINGS.FLIGHT_LOOP * distance * (currExteriorDimension != destExteriorDimension ? 2 : 1);
    }

    private void playSound() {
        if (this.isSoundPlayed) return;
        ModSounds.playTardisFlySound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        this.isSoundPlayed = true;
    }
}

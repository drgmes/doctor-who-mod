package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.ModSounds;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;

public class TardisSystemFlight implements ITardisSystem {
    public float tickInProgress = 0;
    public float tickInProgressGoal = 0;
    public float destinationDistanceRate = 0;

    private final TardisStateManager tardis;
    private boolean isInFlight = false;
    private boolean isFlightLaunched = false;
    private boolean isSoundFlyPlayed = false;

    public TardisSystemFlight(TardisStateManager tardis) {
        this.tardis = tardis;
    }

    @Override
    public boolean isEnabled() {
        return this.tardis.isSystemEnabled(this.getClass());
    }

    @Override
    public boolean inProgress() {
        return this.isInFlight || this.isFlightLaunched || this.tickInProgress > 0;
    }

    @Override
    public void load(NbtCompound tag) {
        this.tickInProgress = tag.getFloat("tickInProgress");
        this.tickInProgressGoal = tag.getFloat("tickInProgressGoal");
        this.destinationDistanceRate = tag.getFloat("destinationDistanceRate");
    }

    @Override
    public NbtCompound save() {
        NbtCompound tag = new NbtCompound();

        tag.putFloat("tickInProgress", this.tickInProgress);
        tag.putFloat("tickInProgressGoal", this.tickInProgressGoal);
        tag.putFloat("destinationDistanceRate", this.destinationDistanceRate);

        return tag;
    }

    @Override
    public void tick() {
        if (this.tickInProgress > 0) {
            if (!this.tardis.getWorld().isClient && this.tardis.getWorld().getTime() % 20 == 0 && !Transaction.isOpen()) {
                Transaction transaction = Transaction.openOuter();
                long fuelExtracted = this.tardis.getFuelStorage().extract(1, transaction);

                if (fuelExtracted != 0) {
                    transaction.commit();
                    this.tardis.updateConsoleTiles();
                }
                else {
                    ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
                    transaction.close();
                    this.land();
                    return;
                }
            }

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

    public boolean setFlight(boolean flag) {
        if (flag ? this.takeoff() : this.land()) {
            this.tardis.updateConsoleTiles();
            return true;
        }

        return false;
    }

    public boolean takeoff() {
        if (!this.isEnabled()) return false;
        if (this.inProgress()) return false;

        if (!this.tardis.getSystem(TardisSystemMaterialization.class).isEnabled()) {
            ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        if (this.tardis.getFuelStorage().getAmount() == 0) {
            ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        this.isFlightLaunched = true;

        return this.tardis.getSystem(TardisSystemMaterialization.class).demat(() -> {
            if (!this.isFlightLaunched) return;

            BlockPos currExteriorPosition = this.tardis.getCurrentExteriorPosition();
            BlockPos destExteriorPosition = this.tardis.getDestinationExteriorPosition();
            RegistryKey<World> currExteriorDimension = this.tardis.getCurrentExteriorDimension();
            RegistryKey<World> destExteriorDimension = this.tardis.getDestinationExteriorDimension();
            float distance = Math.max(1, currExteriorPosition.getManhattanDistance(destExteriorPosition) / 200);
            float timeToFly = DWM.TIMINGS.FLIGHT_LOOP * distance * (currExteriorDimension != destExteriorDimension ? 2 : 1);

            this.isSoundFlyPlayed = false;
            this.isInFlight = true;
            this.tickInProgress = timeToFly;
            this.tickInProgressGoal = timeToFly;
            this.destinationDistanceRate = timeToFly / Math.min(ModConfig.COMMON.tardisMaxFlightTime.get(), timeToFly);
        });
    }

    public boolean land() {
        if (!this.isEnabled()) return false;
        if (!this.inProgress()) return false;

        if (!this.tardis.getSystem(TardisSystemMaterialization.class).isEnabled()) {
            ModSounds.playTardisFailSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
            return false;
        }

        this.isFlightLaunched = false;

        if (this.tickInProgress > 1) {
            BlockPos currExteriorPosition = this.tardis.getCurrentExteriorPosition();
            BlockPos destExteriorPosition = this.tardis.getDestinationExteriorPosition();
            Vec3d resultPosition = Vec3d.of(destExteriorPosition.subtract(currExteriorPosition)).multiply(this.getProgressPercent() / 100D);
            this.tardis.setDestinationPosition(currExteriorPosition.add(resultPosition.x, resultPosition.y, resultPosition.z));
        }

        this.isSoundFlyPlayed = false;
        this.tickInProgress = 0;
        this.destinationDistanceRate = 0;
        this.tardis.setDimension(this.tardis.getDestinationExteriorDimension(), true);
        this.tardis.setFacing(this.tardis.getDestinationExteriorFacing(), true);
        this.tardis.setPosition(this.tardis.getDestinationExteriorPosition(), true);
        this.tardis.updateConsoleTiles();

        Runnable defferedConsumer = () -> {
            this.isInFlight = false;
            this.tardis.getConsoleTiles().forEach((tile) -> tile.controlsStorage.values.put(ETardisConsoleUnitControlRole.STARTER, false));
            this.tardis.updateConsoleTiles();
        };

        this.tardis.getSystem(TardisSystemMaterialization.class).onFail(defferedConsumer);
        return this.tardis.getSystem(TardisSystemMaterialization.class).remat(defferedConsumer);
    }

    private void playFlySound() {
        if (this.isSoundFlyPlayed) return;
        ModSounds.playTardisFlySound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        this.isSoundFlyPlayed = true;
    }
}

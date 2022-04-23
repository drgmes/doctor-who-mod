package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.minecraft.nbt.CompoundTag;

public class TardisSystemFlight implements ITardisSystem {
    public int tickInFlight = 0;
    public int lastTicksInFlyGoal = 0;
    private final ITardisLevelData tardisData;

    public TardisSystemFlight(ITardisLevelData tardisData) {
        this.tardisData = tardisData;
    }

    @Override
    public void load(CompoundTag tag) {
        this.tickInFlight = tag.getInt("tickInFlight");
        this.lastTicksInFlyGoal = tag.getInt("lastTicksInFlyGoal");
    }

    @Override
    public CompoundTag save() {
        CompoundTag tag = new CompoundTag();

        tag.putInt("tickInFlight", this.tickInFlight);
        tag.putInt("lastTicksInFlyGoal", this.lastTicksInFlyGoal);

        return tag;
    }

    @Override
    public void tick() {
        if (this.tickInFlight > 0) {
            this.tickInFlight--;

            if (this.tickInFlight == 1) this.stopFligth();
            else this.tardisData.updateConsoleTiles();
        }
    }

    public boolean isInFligth() {
        return this.tickInFlight > 0;
    }

    public void setFlight(boolean flag) {
        if (flag) this.startFligth();
        else this.stopFligth();
    }

    public boolean startFligth() {
        if (this.isInFligth()) return false;

        if (this.tardisData.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization rematSystem) {
            if (!rematSystem.demat()) return false;

            this.lastTicksInFlyGoal = 60;
            this.tickInFlight = this.lastTicksInFlyGoal;
        }

        return false;
    }

    public boolean stopFligth() {
        if (!this.isInFligth()) return false;

        this.tickInFlight = 0;
        this.tardisData.getConsoleTiles().forEach((tile) -> tile.controlsStorage.values.put(TardisConsoleControlRoles.STARTER, false));
        this.tardisData.setDimension(this.tardisData.getDestinationExteriorDimension(), true);
        this.tardisData.setFacing(this.tardisData.getDestinationExteriorFacing(), true);
        this.tardisData.setPosition(this.tardisData.getDestinationExteriorPosition(), true);
        this.tardisData.updateConsoleTiles();

        if (this.tardisData.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization rematSystem) {
            return rematSystem.remat();
        }

        return false;
    }

    public int getFlightPercent() {
        return (int) Math.ceil((this.lastTicksInFlyGoal - this.tickInFlight) / (float) this.lastTicksInFlyGoal * 100);
    }
}

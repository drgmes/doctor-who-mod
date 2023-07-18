package net.drgmes.dwm.forge.common;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import net.minecraftforge.energy.IEnergyStorage;

import java.util.Optional;

public class TardisEnergyStorage implements IEnergyStorage {
    private Optional<TardisStateManager> tardisHolder = Optional.empty();
    private final int maxReceive;
    private final int maxExtract;

    public TardisEnergyStorage(int maxReceive, int maxExtract) {
        this.maxReceive = maxReceive;
        this.maxExtract = maxExtract;
    }

    @Override
    public boolean canExtract() {
        return this.tardisHolder.isPresent();
    }

    @Override
    public boolean canReceive() {
        return this.tardisHolder.map(TardisStateManager::isEnergyHarvesting).orElse(false);
    }

    @Override
    public int getEnergyStored() {
        return this.tardisHolder.map(TardisStateManager::getEnergyAmount).orElse(0);
    }

    @Override
    public int getMaxEnergyStored() {
        return this.tardisHolder.map(TardisStateManager::getEnergyCapacity).orElse(0);
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (!this.canReceive()) return 0;

        int energy = this.getEnergyStored();
        int energyReceived = Math.min(this.getMaxEnergyStored() - energy, Math.min(this.maxReceive, maxReceive));
        if (!simulate) this.updateEnergyAmount(energy + energyReceived);
        return energyReceived;
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        if (!this.canExtract()) return 0;

        int energy = this.getEnergyStored();
        int energyExtracted = Math.min(this.getEnergyStored(), Math.min(this.maxExtract, maxExtract));
        if (!simulate) this.updateEnergyAmount(energy - energyExtracted);
        return energyExtracted;
    }

    public void refreshTardisStateManager(World world) {
        if (!(world instanceof ServerWorld serverWorld)) return;
        this.tardisHolder = TardisStateManager.get(serverWorld);
    }

    private void updateEnergyAmount(int energy) {
        if (this.tardisHolder.isEmpty()) return;
        this.tardisHolder.get().setEnergyAmount(energy);
        this.tardisHolder.get().updateConsoleTiles();
    }
}

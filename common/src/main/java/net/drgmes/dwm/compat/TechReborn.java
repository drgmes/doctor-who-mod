package net.drgmes.dwm.compat;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TechReborn {
    private static final Map<String, EnergyStorage> energyStorages = new HashMap<>();

    public static void registerExternalTardisEnergyStorage(BlockEntityType<?> blockEntityType) {
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
            if (!(blockEntity instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity)) return null;
            ServerWorld tardisWorld = tardisExteriorBlockEntity.getTardisWorld(true);
            return TechReborn.getOrCreateTardisEnergyStorage(tardisWorld);
        }, blockEntityType);
    }

    public static void registerInternalTardisEnergyStorage(BlockEntityType<?> blockEntityType) {
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
            if (!(blockEntity.getWorld() instanceof ServerWorld serverWorld)) return null;
            return TechReborn.getOrCreateTardisEnergyStorage(serverWorld);
        }, blockEntityType);
    }

    public static EnergyStorage getOrCreateTardisEnergyStorage(ServerWorld world) {
        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(world);
        if (tardisHolder.isEmpty()) return null;

        String tardisId = DimensionHelper.getWorldId(world);
        if (!energyStorages.containsKey(tardisId)) energyStorages.put(tardisId, new TardisEnergyStorage(tardisHolder.get()));
        return energyStorages.get(tardisId);
    }

    public static void removeTardisEnergyStorage(String tardisId) {
        if (!energyStorages.containsKey(tardisId)) return;
        energyStorages.remove(tardisId);
    }

    public static void clearTardisEnergyStorage() {
        energyStorages.clear();
    }

    private static class TardisEnergyStorage extends SimpleEnergyStorage {
        private final TardisStateManager tardis;

        public TardisEnergyStorage(TardisStateManager tardis) {
            super(tardis.getEnergyCapacity(), Long.MAX_VALUE, Long.MAX_VALUE);

            this.tardis = tardis;
            this.amount = tardis.getEnergyAmount();
        }

        @Override
        @SuppressWarnings("UnstableApiUsage")
        public long insert(long maxAmount, TransactionContext transaction) {
            return this.tardis.isEnergyHarvesting() ? super.insert(maxAmount, transaction) : 0;
        }

        @Override
        @SuppressWarnings("UnstableApiUsage")
        protected void onFinalCommit() {
            tardis.setEnergyAmount((int) this.amount);
            tardis.updateConsoleTiles();
        }
    }
}

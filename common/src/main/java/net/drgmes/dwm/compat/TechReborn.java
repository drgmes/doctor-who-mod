package net.drgmes.dwm.compat;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.fabricmc.fabric.api.transfer.v1.transaction.TransactionContext;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.World;
import team.reborn.energy.api.EnergyStorage;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TechReborn {
    private static final Map<String, EnergyStorage> ENERGY_STORAGES = new HashMap<>();

    public static void registerTardisEnergyStorage(BlockEntityType<?> blockEntityType) {
        EnergyStorage.SIDED.registerForBlockEntity((blockEntity, direction) -> {
            World world = blockEntity.getWorld();

            if (blockEntity instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
                world = tardisExteriorBlockEntity.getOrCreateTardisWorld();
            }

            return TechReborn.getOrCreateTardisEnergyStorage(world);
        }, blockEntityType);
    }

    public static EnergyStorage getOrCreateTardisEnergyStorage(World world) {
        if (!(world instanceof ServerWorld serverWorld)) return null;

        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(serverWorld);
        String tardisId = DimensionHelper.getWorldId(serverWorld);
        if (tardisHolder.isEmpty()) return null;

        if (!ENERGY_STORAGES.containsKey(tardisId)) ENERGY_STORAGES.put(tardisId, new TardisEnergyStorage(tardisHolder.get()));
        return ENERGY_STORAGES.get(tardisId);
    }

    public static void removeTardisEnergyStorage(String tardisId) {
        ENERGY_STORAGES.remove(tardisId);
    }

    public static void clearTardisEnergyStorages() {
        ENERGY_STORAGES.clear();
    }

    private static class TardisEnergyStorage extends SimpleEnergyStorage {
        private final TardisStateManager tardis;

        public TardisEnergyStorage(TardisStateManager tardis) {
            super(tardis.getEnergyCapacity(), Long.MAX_VALUE, Long.MAX_VALUE);

            this.tardis = tardis;
            this.amount = tardis.getEnergyAmount();
        }

        @Override
        public boolean supportsInsertion() {
            return super.supportsInsertion() && this.tardis.isEnergyHarvesting();
        }

        @Override
        @SuppressWarnings("UnstableApiUsage")
        public long insert(long maxAmount, TransactionContext transaction) {
            return this.supportsInsertion() ? super.insert(maxAmount, transaction) : 0;
        }

        @Override
        @SuppressWarnings("UnstableApiUsage")
        public long extract(long maxAmount, TransactionContext transaction) {
            return this.supportsExtraction() ? super.extract(maxAmount, transaction) : 0;
        }

        @Override
        @SuppressWarnings("UnstableApiUsage")
        protected void onFinalCommit() {
            this.tardis.setEnergyAmount((int) this.amount);
            this.tardis.updateConsoleTiles();
        }
    }
}

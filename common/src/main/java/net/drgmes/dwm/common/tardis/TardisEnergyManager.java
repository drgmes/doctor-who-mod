package net.drgmes.dwm.common.tardis;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.drgmes.dwm.compat.TechReborn;
import net.drgmes.dwm.setup.ModCompats;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class TardisEnergyManager {
    @ExpectPlatform
    public static boolean hasEnergyApi() {
        throw new AssertionError();
    }

    public static void register(Supplier<BlockEntityType<?>> blockEntityTypeSupplier) {
        if (ModCompats.techReborn()) TechReborn.registerTardisEnergyStorage(blockEntityTypeSupplier.get());
    }

    public static void remove(String tardisId) {
        if (ModCompats.techReborn()) TechReborn.removeTardisEnergyStorage(tardisId);
    }

    public static void clear() {
        if (ModCompats.techReborn()) TechReborn.clearTardisEnergyStorages();
    }
}

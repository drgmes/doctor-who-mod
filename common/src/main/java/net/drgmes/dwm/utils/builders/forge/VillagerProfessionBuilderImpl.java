package net.drgmes.dwm.utils.builders.forge;

import com.google.common.collect.ImmutableSet;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.function.Supplier;

public class VillagerProfessionBuilderImpl {
    public static Supplier<PointOfInterestType> registerPoiType(String name, int ticketCount, int searchDistance, Supplier<ImmutableSet<BlockState>> blockStatesSupplier) {
        return Registration.registerPoiType(name, () -> new PointOfInterestType(blockStatesSupplier.get(), ticketCount, searchDistance));
    }
}

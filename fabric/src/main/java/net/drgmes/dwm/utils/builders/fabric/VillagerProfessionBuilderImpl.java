package net.drgmes.dwm.utils.builders.fabric;

import com.google.common.collect.ImmutableSet;
import net.drgmes.dwm.DWM;
import net.fabricmc.fabric.api.object.builder.v1.world.poi.PointOfInterestHelper;
import net.minecraft.block.BlockState;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.function.Supplier;

public class VillagerProfessionBuilderImpl {
    public static Supplier<PointOfInterestType> registerPoiType(String name, int ticketCount, int searchDistance, Supplier<ImmutableSet<BlockState>> blockStatesSupplier) {
        PointOfInterestType poiType = PointOfInterestHelper.register(DWM.getIdentifier(name), ticketCount, searchDistance, blockStatesSupplier.get());
        return () -> poiType;
    }
}

package net.drgmes.dwm.common.tardis.consoleunits;

import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleUnitControlEntry;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.enums.TardisConsoleUnitControlRole;
import net.drgmes.dwm.enums.TardisConsoleUnitControlType;
import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.utils.builders.BlockEntityBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class TardisConsoleUnitEntry {
    public final Map<TardisConsoleUnitControlRole, TardisConsoleUnitControlEntry> controlEntries = new HashMap<>();

    public final String name;
    public final Supplier<BlockEntityBuilder<?>> blockEntityBuilderSupplier;

    public TardisConsoleUnitEntry(String name, Supplier<BlockEntityBuilder<?>> blockEntityBuilderSupplier) {
        this.name = name;
        this.blockEntityBuilderSupplier = blockEntityBuilderSupplier;

        TardisConsoleUnits.TYPES.put(name, this);
    }

    public BlockEntityType<?> getBlockEntityType() {
        return this.blockEntityBuilderSupplier.get().getBlockEntityType();
    }

    public TardisConsoleUnitControlEntry addControlEntry(TardisConsoleUnitControlRole controlRole, TardisConsoleUnitControlType controlType, Vec3d position, String modelPath, TardisConsoleControlEntityBuilder builder) {
        return this.controlEntries.put(controlRole, new TardisConsoleUnitControlEntry(controlRole, controlType, position, modelPath, builder));
    }

    public TardisConsoleUnitControlEntry addControlEntry(TardisConsoleUnitControlRole controlRole, TardisConsoleUnitControlType controlType, Vec3d position, String modelPath) {
        return this.addControlEntry(controlRole, controlType, position, modelPath, ModEntities.TARDIS_CONSOLE_UNIT_CONTROL);
    }
}

package net.drgmes.dwm.common.tardis.consoleunits;

import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlEntry;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleUnitTypeEntry {
    public final Map<ETardisConsoleUnitControlRole, TardisConsoleControlEntry> controlEntries = new HashMap<>();
    public final String name;

    public TardisConsoleUnitTypeEntry(String name) {
        this.name = name;

        TardisConsoleUnitTypes.CONSOLE_TYPES.put(name, this);
    }

    public TardisConsoleControlEntry addControlEntry(ETardisConsoleUnitControlRole controlRole, ETardisConsoleUnitControlEntry controlType, Vec3d position, String modelPath, TardisConsoleControlEntityBuilder builder) {
        return this.controlEntries.put(controlRole, new TardisConsoleControlEntry(controlRole, controlType, position, modelPath, builder));
    }

    public TardisConsoleControlEntry addControlEntry(ETardisConsoleUnitControlRole controlRole, ETardisConsoleUnitControlEntry controlType, Vec3d position, String modelPath) {
        return this.addControlEntry(controlRole, controlType, position, modelPath, ModEntities.TARDIS_CONSOLE_UNIT_CONTROL);
    }
}

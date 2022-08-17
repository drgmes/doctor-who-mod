package net.drgmes.dwm.common.tardis.consoles;

import net.drgmes.dwm.common.tardis.consoles.controls.ETardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.ETardisConsoleControlRole;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.util.math.Vec3d;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleType {
    public static final TardisConsoleType TOYOTA = new TardisConsoleTypeToyota();

    public final Map<ETardisConsoleControlRole, TardisConsoleControlEntry> controlEntries = new HashMap<>();

    public TardisConsoleControlEntry addControlEntry(ETardisConsoleControlRole controlRole, ETardisConsoleControlEntry controlType, Vec3d position, String modelPath, TardisConsoleControlEntityBuilder builder) {
        return this.controlEntries.put(controlRole, new TardisConsoleControlEntry(controlRole, controlType, position, modelPath, builder));
    }

    public TardisConsoleControlEntry addControlEntry(ETardisConsoleControlRole controlRole, ETardisConsoleControlEntry controlType, Vec3d position, String modelPath) {
        return this.addControlEntry(controlRole, controlType, position, modelPath, ModEntities.TARDIS_CONSOLE_CONTROL);
    }
}

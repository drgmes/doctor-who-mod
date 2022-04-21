package net.drgmes.dwm.common.tardis;

import java.util.HashMap;
import java.util.Map;

import net.drgmes.dwm.common.tardis.consoles.TardisConsoleTypeToyota;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.world.phys.Vec3;

public class TardisConsoleType {
    public static final TardisConsoleType TOYOTA = new TardisConsoleTypeToyota();

    public final Map<TardisConsoleControlRoles, TardisConsoleControlEntry> controlEntries = new HashMap<>();

    public TardisConsoleControlEntry addControlEntry(TardisConsoleControlRoles controlRole, TardisConsoleControlEntryTypes controlType, Vec3 position, String modelPath, TardisConsoleControlEntityBuilder builder) {
        return this.controlEntries.put(controlRole, new TardisConsoleControlEntry(controlRole, controlType, position, modelPath, builder));
    }

    public TardisConsoleControlEntry addControlEntry(TardisConsoleControlRoles controlRole, TardisConsoleControlEntryTypes controlType, Vec3 position, String modelPath) {
        return this.addControlEntry(controlRole, controlType, position, modelPath, ModEntities.TARDIS_CONSOLE_CONTROL);
    }
}

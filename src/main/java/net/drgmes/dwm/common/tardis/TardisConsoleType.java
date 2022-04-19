package net.drgmes.dwm.common.tardis;

import java.util.ArrayList;
import java.util.List;

import net.drgmes.dwm.common.tardis.consoles.TardisConsoleTypeToyota;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControl;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.entities.tardis.consolecontrol.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.world.phys.Vec3;

public class TardisConsoleType {
    public static final TardisConsoleType TOYOTA = new TardisConsoleTypeToyota();

    public final List<TardisConsoleControl> controls = new ArrayList<>();

    public TardisConsoleControl addControl(TardisConsoleControlRoles role, Vec3 position, TardisConsoleControlEntityBuilder builder) {
        TardisConsoleControl control = new TardisConsoleControl(role, position, builder);
        this.controls.add(control);
        return control;
    }

    public TardisConsoleControl addControl(TardisConsoleControlRoles role, Vec3 position) {
        return this.addControl(role, position, ModEntities.TARDIS_CONSOLE_CONTROL);
    }
}

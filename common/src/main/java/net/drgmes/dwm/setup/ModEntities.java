package net.drgmes.dwm.setup;

import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.utils.builders.EntityBuilder;

import java.util.ArrayList;
import java.util.List;

public class ModEntities {
    public static final List<EntityBuilder<?>> ENTITY_BUILDERS = new ArrayList<>();

    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_UNIT_CONTROL = new TardisConsoleControlEntityBuilder("tardis_console_unit_control");
    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_UNIT_CONTROL_SMALL = new TardisConsoleControlEntityBuilder("tardis_console_unit_control_small", 0.05F);
    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_UNIT_CONTROL_MEDIUM = new TardisConsoleControlEntityBuilder("tardis_console_unit_control_medium", 0.55F, 0.1F);
    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_UNIT_CONTROL_LARGE = new TardisConsoleControlEntityBuilder("tardis_console_unit_control_large", 0.6F, 0.25F);

    public static void init() {
    }
}

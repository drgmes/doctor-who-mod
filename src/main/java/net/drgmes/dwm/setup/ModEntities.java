package net.drgmes.dwm.setup;

import java.util.ArrayList;

import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.utils.builders.entity.EntityBuilder;

public class ModEntities {
    public static final ArrayList<EntityBuilder<?>> ENTITY_BUILDERS = new ArrayList<>();

    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_CONTROL = new TardisConsoleControlEntityBuilder("tardis_console_control");
    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_CONTROL_SMALL = new TardisConsoleControlEntityBuilder("tardis_console_control_small", 0.05F);
    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_CONTROL_MEDIUM = new TardisConsoleControlEntityBuilder("tardis_console_control_medium", 0.55F, 0.1F);
    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_CONTROL_LARGE = new TardisConsoleControlEntityBuilder("tardis_console_control_large", 0.6F, 0.25F);

    public static void init() {
    }
}

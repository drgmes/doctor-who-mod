package net.drgmes.dwm.setup;

import java.util.ArrayList;

import net.drgmes.dwm.entities.tardis.consolecontrol.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.utils.builders.entity.EntityBuilder;

public class ModEntities {
    public static final ArrayList<EntityBuilder<?>> ENTITY_BUILDERS = new ArrayList<>();

    public static final TardisConsoleControlEntityBuilder TARDIS_CONSOLE_CONTROL = new TardisConsoleControlEntityBuilder("tardis_console_control");

    public static void init() {
    }
}

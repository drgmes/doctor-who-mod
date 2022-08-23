package net.drgmes.dwm.common.tardis.consoles;

import net.drgmes.dwm.common.tardis.consoles.controls.ETardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.ETardisConsoleControlRole;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.util.math.Vec3d;

public class TardisConsoleTypeEntryImperial extends TardisConsoleTypeEntry {
    public TardisConsoleTypeEntryImperial(String name) {
        super(name);

        this.addControlEntry(ETardisConsoleControlRole.MONITOR, ETardisConsoleControlEntry.OTHER, new Vec3d(-0.4F, 0.5F, -0.7F), "controls/control_monitor", ModEntities.TARDIS_CONSOLE_CONTROL_MEDIUM);
        this.addControlEntry(ETardisConsoleControlRole.TELEPATHIC_INTERFACE, ETardisConsoleControlEntry.OTHER, new Vec3d(0.45F, 0.4F, -0.75F), "controls/control_telepathic_interface", ModEntities.TARDIS_CONSOLE_CONTROL_LARGE);
        this.addControlEntry(ETardisConsoleControlRole.SCREWDRIVER_SLOT, ETardisConsoleControlEntry.OTHER, new Vec3d(0.535F, 0.55F, 0.5425F), "screwdriver_slot");

        this.addControlEntry(ETardisConsoleControlRole.MATERIALIZATION, ETardisConsoleControlEntry.SLIDER, new Vec3d(0.855F, 0.575F, -0.0775F), "controls/control_sliders_v/control_slider_v_3$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.MONITOR_PAGE_PREV, ETardisConsoleControlEntry.BUTTON, new Vec3d(-0.1315F, 0.6F, -0.735F), "controls/control_buttons/control_button_7$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.MONITOR_PAGE_NEXT, ETardisConsoleControlEntry.BUTTON, new Vec3d(-0.1875F, 0.6F, -0.705F), "controls/control_buttons/control_button_8$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.DIM_PREV, ETardisConsoleControlEntry.BUTTON, new Vec3d(-0.4975F, 0.6F, -0.5295F), "controls/control_buttons/control_button_9$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.DIM_NEXT, ETardisConsoleControlEntry.BUTTON, new Vec3d(-0.5515F, 0.6F, -0.5F), "controls/control_buttons/control_button_10$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.RESET_TO_PREV, ETardisConsoleControlEntry.BUTTON, new Vec3d(-0.7575F, 0.495F, -0.8175F), "controls/control_buttons/control_button_14$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.RESET_TO_CURR, ETardisConsoleControlEntry.BUTTON, new Vec3d(-0.8095F, 0.495F, -0.7875F), "controls/control_buttons/control_button_15$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.ENERGY_ARTRON_HARVESTING, ETardisConsoleControlEntry.BUTTON, new Vec3d(-1.005F, 0.505F, -0.3585F), "controls/control_buttons/control_button_17$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.ENERGY_FORGE_HARVESTING, ETardisConsoleControlEntry.BUTTON, new Vec3d(-1.005F, 0.505F, -0.295F), "controls/control_buttons/control_button_18$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);

        this.addControlEntry(ETardisConsoleControlRole.XSET, ETardisConsoleControlEntry.LEVER, new Vec3d(0.725F, 0.605F, 0.0875F), "controls/control_levers/control_lever_12$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.YSET, ETardisConsoleControlEntry.LEVER, new Vec3d(0.725F, 0.605F, 0), "controls/control_levers/control_lever_13$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.ZSET, ETardisConsoleControlEntry.LEVER, new Vec3d(0.725F, 0.605F, -0.0875F), "controls/control_levers/control_lever_14$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleControlRole.XYZSTEP, ETardisConsoleControlEntry.LEVER, new Vec3d(0.725F, 0.605F, 0.175F), "controls/control_levers/control_lever_11$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);

        this.addControlEntry(ETardisConsoleControlRole.SHIELDS, ETardisConsoleControlEntry.LEVER, new Vec3d(-0.895F, 0.565F, -0.3255F), "controls/control_levers/control_lever_6$_handle");
        this.addControlEntry(ETardisConsoleControlRole.LIGHT, ETardisConsoleControlEntry.LEVER, new Vec3d(-0.725F, 0.605F, -0.175F), "controls/control_levers/control_lever_1$_handle");
        this.addControlEntry(ETardisConsoleControlRole.DOORS, ETardisConsoleControlEntry.LEVER, new Vec3d(-0.895F, 0.565F, 0.3255F), "controls/control_levers/control_lever_7$_handle");
        this.addControlEntry(ETardisConsoleControlRole.SAFE_DIRECTION, ETardisConsoleControlEntry.LEVER, new Vec3d(0.725F, 0.605F, -0.175F), "controls/control_levers/control_lever_15$_handle");

        this.addControlEntry(ETardisConsoleControlRole.RANDOMIZER, ETardisConsoleControlEntry.ROTATOR, new Vec3d(-0.1565F, 0.45F, -1.1695F), "controls/control_rotators/control_rotator_6$_handle");
        this.addControlEntry(ETardisConsoleControlRole.FACING, ETardisConsoleControlEntry.ROTATOR, new Vec3d(-0.925F, 0.45F, -0.7245F), "controls/control_rotators/control_rotator_7$_handle");

        this.addControlEntry(ETardisConsoleControlRole.STARTER, ETardisConsoleControlEntry.OTHER, new Vec3d(1.05F, 0.495F, -0.335F), "controls/control_starter$_handle");
        this.addControlEntry(ETardisConsoleControlRole.HANDBRAKE, ETardisConsoleControlEntry.OTHER, new Vec3d(1.05F, 0.495F, 0.335F), "controls/control_handbrake$_handle");
    }
}

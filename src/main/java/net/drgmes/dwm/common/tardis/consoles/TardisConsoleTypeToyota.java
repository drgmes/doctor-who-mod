package net.drgmes.dwm.common.tardis.consoles;

import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.minecraft.world.phys.Vec3;

public class TardisConsoleTypeToyota extends TardisConsoleType {
    public TardisConsoleTypeToyota() {
        this.addControlEntry(TardisConsoleControlRoles.MONITOR, TardisConsoleControlEntryTypes.OTHER, new Vec3(-0.4F, 0.5F, -0.7F), "controls/control_monitor", ModEntities.TARDIS_CONSOLE_CONTROL_MEDIUM);
        this.addControlEntry(TardisConsoleControlRoles.TELEPATIC_INTERFACE, TardisConsoleControlEntryTypes.OTHER, new Vec3(0.45F, 0.4F, -0.75F), "controls/control_telepatic_interface", ModEntities.TARDIS_CONSOLE_CONTROL_LARGE);

        this.addControlEntry(TardisConsoleControlRoles.MATERIALIZATION, TardisConsoleControlEntryTypes.SLIDER, new Vec3(0.855F, 0.575F, -0.0775F), "controls/control_sliders_v/control_slider_v_3$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);

        this.addControlEntry(TardisConsoleControlRoles.MONITOR_PAGE_PREV, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.1315F, 0.6F, -0.735F), "controls/control_buttons/control_button_7$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.MONITOR_PAGE_NEXT, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.1875F, 0.6F, -0.705F), "controls/control_buttons/control_button_8$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.DIM_PREV, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.4975F, 0.6F, -0.5295F), "controls/control_buttons/control_button_9$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.DIM_NEXT, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.5515F, 0.6F, -0.5F), "controls/control_buttons/control_button_10$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.RESET_TO_PREV, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.7575F, 0.495F, -0.8175F), "controls/control_buttons/control_button_14$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.RESET_TO_CURR, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.8095F, 0.495F, -0.7875F), "controls/control_buttons/control_button_15$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.ENERGY_FORGE_HARVESTING, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-1.005F, 0.505F, -0.3585F), "controls/control_buttons/control_button_17$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.ENERGY_ARTRON_HARVESTING, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-1.005F, 0.505F, -0.295F), "controls/control_buttons/control_button_18$_r1", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);

        this.addControlEntry(TardisConsoleControlRoles.XSET, TardisConsoleControlEntryTypes.LEVER, new Vec3(0.725F, 0.605F, 0.0875F), "controls/control_levers/control_lever_12$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.YSET, TardisConsoleControlEntryTypes.LEVER, new Vec3(0.725F, 0.605F, 0), "controls/control_levers/control_lever_13$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.ZSET, TardisConsoleControlEntryTypes.LEVER, new Vec3(0.725F, 0.605F, -0.0875F), "controls/control_levers/control_lever_14$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.XYZSTEP, TardisConsoleControlEntryTypes.LEVER, new Vec3(0.725F, 0.605F, 0.175F), "controls/control_levers/control_lever_11$_handle", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);

        this.addControlEntry(TardisConsoleControlRoles.SHIELDS, TardisConsoleControlEntryTypes.LEVER, new Vec3(-0.895F, 0.565F, -0.3255F), "controls/control_levers/control_lever_6$_handle");
        this.addControlEntry(TardisConsoleControlRoles.LIGHT, TardisConsoleControlEntryTypes.LEVER, new Vec3(-0.725F, 0.605F, -0.175F), "controls/control_levers/control_lever_1$_handle");
        this.addControlEntry(TardisConsoleControlRoles.DOORS, TardisConsoleControlEntryTypes.LEVER, new Vec3(-0.895F, 0.565F, 0.3255F), "controls/control_levers/control_lever_7$_handle");
        this.addControlEntry(TardisConsoleControlRoles.SAFE_DIRECTION, TardisConsoleControlEntryTypes.LEVER, new Vec3(0.725F, 0.605F, -0.175F), "controls/control_levers/control_lever_15$_handle");

        this.addControlEntry(TardisConsoleControlRoles.RANDOMIZER, TardisConsoleControlEntryTypes.ROTATOR, new Vec3(-0.1565F, 0.45F, -1.1695F), "controls/control_rotators/control_rotator_6$_handle");
        this.addControlEntry(TardisConsoleControlRoles.FACING, TardisConsoleControlEntryTypes.ROTATOR, new Vec3(-0.925F, 0.45F, -0.7245F), "controls/control_rotators/control_rotator_7$_handle");

        this.addControlEntry(TardisConsoleControlRoles.STARTER, TardisConsoleControlEntryTypes.OTHER, new Vec3(1.05F, 0.495F, -0.335F), "controls/control_starter$_handle");
        this.addControlEntry(TardisConsoleControlRoles.HANDBRAKE, TardisConsoleControlEntryTypes.OTHER, new Vec3(1.05F, 0.495F, 0.335F), "controls/control_handbrake$_handle");
    }
}

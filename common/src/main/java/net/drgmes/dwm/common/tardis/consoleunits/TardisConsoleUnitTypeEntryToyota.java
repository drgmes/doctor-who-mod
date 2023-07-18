package net.drgmes.dwm.common.tardis.consoleunits;

import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlEntry;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.util.math.Vec3d;

public class TardisConsoleUnitTypeEntryToyota extends TardisConsoleUnitTypeEntry {
    public TardisConsoleUnitTypeEntryToyota() {
        super("toyota");

        this.addControlEntry(ETardisConsoleUnitControlRole.MONITOR, ETardisConsoleUnitControlEntry.OTHER, new Vec3d(-0.4F, 0.5F, -0.7F), "controls/control_monitor", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_MEDIUM);
        this.addControlEntry(ETardisConsoleUnitControlRole.TELEPATHIC_INTERFACE, ETardisConsoleUnitControlEntry.OTHER, new Vec3d(0.45F, 0.4F, -0.75F), "controls/control_telepathic_interface", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_LARGE);
        this.addControlEntry(ETardisConsoleUnitControlRole.SONIC_SCREWDRIVER_SLOT, ETardisConsoleUnitControlEntry.OTHER, new Vec3d(0.535F, 0.55F, 0.5425F), "sonic_screwdriver_slot");

        this.addControlEntry(ETardisConsoleUnitControlRole.MATERIALIZATION, ETardisConsoleUnitControlEntry.SLIDER, new Vec3d(0.855F, 0.575F, -0.0775F), "controls/control_sliders_v/control_slider_v_3$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.MONITOR_PAGE_PREV, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.1315F, 0.6F, -0.735F), "controls/control_buttons/control_button_7$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.MONITOR_PAGE_NEXT, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.1875F, 0.6F, -0.705F), "controls/control_buttons/control_button_8$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.DIM_PREV, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.4975F, 0.6F, -0.5295F), "controls/control_buttons/control_button_9$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.DIM_NEXT, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.5515F, 0.6F, -0.5F), "controls/control_buttons/control_button_10$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.RESET_TO_PREV, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.7575F, 0.495F, -0.8175F), "controls/control_buttons/control_button_14$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.RESET_TO_CURR, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.8095F, 0.495F, -0.7875F), "controls/control_buttons/control_button_15$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.FUEL_HARVESTING, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-1.005F, 0.505F, -0.3585F), "controls/control_buttons/control_button_17$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.ENERGY_HARVESTING, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-1.005F, 0.505F, -0.295F), "controls/control_buttons/control_button_18$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.SHIELDS_OXYGEN, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.95F, 0.5175F, -0.1575F), "controls/control_buttons/control_button_1$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.SHIELDS_FIRE_PROOF, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.95F, 0.5175F, -0.094F), "controls/control_buttons/control_button_2$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.SHIELDS_MEDICAL, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.95F, 0.5175F, -0.03F), "controls/control_buttons/control_button_3$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.SHIELDS_MINING, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.95F, 0.5175F, 0.0315F), "controls/control_buttons/control_button_4$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.SHIELDS_GRAVITATION, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.95F, 0.5175F, 0.0955F), "controls/control_buttons/control_button_5$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.SHIELDS_SPECIAL, ETardisConsoleUnitControlEntry.BUTTON, new Vec3d(-0.95F, 0.5175F, 0.155F), "controls/control_buttons/control_button_6$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);

        this.addControlEntry(ETardisConsoleUnitControlRole.XSET, ETardisConsoleUnitControlEntry.LEVER, new Vec3d(0.725F, 0.605F, 0.0875F), "controls/control_levers/control_lever_12$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.YSET, ETardisConsoleUnitControlEntry.LEVER, new Vec3d(0.725F, 0.605F, 0), "controls/control_levers/control_lever_13$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.ZSET, ETardisConsoleUnitControlEntry.LEVER, new Vec3d(0.725F, 0.605F, -0.0875F), "controls/control_levers/control_lever_14$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.XYZSTEP, ETardisConsoleUnitControlEntry.LEVER, new Vec3d(0.725F, 0.605F, 0.175F), "controls/control_levers/control_lever_11$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);

        this.addControlEntry(ETardisConsoleUnitControlRole.SHIELDS, ETardisConsoleUnitControlEntry.LEVER, new Vec3d(-0.895F, 0.565F, -0.3255F), "controls/control_levers/control_lever_6$_handle");
        this.addControlEntry(ETardisConsoleUnitControlRole.DOORS, ETardisConsoleUnitControlEntry.LEVER, new Vec3d(-0.895F, 0.565F, 0.3255F), "controls/control_levers/control_lever_7$_handle");
        this.addControlEntry(ETardisConsoleUnitControlRole.LIGHT, ETardisConsoleUnitControlEntry.LEVER, new Vec3d(-0.725F, 0.605F, -0.175F), "controls/control_levers/control_lever_1$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(ETardisConsoleUnitControlRole.SAFE_DIRECTION, ETardisConsoleUnitControlEntry.LEVER, new Vec3d(0.725F, 0.605F, -0.175F), "controls/control_levers/control_lever_15$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);

        this.addControlEntry(ETardisConsoleUnitControlRole.RANDOMIZER, ETardisConsoleUnitControlEntry.ROTATOR, new Vec3d(-0.1565F, 0.45F, -1.1695F), "controls/control_rotators/control_rotator_6$_handle");
        this.addControlEntry(ETardisConsoleUnitControlRole.FACING, ETardisConsoleUnitControlEntry.ROTATOR, new Vec3d(-0.925F, 0.45F, -0.7245F), "controls/control_rotators/control_rotator_7$_handle");

        this.addControlEntry(ETardisConsoleUnitControlRole.STARTER, ETardisConsoleUnitControlEntry.OTHER, new Vec3d(1.05F, 0.495F, -0.335F), "controls/control_starter$_handle");
        this.addControlEntry(ETardisConsoleUnitControlRole.HANDBRAKE, ETardisConsoleUnitControlEntry.OTHER, new Vec3d(1.05F, 0.495F, 0.335F), "controls/control_handbrake$_handle");
    }
}

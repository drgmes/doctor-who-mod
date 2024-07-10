package net.drgmes.dwm.common.tardis.consoleunits.types;

import net.drgmes.dwm.common.tardis.consoleunits.TardisConsoleUnitEntry;
import net.drgmes.dwm.enums.TardisConsoleUnitControlRole;
import net.drgmes.dwm.enums.TardisConsoleUnitControlType;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModEntities;
import net.minecraft.util.math.Vec3d;

public class TardisConsoleUnitImperial extends TardisConsoleUnitEntry {
    public TardisConsoleUnitImperial() {
        super("imperial", () -> ModBlockEntities.TARDIS_CONSOLE_UNIT_IMPERIAL);

        this.addControlEntry(TardisConsoleUnitControlRole.MONITOR, TardisConsoleUnitControlType.OTHER, new Vec3d(-0.4F, 0.5F, -0.7F), "controls/control_monitor", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_MEDIUM);
        this.addControlEntry(TardisConsoleUnitControlRole.TELEPATHIC_INTERFACE, TardisConsoleUnitControlType.OTHER, new Vec3d(0.45F, 0.4F, -0.75F), "controls/control_telepathic_interface", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_LARGE);
        this.addControlEntry(TardisConsoleUnitControlRole.SONIC_SCREWDRIVER_SLOT, TardisConsoleUnitControlType.OTHER, new Vec3d(0.535F, 0.55F, 0.5425F), "sonic_screwdriver_slot");

        this.addControlEntry(TardisConsoleUnitControlRole.MATERIALIZATION, TardisConsoleUnitControlType.SLIDER, new Vec3d(0.855F, 0.575F, -0.0775F), "controls/control_sliders_v/control_slider_v_3$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.MONITOR_PAGE_PREV, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.1315F, 0.6F, -0.735F), "controls/control_buttons/control_button_7$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.MONITOR_PAGE_NEXT, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.1875F, 0.6F, -0.705F), "controls/control_buttons/control_button_8$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.DIM_PREV, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.4975F, 0.6F, -0.5295F), "controls/control_buttons/control_button_9$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.DIM_NEXT, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.5515F, 0.6F, -0.5F), "controls/control_buttons/control_button_10$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.RESET_TO_PREV, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.7575F, 0.495F, -0.8175F), "controls/control_buttons/control_button_14$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.RESET_TO_CURR, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.8095F, 0.495F, -0.7875F), "controls/control_buttons/control_button_15$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.FUEL_HARVESTING, TardisConsoleUnitControlType.BUTTON, new Vec3d(-1.005F, 0.505F, -0.3585F), "controls/control_buttons/control_button_17$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.ENERGY_HARVESTING, TardisConsoleUnitControlType.BUTTON, new Vec3d(-1.005F, 0.505F, -0.295F), "controls/control_buttons/control_button_18$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.SHIELDS_OXYGEN, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.95F, 0.5175F, -0.1575F), "controls/control_buttons/control_button_1$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.SHIELDS_FIRE_PROOF, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.95F, 0.5175F, -0.094F), "controls/control_buttons/control_button_2$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.SHIELDS_MEDICAL, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.95F, 0.5175F, -0.03F), "controls/control_buttons/control_button_3$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.SHIELDS_MINING, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.95F, 0.5175F, 0.0315F), "controls/control_buttons/control_button_4$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.SHIELDS_GRAVITATION, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.95F, 0.5175F, 0.0955F), "controls/control_buttons/control_button_5$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.SHIELDS_SPECIAL, TardisConsoleUnitControlType.BUTTON, new Vec3d(-0.95F, 0.5175F, 0.155F), "controls/control_buttons/control_button_6$_r1", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);

        this.addControlEntry(TardisConsoleUnitControlRole.XSET, TardisConsoleUnitControlType.LEVER, new Vec3d(0.725F, 0.605F, 0.0875F), "controls/control_levers/control_lever_12$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.YSET, TardisConsoleUnitControlType.LEVER, new Vec3d(0.725F, 0.605F, 0), "controls/control_levers/control_lever_13$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.ZSET, TardisConsoleUnitControlType.LEVER, new Vec3d(0.725F, 0.605F, -0.0875F), "controls/control_levers/control_lever_14$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.XYZSTEP, TardisConsoleUnitControlType.LEVER, new Vec3d(0.725F, 0.605F, 0.175F), "controls/control_levers/control_lever_11$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);

        this.addControlEntry(TardisConsoleUnitControlRole.SHIELDS, TardisConsoleUnitControlType.LEVER, new Vec3d(-0.895F, 0.565F, -0.3255F), "controls/control_levers/control_lever_6$_handle");
        this.addControlEntry(TardisConsoleUnitControlRole.DOORS, TardisConsoleUnitControlType.LEVER, new Vec3d(-0.895F, 0.565F, 0.3255F), "controls/control_levers/control_lever_7$_handle");
        this.addControlEntry(TardisConsoleUnitControlRole.LIGHT, TardisConsoleUnitControlType.LEVER, new Vec3d(-0.725F, 0.605F, -0.175F), "controls/control_levers/control_lever_1$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleUnitControlRole.VERTICAL_SCANNING, TardisConsoleUnitControlType.LEVER, new Vec3d(0.725F, 0.605F, -0.175F), "controls/control_levers/control_lever_15$_handle", ModEntities.TARDIS_CONSOLE_UNIT_CONTROL_SMALL);

        this.addControlEntry(TardisConsoleUnitControlRole.RANDOMIZER, TardisConsoleUnitControlType.ROTATOR, new Vec3d(-0.1565F, 0.45F, -1.1695F), "controls/control_rotators/control_rotator_6$_handle");
        this.addControlEntry(TardisConsoleUnitControlRole.FACING, TardisConsoleUnitControlType.ROTATOR, new Vec3d(-0.925F, 0.45F, -0.7245F), "controls/control_rotators/control_rotator_7$_handle");

        this.addControlEntry(TardisConsoleUnitControlRole.STARTER, TardisConsoleUnitControlType.OTHER, new Vec3d(1.05F, 0.495F, -0.335F), "controls/control_starter$_handle");
        this.addControlEntry(TardisConsoleUnitControlRole.HANDBRAKE, TardisConsoleUnitControlType.OTHER, new Vec3d(1.05F, 0.495F, 0.335F), "controls/control_handbrake$_handle");
    }
}

package net.drgmes.dwm.common.tardis.consoles;

import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.minecraft.world.phys.Vec3;

public class TardisConsoleTypeToyota extends TardisConsoleType {
    public TardisConsoleTypeToyota() {
        this.addControlEntry(TardisConsoleControlRoles.RESET_TO_PREV, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.25F, 0.485F, 0.86F), "controls/control_button_9", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.RESET_TO_CURR, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.3275F, 0.485F, 0.86F), "controls/control_button_10", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.DIM_NEXT, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.265F, 0.6F, 0.455F), "controls/control_button_12", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.DIM_PREV, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.18725F, 0.6F, 0.455F), "controls/control_button_13", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.MONITOR_PAGE_PREV, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.265F, 0.6F, 0.455F), "controls/control_button_14", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.MONITOR_PAGE_NEXT, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.18725F, 0.6F, 0.455F), "controls/control_button_15", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.XSET, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.4825F, 0.6F, 0.0925F), "controls/control_button_16", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.YSET, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.4825F, 0.6F, 0), "controls/control_button_17", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.ZSET, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.4825F, 0.6F, -0.0925F), "controls/control_button_18", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.XYZSTEP, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.4825F, 0.6F, 0.25F), "controls/control_button_19", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.MATERIALIZATION, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.4825F, 0.6F, -0.25F), "controls/control_button_20", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.ENERGY_FORGE_HARVESTING, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.4485F, 0.6F, -0.126F), "controls/control_button_21", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.ENERGY_ARTRON_HARVESTING, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.4485F, 0.6F, -0.2185F), "controls/control_button_22", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);

        this.addControlEntry(TardisConsoleControlRoles.SHIELDS, TardisConsoleControlEntryTypes.LEVER, new Vec3(-0.725F, 0.5F, -0.345F), "controls/control_lever_1$_body");
        this.addControlEntry(TardisConsoleControlRoles.DOORS, TardisConsoleControlEntryTypes.LEVER, new Vec3(-0.725F, 0.5F, 0.345F), "controls/control_lever_2$_body");
        this.addControlEntry(TardisConsoleControlRoles.SAFE_DIRECTION, TardisConsoleControlEntryTypes.LEVER, new Vec3(0.6F, 0.55F, -0.125F), "controls/control_lever_9$_body");
        this.addControlEntry(TardisConsoleControlRoles.RANDOMIZER, TardisConsoleControlEntryTypes.ROTATOR, new Vec3(-0.4545F, 0.5F, 0.7225F), "controls/control_cradle_1$_body$_r1");
        this.addControlEntry(TardisConsoleControlRoles.FACING, TardisConsoleControlEntryTypes.ROTATOR, new Vec3(0.4545F, 0.5F, 0.7225F), "controls/control_cradle_2$_body$_r1");
        this.addControlEntry(TardisConsoleControlRoles.STARTER, TardisConsoleControlEntryTypes.OTHER, new Vec3(0.75F, 0.5F, -0.345F), "controls/control_starter$_body");
        this.addControlEntry(TardisConsoleControlRoles.HANDBRAKE, TardisConsoleControlEntryTypes.OTHER, new Vec3(0.75F, 0.5F, 0.345F), "controls/control_handbrake$_body");
    }
}

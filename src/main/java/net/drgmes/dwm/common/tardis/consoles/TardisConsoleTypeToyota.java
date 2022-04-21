package net.drgmes.dwm.common.tardis.consoles;

import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.minecraft.world.phys.Vec3;

public class TardisConsoleTypeToyota extends TardisConsoleType {
    public TardisConsoleTypeToyota() {
        this.addControlEntry(TardisConsoleControlRoles.XSET, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.4825F, 0.6F, 0.0925F), "controls/control_button_16", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.YSET, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.4825F, 0.6F, 0), "controls/control_button_17", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.ZSET, TardisConsoleControlEntryTypes.BUTTON, new Vec3(0.4825F, 0.6F, -0.0925F), "controls/control_button_18", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);
        this.addControlEntry(TardisConsoleControlRoles.FUEL_ARTRON, TardisConsoleControlEntryTypes.BUTTON, new Vec3(-0.4485F, 0.6F, -0.2185F), "controls/control_button_22", ModEntities.TARDIS_CONSOLE_CONTROL_SMALL);

        this.addControlEntry(TardisConsoleControlRoles.SHIELDS, TardisConsoleControlEntryTypes.LEVER, new Vec3(-0.725F, 0.5F, -0.345F), "controls/control_lever_1$_body");
        this.addControlEntry(TardisConsoleControlRoles.DOORS, TardisConsoleControlEntryTypes.LEVER, new Vec3(-0.725F, 0.5F, 0.345F), "controls/control_lever_2$_body");
        this.addControlEntry(TardisConsoleControlRoles.RANDOMIZER, TardisConsoleControlEntryTypes.ROTATOR, new Vec3(-0.4545F, 0.5F, 0.7225F), "controls/control_cradle_1$_body$_r1");
        this.addControlEntry(TardisConsoleControlRoles.FACING, TardisConsoleControlEntryTypes.ROTATOR, new Vec3(0.4545F, 0.5F, 0.7225F), "controls/control_cradle_2$_body$_r1");
        this.addControlEntry(TardisConsoleControlRoles.STARTER, TardisConsoleControlEntryTypes.OTHER, new Vec3(0.75F, 0.5F, -0.345F), "controls/control_starter$_body");
        this.addControlEntry(TardisConsoleControlRoles.HANDBRAKE, TardisConsoleControlEntryTypes.OTHER, new Vec3(0.75F, 0.5F, 0.345F), "controls/control_handbrake$_body");
    }
}

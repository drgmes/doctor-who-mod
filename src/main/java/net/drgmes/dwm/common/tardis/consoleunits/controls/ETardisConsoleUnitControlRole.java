package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.drgmes.dwm.DWM;

public enum ETardisConsoleUnitControlRole {
    NONE(ETardisConsoleUnitControlRoleType.NONE, null),
    MONITOR(ETardisConsoleUnitControlRoleType.NONE, null),
    TELEPATHIC_INTERFACE(ETardisConsoleUnitControlRoleType.NONE, null),
    SCREWDRIVER_SLOT(ETardisConsoleUnitControlRoleType.NONE, null),
    DOORS(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.doors"),
    SHIELDS(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.shields"),
    LIGHT(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.light"),
    ENERGY_ARTRON_HARVESTING(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.energy.artron"),
    ENERGY_FORGE_HARVESTING(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.energy.forge"),
    HANDBRAKE(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, "message." + DWM.MODID + ".tardis.control.role.handbrake"),
    STARTER(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, null),
    MATERIALIZATION(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, null),
    FACING(ETardisConsoleUnitControlRoleType.NUMBER_DIRECT, "message." + DWM.MODID + ".tardis.control.role.facing", 4),
    SAFE_DIRECTION(ETardisConsoleUnitControlRoleType.NUMBER_DIRECT_BLOCK, "message." + DWM.MODID + ".tardis.control.role.safedirection", 3),
    DIM_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, "message." + DWM.MODID + ".tardis.control.role.dimension", 5),
    DIM_NEXT(ETardisConsoleUnitControlRoleType.ANIMATION, "message." + DWM.MODID + ".tardis.control.role.dimension", 5),
    RESET_TO_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5),
    RESET_TO_CURR(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5),
    MONITOR_PAGE_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5),
    MONITOR_PAGE_NEXT(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5),
    RANDOMIZER(ETardisConsoleUnitControlRoleType.ANIMATION, null, 16),
    XSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.xset", 5),
    YSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.yset", 5),
    ZSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.zset", 5),
    XYZSTEP(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.xyzstep", 5);

    public final ETardisConsoleUnitControlRoleType type;
    public final String message;
    public final int maxIntValue;

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message, int maxIntValue) {
        this.type = type;
        this.message = message;
        this.maxIntValue = maxIntValue;
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message) {
        this(type, message, 0);
    }
}

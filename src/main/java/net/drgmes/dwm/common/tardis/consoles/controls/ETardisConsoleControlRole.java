package net.drgmes.dwm.common.tardis.consoles.controls;

import net.drgmes.dwm.DWM;

public enum ETardisConsoleControlRole {
    NONE(ETardisConsoleControlRoleType.NONE, null),
    MONITOR(ETardisConsoleControlRoleType.NONE, null),
    TELEPATHIC_INTERFACE(ETardisConsoleControlRoleType.NONE, null),
    SCREWDRIVER_SLOT(ETardisConsoleControlRoleType.NONE, null),
    DOORS(ETardisConsoleControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.doors"),
    SHIELDS(ETardisConsoleControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.shields"),
    LIGHT(ETardisConsoleControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.light"),
    ENERGY_ARTRON_HARVESTING(ETardisConsoleControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.energy.artron"),
    ENERGY_FORGE_HARVESTING(ETardisConsoleControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.energy.forge"),
    HANDBRAKE(ETardisConsoleControlRoleType.BOOLEAN_DIRECT, "message." + DWM.MODID + ".tardis.control.role.handbrake"),
    STARTER(ETardisConsoleControlRoleType.BOOLEAN_DIRECT, null),
    MATERIALIZATION(ETardisConsoleControlRoleType.BOOLEAN_DIRECT, null),
    FACING(ETardisConsoleControlRoleType.NUMBER_DIRECT, "message." + DWM.MODID + ".tardis.control.role.facing", 4),
    SAFE_DIRECTION(ETardisConsoleControlRoleType.NUMBER_DIRECT_BLOCK, "message." + DWM.MODID + ".tardis.control.role.safedirection", 3),
    DIM_PREV(ETardisConsoleControlRoleType.ANIMATION, "message." + DWM.MODID + ".tardis.control.role.dimension", 5),
    DIM_NEXT(ETardisConsoleControlRoleType.ANIMATION, "message." + DWM.MODID + ".tardis.control.role.dimension", 5),
    RESET_TO_PREV(ETardisConsoleControlRoleType.ANIMATION, null, 5),
    RESET_TO_CURR(ETardisConsoleControlRoleType.ANIMATION, null, 5),
    MONITOR_PAGE_PREV(ETardisConsoleControlRoleType.ANIMATION, null, 5),
    MONITOR_PAGE_NEXT(ETardisConsoleControlRoleType.ANIMATION, null, 5),
    RANDOMIZER(ETardisConsoleControlRoleType.ANIMATION, null, 16),
    XSET(ETardisConsoleControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.xset", 5),
    YSET(ETardisConsoleControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.yset", 5),
    ZSET(ETardisConsoleControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.zset", 5),
    XYZSTEP(ETardisConsoleControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.xyzstep", 5);

    public final ETardisConsoleControlRoleType type;
    public final String message;
    public final int maxIntValue;

    ETardisConsoleControlRole(ETardisConsoleControlRoleType type, String message, int maxIntValue) {
        this.type = type;
        this.message = message;
        this.maxIntValue = maxIntValue;
    }

    ETardisConsoleControlRole(ETardisConsoleControlRoleType type, String message) {
        this(type, message, 0);
    }
}

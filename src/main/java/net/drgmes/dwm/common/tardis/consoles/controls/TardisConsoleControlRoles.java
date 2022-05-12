package net.drgmes.dwm.common.tardis.consoles.controls;

import net.drgmes.dwm.DWM;

public enum TardisConsoleControlRoles {
    NONE(TardisConsoleControlRoleTypes.NONE, null),
    MONITOR(TardisConsoleControlRoleTypes.NONE, null),
    TELEPATHIC_INTERFACE(TardisConsoleControlRoleTypes.NONE, null),
    SCREWDRIVER_SLOT(TardisConsoleControlRoleTypes.NONE, null),
    DOORS(TardisConsoleControlRoleTypes.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.doors"),
    SHIELDS(TardisConsoleControlRoleTypes.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.shields"),
    LIGHT(TardisConsoleControlRoleTypes.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.light"),
    ENERGY_ARTRON_HARVESTING(TardisConsoleControlRoleTypes.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.energy.artron"),
    ENERGY_FORGE_HARVESTING(TardisConsoleControlRoleTypes.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.energy.forge"),
    HANDBRAKE(TardisConsoleControlRoleTypes.BOOLEAN_DIRECT, "message." + DWM.MODID + ".tardis.control.role.handbrake"),
    STARTER(TardisConsoleControlRoleTypes.BOOLEAN_DIRECT, null),
    MATERIALIZATION(TardisConsoleControlRoleTypes.BOOLEAN_DIRECT, null),
    FACING(TardisConsoleControlRoleTypes.NUMBER_DIRECT, "message." + DWM.MODID + ".tardis.control.role.facing", 4),
    SAFE_DIRECTION(TardisConsoleControlRoleTypes.NUMBER_DIRECT_BLOCK, "message." + DWM.MODID + ".tardis.control.role.safedirection", 3),
    DIM_PREV(TardisConsoleControlRoleTypes.ANIMATION, "message." + DWM.MODID + ".tardis.control.role.dimension", 5),
    DIM_NEXT(TardisConsoleControlRoleTypes.ANIMATION, "message." + DWM.MODID + ".tardis.control.role.dimension", 5),
    RESET_TO_PREV(TardisConsoleControlRoleTypes.ANIMATION, null, 5),
    RESET_TO_CURR(TardisConsoleControlRoleTypes.ANIMATION, null, 5),
    MONITOR_PAGE_PREV(TardisConsoleControlRoleTypes.ANIMATION, null, 5),
    MONITOR_PAGE_NEXT(TardisConsoleControlRoleTypes.ANIMATION, null, 5),
    RANDOMIZER(TardisConsoleControlRoleTypes.ANIMATION, null, 16),
    XSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.xset", 5),
    YSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.yset", 5),
    ZSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.zset", 5),
    XYZSTEP(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.xyzstep", 5);

    public final TardisConsoleControlRoleTypes type;
    public final String message;
    public final int maxIntValue;

    TardisConsoleControlRoles(TardisConsoleControlRoleTypes type, String message, int maxIntValue) {
        this.type = type;
        this.message = message;
        this.maxIntValue = maxIntValue;
    }

    TardisConsoleControlRoles(TardisConsoleControlRoleTypes type, String message) {
        this(type, message, 0);
    }
}

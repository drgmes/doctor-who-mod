package net.drgmes.dwm.common.tardis.consoles.controls;

public enum TardisConsoleControlRoles {
    NONE(TardisConsoleControlRoleTypes.NONE),
    MONITOR(TardisConsoleControlRoleTypes.NONE),
    TELEPATIC_INTERFACE(TardisConsoleControlRoleTypes.NONE),
    SCREWDRIVER_SLOT(TardisConsoleControlRoleTypes.NONE),
    DOORS(TardisConsoleControlRoleTypes.BOOLEAN),
    SHIELDS(TardisConsoleControlRoleTypes.BOOLEAN),
    LIGHT(TardisConsoleControlRoleTypes.BOOLEAN),
    ENERGY_ARTRON_HARVESTING(TardisConsoleControlRoleTypes.BOOLEAN),
    ENERGY_FORGE_HARVESTING(TardisConsoleControlRoleTypes.BOOLEAN),
    MATERIALIZATION(TardisConsoleControlRoleTypes.BOOLEAN_DIRECT),
    HANDBRAKE(TardisConsoleControlRoleTypes.BOOLEAN_DIRECT),
    STARTER(TardisConsoleControlRoleTypes.BOOLEAN_DIRECT),
    FACING(TardisConsoleControlRoleTypes.NUMBER_DIRECT, 4),
    SAFE_DIRECTION(TardisConsoleControlRoleTypes.NUMBER_DIRECT_BLOCK, 3),
    RESET_TO_PREV(TardisConsoleControlRoleTypes.ANIMATION, 5),
    RESET_TO_CURR(TardisConsoleControlRoleTypes.ANIMATION, 5),
    DIM_PREV(TardisConsoleControlRoleTypes.ANIMATION, 5),
    DIM_NEXT(TardisConsoleControlRoleTypes.ANIMATION, 5),
    MONITOR_PAGE_PREV(TardisConsoleControlRoleTypes.ANIMATION, 5),
    MONITOR_PAGE_NEXT(TardisConsoleControlRoleTypes.ANIMATION, 5),
    RANDOMIZER(TardisConsoleControlRoleTypes.ANIMATION, 16),
    XSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, 5),
    YSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, 5),
    ZSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, 5),
    XYZSTEP(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, 5);

    public final TardisConsoleControlRoleTypes type;
    public final int maxIntValue;

    TardisConsoleControlRoles(TardisConsoleControlRoleTypes type, int maxIntValue) {
        this.type = type;
        this.maxIntValue = maxIntValue;
    }

    TardisConsoleControlRoles(TardisConsoleControlRoleTypes type) {
        this(type, 0);
    }
}

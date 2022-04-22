package net.drgmes.dwm.common.tardis.consoles.controls;

public enum TardisConsoleControlRoles {
    NONE(TardisConsoleControlRoleTypes.NONE),
    MONITOR(TardisConsoleControlRoleTypes.NONE),
    TELEPATIC_INTERFACE(TardisConsoleControlRoleTypes.NONE),
    DOORS(TardisConsoleControlRoleTypes.BOOLEAN),
    SHIELDS(TardisConsoleControlRoleTypes.BOOLEAN),
    FUEL_ARTRON(TardisConsoleControlRoleTypes.BOOLEAN),
    HANDBRAKE(TardisConsoleControlRoleTypes.BOOLEAN_DIRECT),
    STARTER(TardisConsoleControlRoleTypes.BOOLEAN_DIRECT),
    FACING(TardisConsoleControlRoleTypes.NUMBER_DIRECT, 4),
    XSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, 5),
    YSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, 5),
    ZSET(TardisConsoleControlRoleTypes.ANIMATION_DIRECT, 5),
    RANDOMIZER(TardisConsoleControlRoleTypes.ANIMATION, 16);

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

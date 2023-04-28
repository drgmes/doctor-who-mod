package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.sound.SoundEvent;

import java.util.function.Supplier;

public enum ETardisConsoleUnitControlRole {
    NONE(ETardisConsoleUnitControlRoleType.NONE),
    MONITOR(ETardisConsoleUnitControlRoleType.NONE),
    TELEPATHIC_INTERFACE(ETardisConsoleUnitControlRoleType.NONE),
    SCREWDRIVER_SLOT(ETardisConsoleUnitControlRoleType.NONE),
    DOORS(ETardisConsoleUnitControlRoleType.BOOLEAN, "doors", 0, ModSounds.TARDIS_CONTROL_3),
    SHIELDS(ETardisConsoleUnitControlRoleType.BOOLEAN, "shields", 0, ModSounds.TARDIS_CONTROL_3),
    LIGHT(ETardisConsoleUnitControlRoleType.BOOLEAN, "light", 0, ModSounds.TARDIS_CONTROL_3),
    FUEL_HARVESTING(ETardisConsoleUnitControlRoleType.BOOLEAN, "fuel", 0, ModSounds.TARDIS_CONTROL_2),
    ENERGY_HARVESTING(ETardisConsoleUnitControlRoleType.BOOLEAN, "energy", 0, ModSounds.TARDIS_CONTROL_2),
    HANDBRAKE(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, "handbrake", 0),
    STARTER(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, null, 0),
    MATERIALIZATION(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, null, 0),
    FACING(ETardisConsoleUnitControlRoleType.NUMBER_DIRECT, "facing", 4, ModSounds.TARDIS_CONTROL_4),
    SAFE_DIRECTION(ETardisConsoleUnitControlRoleType.NUMBER_DIRECT_BLOCK, "safe_direction", 3, ModSounds.TARDIS_CONTROL_3),
    DIM_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, "dimension", 5, ModSounds.TARDIS_CONTROL_1),
    DIM_NEXT(ETardisConsoleUnitControlRoleType.ANIMATION, "dimension", 5, ModSounds.TARDIS_CONTROL_1),
    RESET_TO_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, "reset_to_prev", 5, ModSounds.TARDIS_CONTROL_2),
    RESET_TO_CURR(ETardisConsoleUnitControlRoleType.ANIMATION, "reset_to_curr", 5, ModSounds.TARDIS_CONTROL_2),
    MONITOR_PAGE_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5, ModSounds.TARDIS_CONTROL_2),
    MONITOR_PAGE_NEXT(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5, ModSounds.TARDIS_CONTROL_2),
    RANDOMIZER(ETardisConsoleUnitControlRoleType.ANIMATION, "randomizer", 16, ModSounds.TARDIS_CONTROL_RANDOMIZER),
    XSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "x_set", 5, ModSounds.TARDIS_CONTROL_3),
    YSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "y_set", 5, ModSounds.TARDIS_CONTROL_3),
    ZSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "z_set", 5, ModSounds.TARDIS_CONTROL_3),
    XYZSTEP(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "xyz_step", 5, ModSounds.TARDIS_CONTROL_3);

    public final ETardisConsoleUnitControlRoleType type;
    public final String message;
    public final int maxIntValue;
    public final Supplier<SoundEvent> soundEventSupplier;

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message, int maxIntValue, Supplier<SoundEvent> soundEventSupplier) {
        this.type = type;
        this.message = message;
        this.maxIntValue = maxIntValue;
        this.soundEventSupplier = soundEventSupplier;
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message, int maxIntValue) {
        this(type, message, maxIntValue, null);
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message) {
        this(type, message, 0);
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type) {
        this(type, null);
    }
}

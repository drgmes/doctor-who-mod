package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.sound.SoundEvent;

import java.util.Set;
import java.util.function.Supplier;

public enum ETardisConsoleUnitControlRole {
    NONE(ETardisConsoleUnitControlRoleType.NONE),
    MONITOR(ETardisConsoleUnitControlRoleType.NONE),
    TELEPATHIC_INTERFACE(ETardisConsoleUnitControlRoleType.NONE),
    SCREWDRIVER_SLOT(ETardisConsoleUnitControlRoleType.NONE),
    DOORS(ETardisConsoleUnitControlRoleType.BOOLEAN, "doors", 0, ModSounds.TARDIS_CONTROL_3, Set.of(ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED)),
    SHIELDS(ETardisConsoleUnitControlRoleType.BOOLEAN, "shields", 0, ModSounds.TARDIS_CONTROL_3, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_SHIELDS_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED)),
    SHIELDS_OXYGEN(ETardisConsoleUnitControlRoleType.BOOLEAN, "shields_oxygen", 0, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_SHIELDS_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED, ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_FIRE_PROOF(ETardisConsoleUnitControlRoleType.BOOLEAN, "shields_fire_proof", 0, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_SHIELDS_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED, ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_MEDICAL(ETardisConsoleUnitControlRoleType.BOOLEAN, "shields_medical", 0, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_SHIELDS_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED, ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_MINING(ETardisConsoleUnitControlRoleType.BOOLEAN, "shields_mining", 0, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_SHIELDS_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED, ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_GRAVITATION(ETardisConsoleUnitControlRoleType.BOOLEAN, "shields_gravitation", 0, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_SHIELDS_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED, ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_SPECIAL(ETardisConsoleUnitControlRoleType.BOOLEAN, "shields_special", 0, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_SHIELDS_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED, ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_SHIELDS_ON)),
    LIGHT(ETardisConsoleUnitControlRoleType.BOOLEAN, "light", 0, ModSounds.TARDIS_CONTROL_3, Set.of(ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED)),
    FUEL_HARVESTING(ETardisConsoleUnitControlRoleType.BOOLEAN, "fuel", 0, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    ENERGY_HARVESTING(ETardisConsoleUnitControlRoleType.BOOLEAN, "energy", 0, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    HANDBRAKE(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, "handbrake", 0, Set.of(ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_OWNER)),
    STARTER(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, null, 0, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_MATERIALIZING_SYSTEM, ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_HANDBRAKE_OFF)),
    MATERIALIZATION(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, null, 0, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_MATERIALIZING_SYSTEM, ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_HANDBRAKE_OFF)),
    SAFE_DIRECTION(ETardisConsoleUnitControlRoleType.NUMBER_DIRECT_BLOCK, "safe_direction", 4, ModSounds.TARDIS_CONTROL_3, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_MATERIALIZING_SYSTEM)),
    FACING(ETardisConsoleUnitControlRoleType.NUMBER_DIRECT, "facing", 4, ModSounds.TARDIS_CONTROL_4, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    DIM_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, "dimension", 5, ModSounds.TARDIS_CONTROL_1, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    DIM_NEXT(ETardisConsoleUnitControlRoleType.ANIMATION, "dimension", 5, ModSounds.TARDIS_CONTROL_1, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    RESET_TO_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, "reset_to_prev", 5, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    RESET_TO_CURR(ETardisConsoleUnitControlRoleType.ANIMATION, "reset_to_curr", 5, ModSounds.TARDIS_CONTROL_2, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    MONITOR_PAGE_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5, ModSounds.TARDIS_CONTROL_2),
    MONITOR_PAGE_NEXT(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5, ModSounds.TARDIS_CONTROL_2),
    RANDOMIZER(ETardisConsoleUnitControlRoleType.ANIMATION, "randomizer", 16, ModSounds.TARDIS_CONTROL_RANDOMIZER, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    XSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "x_set", 5, ModSounds.TARDIS_CONTROL_3, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    YSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "y_set", 5, ModSounds.TARDIS_CONTROL_3, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    ZSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "z_set", 5, ModSounds.TARDIS_CONTROL_3, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM, ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED)),
    XYZSTEP(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "xyz_step", 5, ModSounds.TARDIS_CONTROL_3, Set.of(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM));

    public final ETardisConsoleUnitControlRoleType type;
    public final String message;
    public final int maxIntValue;
    public final Supplier<SoundEvent> soundEventSupplier;
    public final Set<ETardisConsoleUnitControlRoleFlags> flags;

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message, int maxIntValue, Supplier<SoundEvent> soundEventSupplier, Set<ETardisConsoleUnitControlRoleFlags> flags) {
        this.type = type;
        this.message = message;
        this.maxIntValue = maxIntValue;
        this.soundEventSupplier = soundEventSupplier;
        this.flags = flags;
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message, int maxIntValue, Set<ETardisConsoleUnitControlRoleFlags> flags) {
        this(type, message, maxIntValue, null, flags);
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message, int maxIntValue, Supplier<SoundEvent> soundEventSupplier) {
        this(type, message, maxIntValue, soundEventSupplier, Set.of());
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message, int maxIntValue) {
        this(type, message, maxIntValue, null, Set.of());
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message) {
        this(type, message, 0);
    }

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type) {
        this(type, null);
    }
}

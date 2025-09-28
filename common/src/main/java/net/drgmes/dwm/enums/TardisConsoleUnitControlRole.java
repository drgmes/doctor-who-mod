package net.drgmes.dwm.enums;

import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;
import java.util.function.Supplier;

public enum TardisConsoleUnitControlRole {
    NONE(TardisConsoleUnitControlValueType.NONE),
    MONITOR(TardisConsoleUnitControlValueType.NONE),
    TELEPATHIC_INTERFACE(TardisConsoleUnitControlValueType.NONE, "telepathic_interface"),
    SONIC_SCREWDRIVER_SLOT(TardisConsoleUnitControlValueType.NONE, "sonic_screwdriver_slot"),
    DOORS(TardisConsoleUnitControlValueType.BOOLEAN, "doors", "doors", 0, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED)),
    SHIELDS(TardisConsoleUnitControlValueType.BOOLEAN, "shields", "shields", 0, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED)),
    SHIELDS_OXYGEN(TardisConsoleUnitControlValueType.BOOLEAN, "shields_oxygen", "shields_oxygen", 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED, TardisConsoleUnitControlFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_FIRE_PROOF(TardisConsoleUnitControlValueType.BOOLEAN, "shields_fire_proof", "shields_fire_proof", 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED, TardisConsoleUnitControlFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_MEDICAL(TardisConsoleUnitControlValueType.BOOLEAN, "shields_medical", "shields_medical", 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED, TardisConsoleUnitControlFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_MINING(TardisConsoleUnitControlValueType.BOOLEAN, "shields_mining", "shields_mining", 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED, TardisConsoleUnitControlFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_GRAVITATION(TardisConsoleUnitControlValueType.BOOLEAN, "shields_gravitation", "shields_gravitation", 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED, TardisConsoleUnitControlFlags.DEPENDS_ON_SHIELDS_ON)),
    SHIELDS_SPECIAL(TardisConsoleUnitControlValueType.BOOLEAN, "shields_special", "shields_special", 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED, TardisConsoleUnitControlFlags.DEPENDS_ON_SHIELDS_ON)),
    LIGHT(TardisConsoleUnitControlValueType.BOOLEAN, "light", "light", 0, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED)),
    FUEL_HARVESTING(TardisConsoleUnitControlValueType.BOOLEAN, "fuel", "fuel", 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    ENERGY_HARVESTING(TardisConsoleUnitControlValueType.BOOLEAN, "energy", "energy", 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    HANDBRAKE(TardisConsoleUnitControlValueType.BOOLEAN_DIRECT, "handbrake", "handbrake", 0, Set.of(TardisConsoleUnitControlFlags.DEPENDS_ON_OWNER)),
    STARTER(TardisConsoleUnitControlValueType.BOOLEAN_DIRECT, "starter", null, 0, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.REQUIRED_MATERIALIZING_SYSTEM, TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.DEPENDS_ON_HANDBRAKE_OFF)),
    MATERIALIZATION(TardisConsoleUnitControlValueType.BOOLEAN_DIRECT, "materialization", null, 0, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_MATERIALIZING_SYSTEM, TardisConsoleUnitControlFlags.DEPENDS_ON_HANDBRAKE_OFF)),
    VERTICAL_SCANNING(TardisConsoleUnitControlValueType.NUMBER_DIRECT_LIMITED, "vertical_scanning", "vertical_scanning", 4, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.REQUIRED_MATERIALIZING_SYSTEM)),
    FACING(TardisConsoleUnitControlValueType.NUMBER_DIRECT, "facing", "facing", 4, ModSounds.TARDIS_CONTROL_4, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    DIM_PREV(TardisConsoleUnitControlValueType.ANIMATION, "dim_prev", "dimension", 5, ModSounds.TARDIS_CONTROL_1, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    DIM_NEXT(TardisConsoleUnitControlValueType.ANIMATION, "dim_next", "dimension", 5, ModSounds.TARDIS_CONTROL_1, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    RESET_TO_PREV(TardisConsoleUnitControlValueType.ANIMATION, "reset_to_prev", "reset_to_prev", 5, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    RESET_TO_CURR(TardisConsoleUnitControlValueType.ANIMATION, "reset_to_curr", "reset_to_curr", 5, ModSounds.TARDIS_CONTROL_2, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    MONITOR_PAGE_PREV(TardisConsoleUnitControlValueType.ANIMATION, "monitor_page_prev", null, 5, ModSounds.TARDIS_CONTROL_2),
    MONITOR_PAGE_NEXT(TardisConsoleUnitControlValueType.ANIMATION, "monitor_page_next", null, 5, ModSounds.TARDIS_CONTROL_2),
    RANDOMIZER(TardisConsoleUnitControlValueType.ANIMATION, "randomizer", "randomizer", 16, ModSounds.TARDIS_CONTROL_RANDOMIZER, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    XSET(TardisConsoleUnitControlValueType.ANIMATION_DIRECT, "x_set", "x_set", 5, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    YSET(TardisConsoleUnitControlValueType.ANIMATION_DIRECT, "y_set", "y_set", 5, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    ZSET(TardisConsoleUnitControlValueType.ANIMATION_DIRECT, "z_set", "z_set", 5, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM, TardisConsoleUnitControlFlags.MUST_BE_LANDED)),
    XYZSTEP(TardisConsoleUnitControlValueType.ANIMATION_DIRECT, "xyz_step", "xyz_step", 5, ModSounds.TARDIS_CONTROL_3, Set.of(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM));

    public final TardisConsoleUnitControlValueType type;
    public final String name;
    public final String message;
    public final int maxIntValue;
    public final Supplier<SoundEvent> soundEventSupplier;
    public final Set<TardisConsoleUnitControlFlags> flags;

    TardisConsoleUnitControlRole(TardisConsoleUnitControlValueType type, String name, String message, int maxIntValue, Supplier<SoundEvent> soundEventSupplier, Set<TardisConsoleUnitControlFlags> flags) {
        this.type = type;
        this.name = name;
        this.message = message;
        this.maxIntValue = maxIntValue;
        this.soundEventSupplier = soundEventSupplier;
        this.flags = flags;
    }

    TardisConsoleUnitControlRole(TardisConsoleUnitControlValueType type, String name, String message, int maxIntValue, Set<TardisConsoleUnitControlFlags> flags) {
        this(type, name, message, maxIntValue, null, flags);
    }

    TardisConsoleUnitControlRole(TardisConsoleUnitControlValueType type, String name, String message, int maxIntValue, Supplier<SoundEvent> soundEventSupplier) {
        this(type, name, message, maxIntValue, soundEventSupplier, Set.of());
    }

    TardisConsoleUnitControlRole(TardisConsoleUnitControlValueType type, String name, String message, int maxIntValue) {
        this(type, name, message, maxIntValue, null, Set.of());
    }

    TardisConsoleUnitControlRole(TardisConsoleUnitControlValueType type, String name, String message) {
        this(type, name, message, 0);
    }

    TardisConsoleUnitControlRole(TardisConsoleUnitControlValueType type, String name) {
        this(type, name, null);
    }

    TardisConsoleUnitControlRole(TardisConsoleUnitControlValueType type) {
        this(type, null);
    }

    public void playSound(World world, BlockPos blockPos) {
        if (this.soundEventSupplier == null || !(world instanceof ServerWorld)) return;
        ModSounds.playSound(world, blockPos, this.soundEventSupplier.get(), 1.0F, 1.0F);
    }
}

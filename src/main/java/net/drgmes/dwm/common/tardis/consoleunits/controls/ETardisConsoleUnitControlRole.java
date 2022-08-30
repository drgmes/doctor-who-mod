package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.sound.SoundEvent;

public enum ETardisConsoleUnitControlRole {
    NONE(ETardisConsoleUnitControlRoleType.NONE),
    MONITOR(ETardisConsoleUnitControlRoleType.NONE),
    TELEPATHIC_INTERFACE(ETardisConsoleUnitControlRoleType.NONE),
    SCREWDRIVER_SLOT(ETardisConsoleUnitControlRoleType.NONE),
    DOORS(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.doors", 0, ModSounds.TARDIS_CONTROL_3),
    SHIELDS(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.shields", 0, ModSounds.TARDIS_CONTROL_3),
    LIGHT(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.light", 0, ModSounds.TARDIS_CONTROL_3),
    ENERGY_ARTRON_HARVESTING(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.energy.artron", 0, ModSounds.TARDIS_CONTROL_2),
    ENERGY_FORGE_HARVESTING(ETardisConsoleUnitControlRoleType.BOOLEAN, "message." + DWM.MODID + ".tardis.control.role.energy.forge", 0, ModSounds.TARDIS_CONTROL_2),
    HANDBRAKE(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, "message." + DWM.MODID + ".tardis.control.role.handbrake", 0),
    STARTER(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, null, 0),
    MATERIALIZATION(ETardisConsoleUnitControlRoleType.BOOLEAN_DIRECT, null, 0),
    FACING(ETardisConsoleUnitControlRoleType.NUMBER_DIRECT, "message." + DWM.MODID + ".tardis.control.role.facing", 4, ModSounds.TARDIS_CONTROL_4),
    SAFE_DIRECTION(ETardisConsoleUnitControlRoleType.NUMBER_DIRECT_BLOCK, "message." + DWM.MODID + ".tardis.control.role.safedirection", 3, ModSounds.TARDIS_CONTROL_3),
    DIM_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, "message." + DWM.MODID + ".tardis.control.role.dimension", 5, ModSounds.TARDIS_CONTROL_1),
    DIM_NEXT(ETardisConsoleUnitControlRoleType.ANIMATION, "message." + DWM.MODID + ".tardis.control.role.dimension", 5, ModSounds.TARDIS_CONTROL_1),
    RESET_TO_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5, ModSounds.TARDIS_CONTROL_2),
    RESET_TO_CURR(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5, ModSounds.TARDIS_CONTROL_2),
    MONITOR_PAGE_PREV(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5, ModSounds.TARDIS_CONTROL_2),
    MONITOR_PAGE_NEXT(ETardisConsoleUnitControlRoleType.ANIMATION, null, 5, ModSounds.TARDIS_CONTROL_2),
    RANDOMIZER(ETardisConsoleUnitControlRoleType.ANIMATION, null, 16, ModSounds.TARDIS_CONTROL_RANDOMIZER),
    XSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.xset", 5, ModSounds.TARDIS_CONTROL_3),
    YSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.yset", 5, ModSounds.TARDIS_CONTROL_3),
    ZSET(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.zset", 5, ModSounds.TARDIS_CONTROL_3),
    XYZSTEP(ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT, "message." + DWM.MODID + ".tardis.control.role.xyzstep", 5, ModSounds.TARDIS_CONTROL_3);

    public final ETardisConsoleUnitControlRoleType type;
    public final String message;
    public final SoundEvent sound;
    public final int maxIntValue;

    ETardisConsoleUnitControlRole(ETardisConsoleUnitControlRoleType type, String message, int maxIntValue, SoundEvent sound) {
        this.type = type;
        this.message = message;
        this.maxIntValue = maxIntValue;
        this.sound = sound;
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

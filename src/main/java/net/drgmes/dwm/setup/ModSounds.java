package net.drgmes.dwm.setup;

import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final RegistryObject<SoundEvent> SCREWDRIVER_MAIN = Registration.registerSoundEvent("screwdriver_main");

    public static final RegistryObject<SoundEvent> TARDIS_ERROR = Registration.registerSoundEvent("tardis_error");
    public static final RegistryObject<SoundEvent> TARDIS_FLY = Registration.registerSoundEvent("tardis_fly");
    public static final RegistryObject<SoundEvent> TARDIS_LAND = Registration.registerSoundEvent("tardis_land");
    public static final RegistryObject<SoundEvent> TARDIS_TAKEOFF = Registration.registerSoundEvent("tardis_takeoff");

    public static void init() {
    }
}

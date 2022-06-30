package net.drgmes.dwm.setup;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;

public class ModSounds {
    public static final RegistryObject<SoundEvent> SCREWDRIVER_MAIN = Registration.registerSoundEvent("screwdriver_main");

    public static final RegistryObject<SoundEvent> TARDIS_ERROR = Registration.registerSoundEvent("tardis_error");
    public static final RegistryObject<SoundEvent> TARDIS_FLY = Registration.registerSoundEvent("tardis_fly");
    public static final RegistryObject<SoundEvent> TARDIS_LAND = Registration.registerSoundEvent("tardis_land");
    public static final RegistryObject<SoundEvent> TARDIS_TAKEOFF = Registration.registerSoundEvent("tardis_takeoff");

    public static void init() {
    }

    public static void playSound(Level level, BlockPos blockPos, SoundEvent sound, float pitch, float distance) {
        if (level != null) level.playSound(null, blockPos, sound, SoundSource.BLOCKS, pitch, distance);
    }

    public static void playTardisDoorsUnlockSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.IRON_DOOR_OPEN, 1.0F, 1.0F);
    }

    public static void playTardisDoorsLockSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.IRON_DOOR_CLOSE, 1.0F, 1.0F);
    }

    public static void playTardisDoorsOpenSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.WOODEN_DOOR_OPEN, 1.0F, 1.0F);
    }

    public static void playTardisDoorsCloseSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.WOODEN_DOOR_CLOSE, 1.0F, 1.0F);
    }

    public static void playTardisLightOnSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.ENDER_EYE_LAUNCH, 1.0F, 1.0F);
    }

    public static void playTardisLightOffSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.ENDER_EYE_LAUNCH, 1.0F, 1.0F);
    }

    public static void playTardisShieldsOnSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisShieldsOffSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.BEACON_DEACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisComponentAddedSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisComponentRemovedSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.BEACON_DEACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisTakeoffSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, ModSounds.TARDIS_TAKEOFF.get(), 1.0F, 1.0F);
    }

    public static void playTardisLandingSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, ModSounds.TARDIS_LAND.get(), 1.0F, 1.0F);
    }

    public static void playTardisFlySound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, ModSounds.TARDIS_FLY.get(), 1.0F, 1.0F);
    }

    public static void playTardisFailSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, ModSounds.TARDIS_ERROR.get(), 1.0F, 1.0F);
    }

    public static void playScrewdriverMainSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, ModSounds.SCREWDRIVER_MAIN.get(), 0.25F, 1F);
    }

    public static void playTardisRoomCreatedSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisRoomDestroyedSound(Level level, BlockPos blockPos) {
        ModSounds.playSound(level, blockPos, SoundEvents.BEACON_DEACTIVATE, 1.0F, 1.0F);
    }
}

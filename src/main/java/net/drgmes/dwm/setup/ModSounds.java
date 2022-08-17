package net.drgmes.dwm.setup;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ModSounds {
    public static final SoundEvent SCREWDRIVER_MAIN = Registration.registerSoundEvent("screwdriver_main");

    public static final SoundEvent TARDIS_ERROR = Registration.registerSoundEvent("tardis_error");
    public static final SoundEvent TARDIS_FLY = Registration.registerSoundEvent("tardis_fly");
    public static final SoundEvent TARDIS_LAND = Registration.registerSoundEvent("tardis_land");
    public static final SoundEvent TARDIS_TAKEOFF = Registration.registerSoundEvent("tardis_takeoff");

    public static void init() {
    }

    public static void playSound(World world, BlockPos blockPos, SoundEvent sound, float pitch, float distance) {
        world.playSound(null, blockPos, sound, SoundCategory.BLOCKS, pitch, distance);
    }

    public static void playTardisDoorsUnlockSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
    }

    public static void playTardisDoorsLockSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_IRON_DOOR_CLOSE, 1.0F, 1.0F);
    }

    public static void playTardisDoorsOpenSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_WOODEN_DOOR_OPEN, 1.0F, 1.0F);
    }

    public static void playTardisDoorsCloseSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_WOODEN_DOOR_CLOSE, 1.0F, 1.0F);
    }

    public static void playTardisLightOnSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, 1.0F, 1.0F);
    }

    public static void playTardisLightOffSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, 1.0F, 1.0F);
    }

    public static void playTardisShieldsOnSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisShieldsOffSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisComponentAddedSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisComponentRemovedSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisTakeoffSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, ModSounds.TARDIS_TAKEOFF, 1.0F, 1.0F);
    }

    public static void playTardisLandingSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, ModSounds.TARDIS_LAND, 1.0F, 1.0F);
    }

    public static void playTardisFlySound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, ModSounds.TARDIS_FLY, 1.0F, 1.0F);
    }

    public static void playTardisFailSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, ModSounds.TARDIS_ERROR, 1.0F, 1.0F);
    }

    public static void playScrewdriverMainSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, ModSounds.SCREWDRIVER_MAIN, 0.25F, 1F);
    }

    public static void playTardisArsStructureCreatedSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisArsStructureDestroyedSound(World world, BlockPos blockPos) {
        ModSounds.playSound(world, blockPos, SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
    }
}

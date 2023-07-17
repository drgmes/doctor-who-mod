package net.drgmes.dwm.setup;

import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.function.Supplier;

public class ModSounds {
    public static final Supplier<SoundEvent> SCREWDRIVER_MAIN = Registration.registerSoundEvent("screwdriver_main");

    public static final Supplier<SoundEvent> TARDIS_DOORS_KNOCK = Registration.registerSoundEvent("tardis_doors_knock");
    public static final Supplier<SoundEvent> TARDIS_DOORS_UNLOCK = Registration.registerSoundEvent("tardis_doors_unlock");
    public static final Supplier<SoundEvent> TARDIS_DOORS_LOCK = Registration.registerSoundEvent("tardis_doors_lock");
    public static final Supplier<SoundEvent> TARDIS_DOORS_CLOSE = Registration.registerSoundEvent("tardis_doors_close");
    public static final Supplier<SoundEvent> TARDIS_DOORS_WOODEN_CLOSE = Registration.registerSoundEvent("tardis_doors_wooden_close");

    public static final Supplier<SoundEvent> TARDIS_BROKEN_FLARING = Registration.registerSoundEvent("tardis_broken_flaring");
    public static final Supplier<SoundEvent> TARDIS_ERROR = Registration.registerSoundEvent("tardis_error");
    public static final Supplier<SoundEvent> TARDIS_BELL = Registration.registerSoundEvent("tardis_bell");
    public static final Supplier<SoundEvent> TARDIS_FLY = Registration.registerSoundEvent("tardis_fly");
    public static final Supplier<SoundEvent> TARDIS_LAND = Registration.registerSoundEvent("tardis_land");
    public static final Supplier<SoundEvent> TARDIS_TAKEOFF = Registration.registerSoundEvent("tardis_takeoff");

    public static final Supplier<SoundEvent> TARDIS_CONTROL_1 = Registration.registerSoundEvent("tardis_control_1");
    public static final Supplier<SoundEvent> TARDIS_CONTROL_2 = Registration.registerSoundEvent("tardis_control_2");
    public static final Supplier<SoundEvent> TARDIS_CONTROL_3 = Registration.registerSoundEvent("tardis_control_3");
    public static final Supplier<SoundEvent> TARDIS_CONTROL_4 = Registration.registerSoundEvent("tardis_control_4");
    public static final Supplier<SoundEvent> TARDIS_CONTROL_HANDBRAKE_ON = Registration.registerSoundEvent("tardis_control_handbrake_on");
    public static final Supplier<SoundEvent> TARDIS_CONTROL_HANDBRAKE_OFF = Registration.registerSoundEvent("tardis_control_handbrake_off");
    public static final Supplier<SoundEvent> TARDIS_CONTROL_RANDOMIZER = Registration.registerSoundEvent("tardis_control_randomizer");

    public static void init() {
    }

    public static void playSound(World world, BlockPos blockPos, SoundEvent sound, float pitch, float distance) {
        world.playSound(null, blockPos, sound, SoundCategory.BLOCKS, pitch, distance);
    }

    public static void playTardisConsoleCrackSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_BROKEN_FLARING.get(), 0.65F, 1.0F);
    }

    public static void playTardisRepairSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisDoorsKnockSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_DOORS_KNOCK.get(), 1.0F, 1.0F);
    }

    public static void playTardisDoorsUnlockSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_DOORS_UNLOCK.get(), 1.0F, 1.0F);
    }

    public static void playTardisDoorsLockSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_DOORS_LOCK.get(), 1.0F, 1.0F);
    }

    public static void playTardisDoorsOpenSound(World world, BlockPos blockPos, boolean isWooden) {
        playSound(world, blockPos, isWooden ? SoundEvents.BLOCK_WOODEN_DOOR_OPEN : SoundEvents.BLOCK_IRON_DOOR_OPEN, 1.0F, 1.0F);
    }

    public static void playTardisDoorsCloseSound(World world, BlockPos blockPos, boolean isWooden) {
        playSound(world, blockPos, isWooden ? TARDIS_DOORS_WOODEN_CLOSE.get() : TARDIS_DOORS_CLOSE.get(), 1.0F, 1.0F);
    }

    public static void playTardisLightOnSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, 1.0F, 1.0F);
    }

    public static void playTardisLightOffSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.ENTITY_ENDER_EYE_LAUNCH, 1.0F, 1.0F);
    }

    public static void playTardisShieldsOnSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisShieldsOffSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisHandbrakeOnSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_CONTROL_HANDBRAKE_ON.get(), 1.0F, 1.0F);
    }

    public static void playTardisHandbrakeOffSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_CONTROL_HANDBRAKE_OFF.get(), 1.0F, 1.0F);
    }

    public static void playTardisComponentAddedSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisComponentRemovedSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisTakeoffSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_TAKEOFF.get(), 1.0F, 1.0F);
    }

    public static void playTardisLandingSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_LAND.get(), 1.0F, 1.0F);
    }

    public static void playTardisFlySound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_FLY.get(), 1.0F, 1.0F);
    }

    public static void playTardisFailSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_ERROR.get(), 1.0F, 1.0F);
    }

    public static void playTardisBellSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, TARDIS_BELL.get(), 1.0F, 1.0F);
    }

    public static void playScrewdriverMainSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SCREWDRIVER_MAIN.get(), 0.25F, 1.0F);
    }

    public static void playScrewdriverPutSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.ENTITY_ITEM_FRAME_ADD_ITEM, 1.0F, 1.0F);
    }

    public static void playScrewdriverPickupSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.ENTITY_ITEM_FRAME_REMOVE_ITEM, 1.0F, 1.0F);
    }

    public static void playTardisArsStructureCreatedSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisArsStructureDestroyedSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisTeleporterSentSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_DEACTIVATE, 1.0F, 1.0F);
    }

    public static void playTardisTeleporterReceivedSound(World world, BlockPos blockPos) {
        playSound(world, blockPos, SoundEvents.BLOCK_BEACON_ACTIVATE, 1.0F, 1.0F);
    }
}

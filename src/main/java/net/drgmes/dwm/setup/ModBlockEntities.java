package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota.TardisConsoleToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer.TardisRoomDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.RegistryObject;

public class ModBlockEntities {
    //////////////////////
    // Tardis Exteriors //
    //////////////////////

    public static final RegistryObject<BlockEntityType<TardisExteriorPoliceBoxBlockEntity>> TARDIS_EXTERIOR_POLICE_BOX = Registration.registerBlockEntity(
        "tardis_exterior_police_box",
        TardisExteriorPoliceBoxBlockEntity::new,
        ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.blockObject
    );

    //////////////////
    // Tardis Doors //
    //////////////////

    public static final RegistryObject<BlockEntityType<TardisDoorsPoliceBoxBlockEntity>> TARDIS_DOORS_POLICE_BOX = Registration.registerBlockEntity(
        "tardis_doors_police_box",
        TardisDoorsPoliceBoxBlockEntity::new,
        ModBlocks.TARDIS_DOORS_POLICE_BOX.blockObject
    );

    /////////////////////
    // Tardis Consoles //
    /////////////////////

    public static final RegistryObject<BlockEntityType<TardisConsoleToyotaBlockEntity>> TARDIS_CONSOLE_TOYOTA = Registration.registerBlockEntity(
        "tardis_console_toyota",
        TardisConsoleToyotaBlockEntity::new,
        ModBlocks.TARDIS_CONSOLE_TOYOTA.blockObject
    );

    ////////////////////
    // Tardis Engines //
    ////////////////////

    public static final RegistryObject<BlockEntityType<TardisEngineToyotaBlockEntity>> TARDIS_ENGINE_TOYOTA = Registration.registerBlockEntity(
        "tardis_engine_toyota",
        TardisEngineToyotaBlockEntity::new,
        ModBlocks.TARDIS_ENGINE_TOYOTA.blockObject
    );

    ////////////////////////
    // Tardis Decorations //
    ////////////////////////

    public static final RegistryObject<BlockEntityType<TardisToyotaSpinnerBlockEntity>> TARDIS_TOYOTA_SPINNER = Registration.registerBlockEntity(
        "tardis_toyota_spinner",
        TardisToyotaSpinnerBlockEntity::new,
        ModBlocks.TARDIS_TOYOTA_SPINNER.blockObject
    );

    ////////////
    // Others //
    ////////////

    public static final RegistryObject<BlockEntityType<TardisRoomDestroyerBlockEntity>> TARDIS_ROOM_DESTROYER = Registration.registerBlockEntity(
        "tardis_room_destroyer",
        TardisRoomDestroyerBlockEntity::new,
        ModBlocks.TARDIS_ROOM_DESTROYER.blockObject
    );

    public static void init() {
    }
}

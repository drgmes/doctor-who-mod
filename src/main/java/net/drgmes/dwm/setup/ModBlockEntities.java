package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota.TardisConsoleToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.minecraft.block.entity.BlockEntityType;

public class ModBlockEntities {

    // //////////////// //
    // Tardis Exteriors //
    // //////////////// //

    public static final BlockEntityType<TardisExteriorPoliceBoxBlockEntity> TARDIS_EXTERIOR_POLICE_BOX = Registration.registerBlockEntity(
        "tardis_exterior_police_box",
        TardisExteriorPoliceBoxBlockEntity::new,
        ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlock()
    );

    // //////////// //
    // Tardis Doors //
    // //////////// //

    public static final BlockEntityType<TardisDoorsPoliceBoxBlockEntity> TARDIS_DOORS_POLICE_BOX = Registration.registerBlockEntity(
        "tardis_doors_police_box",
        TardisDoorsPoliceBoxBlockEntity::new,
        ModBlocks.TARDIS_DOORS_POLICE_BOX.getBlock()
    );

    // /////////////// //
    // Tardis Consoles //
    // /////////////// //

    public static final BlockEntityType<TardisConsoleToyotaBlockEntity> TARDIS_CONSOLE_TOYOTA = Registration.registerBlockEntity(
        "tardis_console_toyota",
        TardisConsoleToyotaBlockEntity::new,
        ModBlocks.TARDIS_CONSOLE_TOYOTA.getBlock()
    );

    // ////////////// //
    // Tardis Engines //
    // ////////////// //

    public static final BlockEntityType<TardisEngineToyotaBlockEntity> TARDIS_ENGINE_TOYOTA = Registration.registerBlockEntity(
        "tardis_engine_toyota",
        TardisEngineToyotaBlockEntity::new,
        ModBlocks.TARDIS_ENGINE_TOYOTA.getBlock()
    );

    // //// //
    // Misc //
    // //// //

    public static final BlockEntityType<TardisArsDestroyerBlockEntity> TARDIS_ARS_DESTROYER = Registration.registerBlockEntity(
        "tardis_ars_destroyer",
        TardisArsDestroyerBlockEntity::new,
        ModBlocks.TARDIS_ARS_DESTROYER.getBlock()
    );

    public static final BlockEntityType<TardisToyotaSpinnerBlockEntity> TARDIS_TOYOTA_SPINNER = Registration.registerBlockEntity(
        "tardis_toyota_spinner",
        TardisToyotaSpinnerBlockEntity::new,
        ModBlocks.TARDIS_TOYOTA_SPINNER.getBlock()
    );

    public static void init() {
    }
}

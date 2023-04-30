package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.TardisConsoleUnitImperialBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.TardisConsoleUnitToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial.TardisEngineImperialBlockEntity;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.drgmes.dwm.compat.ModCompats;
import net.drgmes.dwm.compat.TechReborn;
import net.drgmes.dwm.utils.builders.BlockEntityBuilder;

import java.util.ArrayList;
import java.util.List;

public class ModBlockEntities {
    public static final List<BlockEntityBuilder<?>> BLOCK_ENTITY_BUILDERS = new ArrayList<>();

    // //////////////// //
    // Tardis Exteriors //
    // //////////////// //

    public static final BlockEntityBuilder<TardisExteriorPoliceBoxBlockEntity> TARDIS_EXTERIOR_POLICE_BOX = new BlockEntityBuilder<>(
        "tardis_exterior_police_box",
        TardisExteriorPoliceBoxBlockEntity::new,
        ModBlocks.TARDIS_EXTERIOR_POLICE_BOX::getBlock
    );

    // //////////// //
    // Tardis Doors //
    // //////////// //

    public static final BlockEntityBuilder<TardisDoorsPoliceBoxBlockEntity> TARDIS_DOORS_POLICE_BOX = new BlockEntityBuilder<>(
        "tardis_doors_police_box",
        TardisDoorsPoliceBoxBlockEntity::new,
        ModBlocks.TARDIS_DOORS_POLICE_BOX::getBlock
    );

    // /////////////// //
    // Tardis Consoles //
    // /////////////// //

    public static final BlockEntityBuilder<TardisConsoleUnitImperialBlockEntity> TARDIS_CONSOLE_UNIT_IMPERIAL = new BlockEntityBuilder<>(
        "tardis_console_unit_imperial",
        TardisConsoleUnitImperialBlockEntity::new,
        ModBlocks.TARDIS_CONSOLE_UNIT_IMPERIAL::getBlock
    );

    public static final BlockEntityBuilder<TardisConsoleUnitToyotaBlockEntity> TARDIS_CONSOLE_UNIT_TOYOTA = new BlockEntityBuilder<>(
        "tardis_console_unit_toyota",
        TardisConsoleUnitToyotaBlockEntity::new,
        ModBlocks.TARDIS_CONSOLE_UNIT_TOYOTA::getBlock
    );

    // ////////////// //
    // Tardis Engines //
    // ////////////// //

    public static final BlockEntityBuilder<TardisEngineImperialBlockEntity> TARDIS_ENGINE_IMPERIAL = new BlockEntityBuilder<>(
        "tardis_engine_imperial",
        TardisEngineImperialBlockEntity::new,
        ModBlocks.TARDIS_ENGINE_IMPERIAL::getBlock
    );

    public static final BlockEntityBuilder<TardisEngineToyotaBlockEntity> TARDIS_ENGINE_TOYOTA = new BlockEntityBuilder<>(
        "tardis_engine_toyota",
        TardisEngineToyotaBlockEntity::new,
        ModBlocks.TARDIS_ENGINE_TOYOTA::getBlock
    );

    // //// //
    // Misc //
    // //// //

    public static final BlockEntityBuilder<TardisArsDestroyerBlockEntity> TARDIS_ARS_DESTROYER = new BlockEntityBuilder<>(
        "tardis_ars_destroyer",
        TardisArsDestroyerBlockEntity::new,
        ModBlocks.TARDIS_ARS_DESTROYER::getBlock
    );

    public static final BlockEntityBuilder<TardisToyotaSpinnerBlockEntity> TARDIS_TOYOTA_SPINNER = new BlockEntityBuilder<>(
        "tardis_toyota_spinner",
        TardisToyotaSpinnerBlockEntity::new,
        ModBlocks.TARDIS_TOYOTA_SPINNER::getBlock
    );

    public static final BlockEntityBuilder<TardisRoundelBlockEntity> TARDIS_ROUNDEL = new BlockEntityBuilder<>(
        "tardis_roundel",
        TardisRoundelBlockEntity::new,
        ModBlocks.TARDIS_ROUNDEL::getBlock
    );

    public static void init() {
    }

    public static void setup() {
        if (ModCompats.techReborn()) {
            TechReborn.registerExternalTardisEnergyStorage(TARDIS_EXTERIOR_POLICE_BOX.getBlockEntityType());
            TechReborn.registerInternalTardisEnergyStorage(TARDIS_ROUNDEL.getBlockEntityType());
        }
    }
}

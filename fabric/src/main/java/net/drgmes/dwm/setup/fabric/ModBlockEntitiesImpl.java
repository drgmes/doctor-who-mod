package net.drgmes.dwm.setup.fabric;

import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.TardisConsoleUnitImperialBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.TardisConsoleUnitToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial.TardisEngineImperialBlockEntity;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.BlockEntityBuilder;

public class ModBlockEntitiesImpl {
    public static void init() {
        // //////////////// //
        // Tardis Exteriors //
        // //////////////// //

        ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX = new BlockEntityBuilder<>(
            "tardis_exterior_police_box",
            TardisExteriorPoliceBoxBlockEntity::new,
            ModBlocks.TARDIS_EXTERIOR_POLICE_BOX::getBlock
        );

        // /////////////// //
        // Tardis Consoles //
        // /////////////// //

        ModBlockEntities.TARDIS_CONSOLE_UNIT_IMPERIAL = new BlockEntityBuilder<>(
            "tardis_console_unit_imperial",
            TardisConsoleUnitImperialBlockEntity::new,
            ModBlocks.TARDIS_CONSOLE_UNIT_IMPERIAL::getBlock
        );

        ModBlockEntities.TARDIS_CONSOLE_UNIT_TOYOTA = new BlockEntityBuilder<>(
            "tardis_console_unit_toyota",
            TardisConsoleUnitToyotaBlockEntity::new,
            ModBlocks.TARDIS_CONSOLE_UNIT_TOYOTA::getBlock
        );

        // ////////////// //
        // Tardis Engines //
        // ////////////// //

        ModBlockEntities.TARDIS_ENGINE_IMPERIAL = new BlockEntityBuilder<>(
            "tardis_engine_imperial",
            TardisEngineImperialBlockEntity::new,
            ModBlocks.TARDIS_ENGINE_IMPERIAL::getBlock
        );

        ModBlockEntities.TARDIS_ENGINE_TOYOTA = new BlockEntityBuilder<>(
            "tardis_engine_toyota",
            TardisEngineToyotaBlockEntity::new,
            ModBlocks.TARDIS_ENGINE_TOYOTA::getBlock
        );

        // /////////// //
        // Tardis Misc //
        // /////////// //

        ModBlockEntities.TARDIS_ROUNDEL = new BlockEntityBuilder<>(
            "tardis_roundel",
            TardisRoundelBlockEntity::new,
            ModBlocks.TARDIS_ROUNDEL::getBlock
        );
    }
}

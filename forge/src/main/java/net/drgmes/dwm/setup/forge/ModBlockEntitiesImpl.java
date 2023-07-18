package net.drgmes.dwm.setup.forge;

import net.drgmes.dwm.forge.blockentities.*;
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
            TardisExteriorPoliceBoxBlockEntityForge::new,
            ModBlocks.TARDIS_EXTERIOR_POLICE_BOX::getBlock
        );

        // /////////////// //
        // Tardis Consoles //
        // /////////////// //

        ModBlockEntities.TARDIS_CONSOLE_UNIT_IMPERIAL = new BlockEntityBuilder<>(
            "tardis_console_unit_imperial",
            TardisConsoleUnitImperialBlockEntityForge::new,
            ModBlocks.TARDIS_CONSOLE_UNIT_IMPERIAL::getBlock
        );

        ModBlockEntities.TARDIS_CONSOLE_UNIT_TOYOTA = new BlockEntityBuilder<>(
            "tardis_console_unit_toyota",
            TardisConsoleUnitToyotaBlockEntityForge::new,
            ModBlocks.TARDIS_CONSOLE_UNIT_TOYOTA::getBlock
        );

        // ////////////// //
        // Tardis Engines //
        // ////////////// //

        ModBlockEntities.TARDIS_ENGINE_IMPERIAL = new BlockEntityBuilder<>(
            "tardis_engine_imperial",
            TardisEngineImperialBlockEntityForge::new,
            ModBlocks.TARDIS_ENGINE_IMPERIAL::getBlock
        );

        ModBlockEntities.TARDIS_ENGINE_TOYOTA = new BlockEntityBuilder<>(
            "tardis_engine_toyota",
            TardisEngineToyotaBlockEntityForge::new,
            ModBlocks.TARDIS_ENGINE_TOYOTA::getBlock
        );

        // /////////// //
        // Tardis Misc //
        // /////////// //

        ModBlockEntities.TARDIS_ROUNDEL = new BlockEntityBuilder<>(
            "tardis_roundel",
            TardisRoundelBlockEntityForge::new,
            ModBlocks.TARDIS_ROUNDEL::getBlock
        );
    }
}

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

        ModBlockEntities.TARDIS_EXTERIOR_CAPSULE = new BlockEntityBuilder<>(
            "tardis_exterior_capsule",
            TardisExteriorCapsuleBlockEntity::new,
            ModBlocks.TARDIS_EXTERIOR_CAPSULE::getBlock
        );

        ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX = new BlockEntityBuilder<>(
            "tardis_exterior_police_box",
            TardisExteriorPoliceBoxBlockEntity::new,
            ModBlocks.TARDIS_EXTERIOR_POLICE_BOX::getBlock
        );

        ModBlockEntities.TARDIS_EXTERIOR_PHONE_BOX = new BlockEntityBuilder<>(
            "tardis_exterior_phone_box",
            TardisExteriorPhoneBoxBlockEntity::new,
            ModBlocks.TARDIS_EXTERIOR_PHONE_BOX::getBlock
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

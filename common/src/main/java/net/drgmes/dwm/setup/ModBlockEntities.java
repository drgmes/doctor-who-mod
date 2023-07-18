package net.drgmes.dwm.setup;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.TardisConsoleUnitImperialBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.TardisConsoleUnitToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial.TardisEngineImperialBlockEntity;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.TardisTeleporterBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.drgmes.dwm.common.tardis.TardisEnergyManager;
import net.drgmes.dwm.utils.builders.BlockEntityBuilder;

import java.util.ArrayList;
import java.util.List;

public class ModBlockEntities {
    public static final List<BlockEntityBuilder<?>> BLOCK_ENTITY_BUILDERS = new ArrayList<>();

    // //////////// //
    // Tardis Doors //
    // //////////// //

    public static final BlockEntityBuilder<TardisDoorsPoliceBoxBlockEntity> TARDIS_DOORS_POLICE_BOX = new BlockEntityBuilder<>(
        "tardis_doors_police_box",
        TardisDoorsPoliceBoxBlockEntity::new,
        ModBlocks.TARDIS_DOORS_POLICE_BOX::getBlock
    );

    // //// //
    // Misc //
    // //// //

    public static final BlockEntityBuilder<TardisArsCreatorBlockEntity> TARDIS_ARS_CREATOR = new BlockEntityBuilder<>(
        "tardis_ars_creator",
        TardisArsCreatorBlockEntity::new,
        ModBlocks.TARDIS_ARS_CREATOR::getBlock
    );

    public static final BlockEntityBuilder<TardisArsDestroyerBlockEntity> TARDIS_ARS_DESTROYER = new BlockEntityBuilder<>(
        "tardis_ars_destroyer",
        TardisArsDestroyerBlockEntity::new,
        ModBlocks.TARDIS_ARS_DESTROYER::getBlock
    );

    public static final BlockEntityBuilder<TardisTeleporterBlockEntity> TARDIS_TELEPORTER = new BlockEntityBuilder<>(
        "tardis_teleporter",
        TardisTeleporterBlockEntity::new,
        ModBlocks.TARDIS_TELEPORTER::getBlock
    );

    public static final BlockEntityBuilder<TardisToyotaSpinnerBlockEntity> TARDIS_TOYOTA_SPINNER = new BlockEntityBuilder<>(
        "tardis_toyota_spinner",
        TardisToyotaSpinnerBlockEntity::new,
        ModBlocks.TARDIS_TOYOTA_SPINNER::getBlock
    );

    // //////////////// //
    // Tardis Exteriors //
    // //////////////// //

    public static BlockEntityBuilder<TardisExteriorPoliceBoxBlockEntity> TARDIS_EXTERIOR_POLICE_BOX;

    // /////////////// //
    // Tardis Consoles //
    // /////////////// //

    public static BlockEntityBuilder<TardisConsoleUnitImperialBlockEntity> TARDIS_CONSOLE_UNIT_IMPERIAL;
    public static BlockEntityBuilder<TardisConsoleUnitToyotaBlockEntity> TARDIS_CONSOLE_UNIT_TOYOTA;

    // ////////////// //
    // Tardis Engines //
    // ////////////// //

    public static BlockEntityBuilder<TardisEngineImperialBlockEntity> TARDIS_ENGINE_IMPERIAL;
    public static BlockEntityBuilder<TardisEngineToyotaBlockEntity> TARDIS_ENGINE_TOYOTA;

    // /////////// //
    // Tardis Misc //
    // /////////// //

    public static BlockEntityBuilder<TardisRoundelBlockEntity> TARDIS_ROUNDEL;

    @ExpectPlatform
    public static void init() {
        throw new AssertionError();
    }

    public static void setup() {
        TardisEnergyManager.register(TARDIS_EXTERIOR_POLICE_BOX::getBlockEntityType);
        TardisEnergyManager.register(TARDIS_CONSOLE_UNIT_IMPERIAL::getBlockEntityType);
        TardisEnergyManager.register(TARDIS_CONSOLE_UNIT_TOYOTA::getBlockEntityType);
        TardisEnergyManager.register(TARDIS_ENGINE_IMPERIAL::getBlockEntityType);
        TardisEnergyManager.register(TARDIS_ENGINE_TOYOTA::getBlockEntityType);
        TardisEnergyManager.register(TARDIS_ROUNDEL::getBlockEntityType);
    }
}

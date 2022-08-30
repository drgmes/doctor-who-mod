package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.common.engineertable.EngineerTableBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.TardisConsoleUnitImperialBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.TardisConsoleUnitToyotaBlockBuilder;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockBuilder;
import net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial.TardisEngineImperialBlockBuilder;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockBuilder;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;

import java.util.ArrayList;

public class ModBlocks {
    public static final ArrayList<BlockBuilder> BLOCK_BUILDERS = new ArrayList<>();

    // Common
    public static final BlockBuilder ENGINEER_TABLE = new EngineerTableBlockBuilder("engineer_table");

    // Tardis Exteriors
    public static final BlockBuilder TARDIS_EXTERIOR_POLICE_BOX = new TardisExteriorPoliceBoxBlockBuilder("tardis_exterior_police_box");

    // Tardis Doors
    public static final BlockBuilder TARDIS_DOORS_POLICE_BOX = new TardisDoorsPoliceBoxBlockBuilder("tardis_doors_police_box");

    // Tardis Consoles
    public static final BlockBuilder TARDIS_CONSOLE_UNIT_IMPERIAL = new TardisConsoleUnitImperialBlockBuilder("tardis_console_unit_imperial");
    public static final BlockBuilder TARDIS_CONSOLE_UNIT_TOYOTA = new TardisConsoleUnitToyotaBlockBuilder("tardis_console_unit_toyota");

    // Tardis Engines
    public static final BlockBuilder TARDIS_ENGINE_IMPERIAL = new TardisEngineImperialBlockBuilder("tardis_engine_imperial");
    public static final BlockBuilder TARDIS_ENGINE_TOYOTA = new TardisEngineToyotaBlockBuilder("tardis_engine_toyota");

    // Tardis Decorations
    public static final BlockBuilder TARDIS_TOYOTA_SPINNER = new TardisToyotaSpinnerBlockBuilder("tardis_toyota_spinner");

    // Misc
    public static final BlockBuilder TARDIS_ARS_CREATOR = new TardisArsCreatorBlockBuilder("tardis_ars_creator");
    public static final BlockBuilder TARDIS_ARS_DESTROYER = new TardisArsDestroyerBlockBuilder("tardis_ars_destroyer");

    public static void init() {
    }
}

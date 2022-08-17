package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota.TardisConsoleToyotaBlockBuilder;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockBuilder;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.TardisEngineToyotaBlockBuilder;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockBuilder;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockBuilder;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;

import java.util.ArrayList;

public class ModBlocks {
    public static final ArrayList<BlockBuilder> BLOCK_BUILDERS = new ArrayList<>();

    // Tardis Exteriors
    public static final BlockBuilder TARDIS_EXTERIOR_POLICE_BOX = new TardisExteriorPoliceBoxBlockBuilder("tardis_exterior_police_box");

    // Tardis Doors
    public static final BlockBuilder TARDIS_DOORS_POLICE_BOX = new TardisDoorsPoliceBoxBlockBuilder("tardis_doors_police_box");

    // Tardis Consoles
    public static final BlockBuilder TARDIS_CONSOLE_TOYOTA = new TardisConsoleToyotaBlockBuilder("tardis_console_toyota");

    // Tardis Engines
    public static final BlockBuilder TARDIS_ENGINE_TOYOTA = new TardisEngineToyotaBlockBuilder("tardis_engine_toyota");

    // Tardis Decorations
    public static final BlockBuilder TARDIS_TOYOTA_SPINNER = new TardisToyotaSpinnerBlockBuilder("tardis_toyota_spinner");

    // Misc
    public static final BlockBuilder TARDIS_ARS_CREATOR = new TardisArsCreatorBlockBuilder("tardis_ars_creator");
    public static final BlockBuilder TARDIS_ARS_DESTROYER = new TardisArsDestroyerBlockBuilder("tardis_ars_destroyer");

    public static void init() {
    }
}

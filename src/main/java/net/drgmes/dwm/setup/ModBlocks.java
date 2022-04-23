package net.drgmes.dwm.setup;

import java.util.ArrayList;

import net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.TardisConsoleToyotaBlockBuilder;
import net.drgmes.dwm.blocks.tardisdoor.TardisDoorBlockBuilder;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockBuilder;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;

public class ModBlocks {
    public static final ArrayList<BlockBuilder> BLOCK_BUILDERS = new ArrayList<>();

    public static final BlockBuilder TARDIS_EXTERIOR = new TardisExteriorBlockBuilder("tardis_exterior");
    public static final BlockBuilder TARDIS_DOOR = new TardisDoorBlockBuilder("tardis_door");

    // Tardis Consoles
    public static final BlockBuilder TARDIS_CONSOLE_TOYOTA = new TardisConsoleToyotaBlockBuilder("tardis_console_toyota");

    public static void init() {
    }
}

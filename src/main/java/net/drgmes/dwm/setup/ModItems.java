package net.drgmes.dwm.setup;

import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItemBuilder;
import net.drgmes.dwm.items.tardis.tardiskey.TardisKeyItemBuilder;
import net.drgmes.dwm.items.tardis.tardissystem.TardisSystemItemBuilder;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;

import java.util.ArrayList;

public class ModItems {
    public static final ArrayList<ItemBuilder> ITEM_BUILDERS = new ArrayList<>();

    // Screwdrivers
    public static final ItemBuilder SCREWDRIVER_11 = new ScrewdriverItemBuilder("screwdriver_11");
    public static final ItemBuilder SCREWDRIVER_12 = new ScrewdriverItemBuilder("screwdriver_12");
    public static final ItemBuilder SCREWDRIVER_13 = new ScrewdriverItemBuilder("screwdriver_13");

    // Tardis Keys
    public static final ItemBuilder KEY_1 = new TardisKeyItemBuilder("tardis_key_1");

    // Tardis Systems
    public static final ItemBuilder DEMATERIALIZATION_CIRCUIT_SYSTEM = new TardisSystemItemBuilder("dematerialization_circuit_system", TardisSystemMaterialization.class);
    public static final ItemBuilder DIRECTIONAL_UNIT_SYSTEM = new TardisSystemItemBuilder("directional_unit", TardisSystemFlight.class);

    public static void init() {
    }
}

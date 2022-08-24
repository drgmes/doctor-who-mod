package net.drgmes.dwm.setup;

import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItemBuilder;
import net.drgmes.dwm.items.tardiskey.TardisKeyItemBuilder;
import net.drgmes.dwm.items.tardissystem.TardisSystemItemBuilder;
import net.drgmes.dwm.items.tardissystem.dematerializationcircuit.TardisSystemDematerializationCircuitItemBuilder;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;

import java.util.ArrayList;

public class ModItems {
    public static final ArrayList<ItemBuilder> ITEM_BUILDERS = new ArrayList<>();

    // Screwdrivers
    public static final ItemBuilder SCREWDRIVER_11 = new ScrewdriverItemBuilder("screwdriver_11");
    public static final ItemBuilder SCREWDRIVER_12 = new ScrewdriverItemBuilder("screwdriver_12");
    public static final ItemBuilder SCREWDRIVER_13 = new ScrewdriverItemBuilder("screwdriver_13");

    // Tardis Keys
    public static final ItemBuilder TARDIS_KEY_1 = new TardisKeyItemBuilder("tardis_key_1");

    // Tardis Systems
    public static final ItemBuilder TARDIS_SYSTEM_DEMATERIALIZATION_CIRCUIT = new TardisSystemDematerializationCircuitItemBuilder("tardis_system_dematerialization_circuit", TardisSystemMaterialization.class);
    public static final ItemBuilder TARDIS_SYSTEM_DIRECTIONAL_UNIT = new TardisSystemItemBuilder("tardis_system_directional_unit", TardisSystemFlight.class);
    public static final ItemBuilder TARDIS_SYSTEM_SHIELDS_GENERATOR = new TardisSystemItemBuilder("tardis_system_shields_generator", TardisSystemShields.class);

    public static void init() {
    }
}

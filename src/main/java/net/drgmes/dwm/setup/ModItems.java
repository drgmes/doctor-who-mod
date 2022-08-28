package net.drgmes.dwm.setup;

import net.drgmes.dwm.items.common.repairkit.RepairKitItemBuilder;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItemBuilder;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItemBuilder;
import net.drgmes.dwm.items.tardis.systems.dematerializationcircuit.TardisSystemDematerializationCircuitItemBuilder;
import net.drgmes.dwm.items.tardis.systems.directionalunit.TardisSystemDirectionalUnitItemBuilder;
import net.drgmes.dwm.items.tardis.systems.shieldsgenerator.TardisSystemShieldsGeneratorItemBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;

import java.util.ArrayList;

public class ModItems {
    public static final ArrayList<ItemBuilder> ITEM_BUILDERS = new ArrayList<>();

    // Misc
    public static final ItemBuilder REPAIR_KIT = new RepairKitItemBuilder("repair_kit");

    // Screwdrivers
    public static final ItemBuilder SCREWDRIVER_11 = new ScrewdriverItemBuilder("screwdriver_11");
    public static final ItemBuilder SCREWDRIVER_12 = new ScrewdriverItemBuilder("screwdriver_12");
    public static final ItemBuilder SCREWDRIVER_13 = new ScrewdriverItemBuilder("screwdriver_13");

    // Tardis Keys
    public static final ItemBuilder TARDIS_KEY_1 = new TardisKeyItemBuilder("tardis_key_1");

    // Tardis Systems
    public static final ItemBuilder TARDIS_SYSTEM_DEMATERIALIZATION_CIRCUIT = new TardisSystemDematerializationCircuitItemBuilder("tardis_system_dematerialization_circuit");
    public static final ItemBuilder TARDIS_SYSTEM_DIRECTIONAL_UNIT = new TardisSystemDirectionalUnitItemBuilder("tardis_system_directional_unit");
    public static final ItemBuilder TARDIS_SYSTEM_SHIELDS_GENERATOR = new TardisSystemShieldsGeneratorItemBuilder("tardis_system_shields_generator");

    public static void init() {
    }
}

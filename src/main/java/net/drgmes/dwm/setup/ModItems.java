package net.drgmes.dwm.setup;

import net.drgmes.dwm.items.screwdriver.ScrewdriverItemBuilder;
import net.drgmes.dwm.items.tardiskey.TardisKeyItemBuilder;
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

    public static void init() {
    }
}

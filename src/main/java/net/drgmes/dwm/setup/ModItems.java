package net.drgmes.dwm.setup;

import java.util.ArrayList;

import net.drgmes.dwm.items.screwdriver.ScrewdriverItemBuilder;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;

public class ModItems {
    public static final ArrayList<ItemBuilder> ITEM_BUILDERS = new ArrayList<>();

    public static final ItemBuilder SCREWDRIVER_11 = new ScrewdriverItemBuilder("screwdriver_11");
    public static final ItemBuilder SCREWDRIVER_12 = new ScrewdriverItemBuilder("screwdriver_12");
    public static final ItemBuilder SCREWDRIVER_13 = new ScrewdriverItemBuilder("screwdriver_13");

    public static void init() {
    }
}

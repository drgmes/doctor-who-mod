package net.drgmes.dwm.setup;

import net.drgmes.dwm.items.armor.titanium.TitaniumArmorItemBuilder;
import net.drgmes.dwm.items.common.titaniumingot.TitaniumIngotItemBuilder;
import net.drgmes.dwm.items.common.titaniumnugget.TitaniumNuggetItemBuilder;
import net.drgmes.dwm.items.common.titaniumplate.TitaniumPlateItemBuilder;
import net.drgmes.dwm.items.common.titaniumraw.TitaniumRawItemBuilder;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItemBuilder;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItemBuilder;
import net.drgmes.dwm.items.tardis.misc.repairkit.RepairKitItemBuilder;
import net.drgmes.dwm.items.tardis.systems.dematerializationcircuit.TardisSystemDematerializationCircuitItemBuilder;
import net.drgmes.dwm.items.tardis.systems.directionalunit.TardisSystemDirectionalUnitItemBuilder;
import net.drgmes.dwm.items.tardis.systems.shieldsgenerator.TardisSystemShieldsGeneratorItemBuilder;
import net.drgmes.dwm.items.tools.titanium.*;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.item.ArmorItem;

import java.util.ArrayList;
import java.util.List;

public class ModItems {
    public static final List<ItemBuilder> ITEM_BUILDERS = new ArrayList<>();

    // Common
    public static final ItemBuilder TITANIUM_RAW = new TitaniumRawItemBuilder("titanium_raw_item");
    public static final ItemBuilder TITANIUM_INGOT = new TitaniumIngotItemBuilder("titanium_ingot");
    public static final ItemBuilder TITANIUM_PLATE = new TitaniumPlateItemBuilder("titanium_plate");
    public static final ItemBuilder TITANIUM_NUGGET = new TitaniumNuggetItemBuilder("titanium_nugget");

    // Armors
    public static final ItemBuilder TITANIUM_HELMET = new TitaniumArmorItemBuilder("titanium_helmet", ArmorItem.Type.HELMET);
    public static final ItemBuilder TITANIUM_CHESTPLATE = new TitaniumArmorItemBuilder("titanium_chestplate", ArmorItem.Type.CHESTPLATE);
    public static final ItemBuilder TITANIUM_LEGGINGS = new TitaniumArmorItemBuilder("titanium_leggings", ArmorItem.Type.LEGGINGS);
    public static final ItemBuilder TITANIUM_BOOTS = new TitaniumArmorItemBuilder("titanium_boots", ArmorItem.Type.BOOTS);

    // Tools
    public static final ItemBuilder TITANIUM_SWORD = new TitaniumSwordItemBuilder("titanium_sword");
    public static final ItemBuilder TITANIUM_AXE = new TitaniumAxeItemBuilder("titanium_axe");
    public static final ItemBuilder TITANIUM_SHOVEL = new TitaniumShovelItemBuilder("titanium_shovel");
    public static final ItemBuilder TITANIUM_PICKAXE = new TitaniumPickaxeItemBuilder("titanium_pickaxe");
    public static final ItemBuilder TITANIUM_HOE = new TitaniumHoeItemBuilder("titanium_hoe");

    // Tardis Misc
    public static final ItemBuilder REPAIR_KIT = new RepairKitItemBuilder("repair_kit");

    // Tardis Keys
    public static final ItemBuilder TARDIS_KEY_1 = new TardisKeyItemBuilder("tardis_key_1");

    // Screwdrivers
    public static final ItemBuilder SCREWDRIVER_11 = new ScrewdriverItemBuilder("screwdriver_11");
    public static final ItemBuilder SCREWDRIVER_12 = new ScrewdriverItemBuilder("screwdriver_12");
    public static final ItemBuilder SCREWDRIVER_13 = new ScrewdriverItemBuilder("screwdriver_13");

    // Tardis Systems
    public static final ItemBuilder TARDIS_SYSTEM_DIRECTIONAL_UNIT = new TardisSystemDirectionalUnitItemBuilder("tardis_system_directional_unit");
    public static final ItemBuilder TARDIS_SYSTEM_DEMATERIALIZATION_CIRCUIT = new TardisSystemDematerializationCircuitItemBuilder("tardis_system_dematerialization_circuit");
    public static final ItemBuilder TARDIS_SYSTEM_SHIELDS_GENERATOR = new TardisSystemShieldsGeneratorItemBuilder("tardis_system_shields_generator");

    public static void init() {
    }
}

package net.drgmes.dwm.items.screwdriver;

import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;

public class ScrewdriverItemBuilder extends ItemBuilder {
    public ScrewdriverItemBuilder(String name) {
        super(name, new ScrewdriverItem(getItemSettings()));
    }

    public static Item.Settings getItemSettings() {
        return ItemBuilder.getItemSettings().maxCount(1);
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getItem(), "item/screwdrivers/" + this.getName());
    }
}

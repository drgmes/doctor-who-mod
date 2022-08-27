package net.drgmes.dwm.items.tardis.keys;

import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;

public class TardisKeyItemBuilder extends ItemBuilder {
    public TardisKeyItemBuilder(String name) {
        super(name, new TardisKeyItem(getItemSettings()));
    }

    public static Item.Settings getItemSettings() {
        return ItemBuilder.getItemSettings().maxCount(1);
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getItem(), "item/tardis/keys/" + this.getName());
    }
}

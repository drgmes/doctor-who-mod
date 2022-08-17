package net.drgmes.dwm.items.tardissystem;

import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.item.Item;

public class TardisSystemItemBuilder extends ItemBuilder {
    public TardisSystemItemBuilder(String name, Class<? extends ITardisSystem> systemType) {
        super(name, new TardisSystemItem(getItemSettings(), systemType));
    }

    public static Item.Settings getItemSettings() {
        return ItemBuilder.getItemSettings().maxCount(1);
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getItem(), "item/tardis_systems/" + this.getName());
    }
}

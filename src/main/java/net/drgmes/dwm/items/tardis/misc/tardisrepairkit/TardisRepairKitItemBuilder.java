package net.drgmes.dwm.items.tardis.misc.tardisrepairkit;

import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.data.client.ItemModelGenerator;

public class TardisRepairKitItemBuilder extends ItemBuilder {
    public TardisRepairKitItemBuilder(String name) {
        super(name, new TardisRepairKitItem(getItemSettings()));
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getItem(), "item/tardis/misc/" + this.getName());
    }
}

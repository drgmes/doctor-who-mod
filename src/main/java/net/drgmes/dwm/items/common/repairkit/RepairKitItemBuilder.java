package net.drgmes.dwm.items.common.repairkit;

import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.data.client.ItemModelGenerator;

public class RepairKitItemBuilder extends ItemBuilder {
    public RepairKitItemBuilder(String name) {
        super(name, new RepairKitItem(getItemSettings()));
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getItem(), "item/common/" + this.getName());
    }
}

package net.drgmes.dwm.items.common.repairkit;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;

public class RepairKitItemBuilder extends ItemBuilder {
    public RepairKitItemBuilder(String name) {
        super(name, () -> new RepairKitItem(getItemSettings()));
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getItem(), this.getId(), DWM.getIdentifier("item/common/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }
}

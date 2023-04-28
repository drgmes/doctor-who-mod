package net.drgmes.dwm.items.screwdriver;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.item.Item;

public class ScrewdriverItemBuilder extends ItemBuilder {
    public ScrewdriverItemBuilder(String name) {
        super(name, () -> new ScrewdriverItem(getItemSettings()));
    }

    public static Item.Settings getItemSettings() {
        return ItemBuilder.getItemSettings().maxCount(1);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getItem(), this.getId(), DWM.getIdentifier("item/screwdrivers/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }
}

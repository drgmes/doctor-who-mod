package net.drgmes.dwm.items.tardis.keys;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.item.Item;

public class TardisKeyItemBuilder extends ItemBuilder {
    public TardisKeyItemBuilder(String name) {
        super(name, () -> new TardisKeyItem(getItemSettings()));
    }

    public static Item.Settings getItemSettings() {
        return ItemBuilder.getItemSettings().maxCount(1);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getItem(), this.getId(), DWM.getIdentifier("item/tardis/keys/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }
}

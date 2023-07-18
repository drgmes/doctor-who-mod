package net.drgmes.dwm.items.sonicdevices;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.item.Item;

public class SonicScrewdriverItemBuilder extends ItemBuilder {
    public SonicScrewdriverItemBuilder(String name) {
        super(name, () -> new SonicScrewdriverItem(getItemSettings()));
    }

    public static Item.Settings getItemSettings() {
        return ItemBuilder.getItemSettings().fireproof().maxCount(1);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getItem(), this.getId(), DWM.getIdentifier("item/sonic_devices/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }
}

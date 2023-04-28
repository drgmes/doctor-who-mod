package net.drgmes.dwm.items.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.item.Item;

public class TardisSystemItemBuilder extends ItemBuilder {
    public TardisSystemItemBuilder(String name, Class<? extends ITardisSystem> systemType) {
        super(name, () -> new TardisSystemItem(getItemSettings(), systemType));
    }

    public static Item.Settings getItemSettings() {
        return ItemBuilder.getItemSettings().maxCount(1);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getItem(), this.getId(), DWM.getIdentifier("item/tardis/systems/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }
}

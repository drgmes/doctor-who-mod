package net.drgmes.dwm.items.tardiskey;

import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;

public class TardisKeyItemBuilder extends ItemBuilder {
    public TardisKeyItemBuilder(String name) {
        super(name, () -> new TardisKeyItem(getItemProperties()));
    }

    public static Item.Properties getItemProperties() {
        return ItemBuilder.getItemProperties().stacksTo(1);
    }

    @Override
    public void registerItemModel(ModItemModelProvider provider) {
        ModelFile itemGenerated = provider.getExistingFile(provider.modLoc("item/tardis_keys/" + this.name));
        provider.getBuilder(this.name).parent(itemGenerated);
    }
}

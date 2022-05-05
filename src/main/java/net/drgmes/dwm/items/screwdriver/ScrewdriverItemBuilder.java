package net.drgmes.dwm.items.screwdriver;

import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.minecraftforge.client.model.generators.ModelFile;

public class ScrewdriverItemBuilder extends ItemBuilder {
    public ScrewdriverItemBuilder(String name) {
        super(name, () -> new ScrewdriverItem(getItemProperties()));
    }

    @Override
    public void registerItemModel(ModItemModelProvider provider) {
        ModelFile itemGenerated = provider.getExistingFile(provider.modLoc("item/screwdrivers/" + this.name));
        provider.getBuilder(this.name).parent(itemGenerated);
    }
}

package net.drgmes.dwm.datagen.common;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;

public class ModItemTagsProvider extends FabricTagProvider.ItemTagProvider {
    public ModItemTagsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        for (ItemBuilder itemBuilder : ModItems.ITEM_BUILDERS) {
            for (TagKey<Item> tag : itemBuilder.tags) {
                this.getTagBuilder(tag).add(DWM.getIdentifier(itemBuilder.getName()));
            }
        }
    }
}

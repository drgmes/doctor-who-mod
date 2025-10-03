package net.drgmes.dwm.items.sonicdevices;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;

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

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem(), 1)
            .input(this.getItem())
            .criterion("has_item", RecipeProvider.conditionsFromItem(this.getItem()))
            .offerTo(exporter);
    }
}

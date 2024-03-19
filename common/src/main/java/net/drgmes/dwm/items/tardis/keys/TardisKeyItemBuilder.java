package net.drgmes.dwm.items.tardis.keys;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;

public class TardisKeyItemBuilder extends ItemBuilder {
    public TardisKeyItemBuilder(String name) {
        super(name, () -> new TardisKeyItem(getItemSettings()));
    }

    public static Item.Settings getItemSettings() {
        return ItemBuilder.getItemSettings().fireproof().maxCount(1);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getItem(), this.getId(), DWM.getIdentifier("item/tardis/keys/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem(), 1)
            .input(this.getItem())
            .criterion("has_item", RecipeHelper.conditionsFromItem(this.getItem()))
            .offerTo(exporter);
    }
}

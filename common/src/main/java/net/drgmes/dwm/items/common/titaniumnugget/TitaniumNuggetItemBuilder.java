package net.drgmes.dwm.items.common.titaniumnugget;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.RecipeProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumNuggetItemBuilder extends ItemBuilder {
    public TitaniumNuggetItemBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("item/common/" + this.getName());
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem(), 9)
            .input(ModItems.TITANIUM_INGOT.getItem())
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
            .offerTo(exporter);
    }
}

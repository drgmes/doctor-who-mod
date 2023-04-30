package net.drgmes.dwm.items.common.titaniumplate;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumPlateItemBuilder extends ItemBuilder {
    public TitaniumPlateItemBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("item/common/" + this.getName());
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem())
            .input('#', ModItems.TITANIUM_INGOT.getItem())
            .pattern("##")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
            .offerTo(exporter);
    }
}

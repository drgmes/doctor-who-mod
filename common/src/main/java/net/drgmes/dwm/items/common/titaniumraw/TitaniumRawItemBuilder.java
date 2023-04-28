package net.drgmes.dwm.items.common.titaniumraw;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumRawItemBuilder extends ItemBuilder {
    public TitaniumRawItemBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("item/common/" + this.getName());
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem(), 9)
            .input(ModBlocks.TITANIUM_RAW.getBlock())
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_RAW.getBlock()))
            .offerTo(exporter);
    }
}

package net.drgmes.dwm.blocks.decorative.titaniumskin;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumSkinBlockBuilder extends BlockBuilder {
    public TitaniumSkinBlockBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock())
            .input('#', ModItems.TITANIUM_PLATE.getItem())
            .pattern("##")
            .pattern("##")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_PLATE.getItem()))
            .offerTo(exporter);
    }
}

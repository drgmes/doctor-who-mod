package net.drgmes.dwm.blocks.decorative.titaniumskinvent;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumSkinVentBlockBuilder extends BlockBuilder {
    public TitaniumSkinVentBlockBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 4)
            .input('#', ModItems.TITANIUM_PLATE.getItem())
            .input('0', ModItems.TITANIUM_INGOT.getItem())
            .pattern("###")
            .pattern("0 0")
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_PLATE.getItem()))
            .offerTo(exporter);
    }
}

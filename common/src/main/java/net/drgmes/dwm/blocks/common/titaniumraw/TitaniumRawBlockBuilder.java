package net.drgmes.dwm.blocks.common.titaniumraw;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumRawBlockBuilder extends BlockBuilder {
    public TitaniumRawBlockBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/common/" + this.getName());
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.BUILDING_BLOCKS, this.getBlock())
            .input('#', ModItems.TITANIUM_RAW.getItem())
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_RAW.getItem()))
            .offerTo(exporter);
    }
}

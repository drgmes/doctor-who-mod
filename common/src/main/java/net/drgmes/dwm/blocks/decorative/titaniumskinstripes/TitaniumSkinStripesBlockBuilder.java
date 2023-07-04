package net.drgmes.dwm.blocks.decorative.titaniumskinstripes;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumSkinStripesBlockBuilder extends BlockBuilder {
    public TitaniumSkinStripesBlockBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 7)
            .input('#', ModBlocks.TITANIUM_PANEL.getBlock())
            .input('0', Items.BLACK_DYE)
            .input('1', Items.YELLOW_DYE)
            .pattern("#0#")
            .pattern("###")
            .pattern("#1#")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_PANEL.getBlock()))
            .offerTo(exporter);
    }
}

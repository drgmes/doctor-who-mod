package net.drgmes.dwm.blocks.decorative.titaniumskinventdark;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumSkinVentDarkBlockBuilder extends BlockBuilder {
    public TitaniumSkinVentDarkBlockBuilder(String name) {
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
            .input('1', Items.COAL)
            .pattern("###")
            .pattern("010")
            .pattern("###")
            .criterion("has_titanium_panel", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_PLATE.getItem()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), Items.COAL.toString()));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 4)
            .input('#', ModBlocks.TITANIUM_SKIN_VENT.getBlock())
            .input('0', Items.COAL)
            .pattern(" # ")
            .pattern("#0#")
            .pattern(" # ")
            .criterion("has_titanium_skin_vent", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_SKIN_VENT.getBlock()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModBlocks.TITANIUM_SKIN_VENT.getName()));
    }
}

package net.drgmes.dwm.blocks.decorative.titaniumskinventdark;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumSkinVentDarkBlockBuilder extends BlockBuilder {
    public TitaniumSkinVentDarkBlockBuilder(String name) {
        super(name, ModCreativeTabs.DECORATIONS);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock(), 4)
            .input('#', ModItems.TITANIUM_PLATE.getItem())
            .input('0', ModItems.TITANIUM_INGOT.getItem())
            .input('1', Items.COAL)
            .pattern("###")
            .pattern("010")
            .pattern("###")
            .criterion("has_titanium_panel", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_PLATE.getItem()))
            .offerTo(exporter, provider.getConversionRecipeName(this.getName(), Items.COAL.toString()));

        ShapedRecipeJsonBuilder.create(this.getBlock(), 4)
            .input('#', ModBlocks.TITANIUM_SKIN_VENT.getBlock())
            .input('0', Items.COAL)
            .pattern(" # ")
            .pattern("#0#")
            .pattern(" # ")
            .criterion("has_titanium_skin_vent", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_SKIN_VENT.getBlock()))
            .offerTo(exporter, provider.getConversionRecipeName(this.getName(), ModBlocks.TITANIUM_SKIN_VENT.getName()));
    }
}

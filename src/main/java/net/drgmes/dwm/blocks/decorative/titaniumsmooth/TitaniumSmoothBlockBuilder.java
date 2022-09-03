package net.drgmes.dwm.blocks.decorative.titaniumsmooth;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumSmoothBlockBuilder extends BlockBuilder {
    public TitaniumSmoothBlockBuilder(String name) {
        super(name, ModCreativeTabs.DECORATIONS);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock())
            .input('#', ModItems.TITANIUM_INGOT.getItem())
            .pattern("##")
            .pattern("##")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
            .offerTo(exporter, provider.getConversionRecipeName(this.getName(), ModItems.TITANIUM_INGOT.getName()));
    }
}

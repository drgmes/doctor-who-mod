package net.drgmes.dwm.blocks.common.titaniumraw;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumRawBlockBuilder extends BlockBuilder {
    public TitaniumRawBlockBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/common/" + this.getName());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock())
            .input('#', ModItems.TITANIUM_RAW.getItem())
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_RAW.getItem()))
            .offerTo(exporter);
    }
}

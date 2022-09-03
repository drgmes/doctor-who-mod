package net.drgmes.dwm.blocks.decorative.titaniumskinvent;

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

public class TitaniumSkinVentBlockBuilder extends BlockBuilder {
    public TitaniumSkinVentBlockBuilder(String name) {
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
            .pattern("###")
            .pattern("0 0")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_PLATE.getItem()))
            .offerTo(exporter);
    }
}

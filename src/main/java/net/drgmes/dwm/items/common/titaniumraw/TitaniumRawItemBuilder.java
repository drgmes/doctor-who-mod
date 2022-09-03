package net.drgmes.dwm.items.common.titaniumraw;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapelessRecipeJsonBuilder;
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
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapelessRecipeJsonBuilder.create(this.getItem(), 9)
            .input(ModBlocks.TITANIUM_RAW.getBlock())
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_RAW.getBlock()))
            .offerTo(exporter);
    }
}

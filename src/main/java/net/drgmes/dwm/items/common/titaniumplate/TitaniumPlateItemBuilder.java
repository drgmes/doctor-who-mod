package net.drgmes.dwm.items.common.titaniumplate;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumPlateItemBuilder extends ItemBuilder {
    public TitaniumPlateItemBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("item/common/" + this.getName());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getItem())
            .input('#', ModItems.TITANIUM_INGOT.getItem())
            .pattern("##")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
            .offerTo(exporter);
    }
}

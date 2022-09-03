package net.drgmes.dwm.items.tardis.systems.directionalunit;

import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItemBuilder;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class TardisSystemDirectionalUnitItemBuilder extends TardisSystemItemBuilder {
    public TardisSystemDirectionalUnitItemBuilder(String name) {
        super(name, TardisSystemFlight.class);
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getItem())
            .input('i', Items.IRON_INGOT)
            .input('r', Items.REDSTONE)
            .input('a', Items.AMETHYST_SHARD)
            .input('e', Items.ENDER_EYE)
            .input('f', Items.FLINT)
            .pattern(" a ")
            .pattern("rer")
            .pattern("ifi")
            .criterion("has_item", RecipeProvider.conditionsFromItem(Items.IRON_INGOT))
            .offerTo(exporter);
    }
}

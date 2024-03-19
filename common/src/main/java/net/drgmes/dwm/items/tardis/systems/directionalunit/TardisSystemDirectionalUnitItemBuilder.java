package net.drgmes.dwm.items.tardis.systems.directionalunit;

import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class TardisSystemDirectionalUnitItemBuilder extends TardisSystemItemBuilder {
    public TardisSystemDirectionalUnitItemBuilder(String name) {
        super(name, TardisSystemFlight.class);
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem())
            .input('i', Items.IRON_INGOT)
            .input('r', Items.REDSTONE)
            .input('a', Items.AMETHYST_SHARD)
            .input('e', Items.ENDER_EYE)
            .input('f', Items.FLINT)
            .pattern(" a ")
            .pattern("rer")
            .pattern("ifi")
            .criterion("has_item", RecipeHelper.conditionsFromItem(Items.IRON_INGOT))
            .offerTo(exporter);
    }
}

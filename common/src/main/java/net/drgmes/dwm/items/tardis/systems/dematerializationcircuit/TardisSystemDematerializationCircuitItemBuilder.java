package net.drgmes.dwm.items.tardis.systems.dematerializationcircuit;

import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

public class TardisSystemDematerializationCircuitItemBuilder extends TardisSystemItemBuilder {
    public TardisSystemDematerializationCircuitItemBuilder(String name) {
        super(name, TardisSystemMaterialization.class);
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem())
            .input('i', Items.IRON_INGOT)
            .input('g', Items.GOLD_INGOT)
            .input('r', Items.REDSTONE)
            .input('d', Items.DIAMOND)
            .input('a', Items.AMETHYST_SHARD)
            .input('e', Items.ENDER_PEARL)
            .pattern("idi")
            .pattern("aea")
            .pattern("grg")
            .criterion("has_item", RecipeHelper.conditionsFromItem(Items.IRON_INGOT))
            .offerTo(exporter);
    }
}

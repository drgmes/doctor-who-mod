package net.drgmes.dwm.items.tardis.systems.shieldsgenerator;

import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class TardisSystemShieldsGeneratorItemBuilder extends TardisSystemItemBuilder {
    public TardisSystemShieldsGeneratorItemBuilder(String name) {
        super(name, TardisSystemShields.class);
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem())
            .input('i', Items.IRON_INGOT)
            .input('g', Items.GOLD_INGOT)
            .input('r', Items.REDSTONE)
            .input('a', Items.AMETHYST_SHARD)
            .pattern("grg")
            .pattern("iai")
            .pattern("grg")
            .criterion("has_item", RecipeHelper.conditionsFromItem(Items.IRON_INGOT))
            .offerTo(exporter);
    }
}

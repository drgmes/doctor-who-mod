package net.drgmes.dwm.items.tardis.systems.shieldsgenerator;

import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItemBuilder;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class TardisSystemShieldsGeneratorItemBuilder extends TardisSystemItemBuilder {
    public TardisSystemShieldsGeneratorItemBuilder(String name) {
        super(name, TardisSystemShields.class);
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getItem())
            .input('i', Items.IRON_INGOT)
            .input('g', Items.GOLD_INGOT)
            .input('r', Items.REDSTONE)
            .input('a', Items.AMETHYST_SHARD)
            .pattern("grg")
            .pattern("iai")
            .pattern("grg")
            .criterion("has_item", RecipeProvider.conditionsFromItem(Items.IRON_INGOT))
            .offerTo(exporter);
    }
}

package net.drgmes.dwm.items.tardis.systems.dematerializationcircuit;

import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItemBuilder;
import net.drgmes.dwm.items.tardis.systems.dematerializationcircuit.models.TardisSystemDematerializationCircuitModel;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;

import java.util.function.Consumer;

public class TardisSystemDematerializationCircuitItemBuilder extends TardisSystemItemBuilder {
    public TardisSystemDematerializationCircuitItemBuilder(String name) {
        super(name, TardisSystemMaterialization.class);
    }

    @Override
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisSystemDematerializationCircuitModel.LAYER_LOCATION, TardisSystemDematerializationCircuitModel::getTexturedModelData);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getItem(), new TardisSystemDematerializationCircuitItemRenderer());
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getItem())
            .input('i', Items.IRON_INGOT)
            .input('g', Items.GOLD_INGOT)
            .input('r', Items.REDSTONE)
            .input('d', Items.DIAMOND)
            .input('a', Items.AMETHYST_SHARD)
            .input('e', Items.ENDER_PEARL)
            .pattern("idi")
            .pattern("aea")
            .pattern("grg")
            .criterion("has_iron_ingot", RecipeProvider.conditionsFromItem(Items.IRON_INGOT))
            .offerTo(exporter);
    }
}

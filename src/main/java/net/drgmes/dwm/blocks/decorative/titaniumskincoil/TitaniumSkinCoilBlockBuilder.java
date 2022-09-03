package net.drgmes.dwm.blocks.decorative.titaniumskincoil;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumSkinCoilBlockBuilder extends BlockBuilder {
    public TitaniumSkinCoilBlockBuilder(String name) {
        super(name, ModCreativeTabs.DECORATIONS);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {

        ShapedRecipeJsonBuilder.create(this.getBlock(), 8)
            .input('#', ModBlocks.TITANIUM_PANEL.getBlock())
            .input('0', Items.COPPER_INGOT)
            .pattern("###")
            .pattern("#0#")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_PANEL.getBlock()))
            .offerTo(exporter);
    }
}

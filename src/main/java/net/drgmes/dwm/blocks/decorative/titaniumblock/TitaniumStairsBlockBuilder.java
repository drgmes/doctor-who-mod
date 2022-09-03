package net.drgmes.dwm.blocks.decorative.titaniumblock;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumStairsBlockBuilder extends BlockBuilder {
    public TitaniumStairsBlockBuilder(String name) {
        super(name, new StairsBlock(ModBlocks.TITANIUM_BLOCK.getBlock().getDefaultState(), TitaniumBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_BLOCK.getName());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createStairsBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.TITANIUM_BLOCK);
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), this.getId());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock(), 4)
            .input('#', ModBlocks.TITANIUM_BLOCK.getBlock())
            .pattern("#  ")
            .pattern("## ")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_BLOCK.getBlock()))
            .offerTo(exporter);
    }
}

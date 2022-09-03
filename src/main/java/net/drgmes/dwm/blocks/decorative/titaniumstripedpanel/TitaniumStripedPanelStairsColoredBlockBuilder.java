package net.drgmes.dwm.blocks.decorative.titaniumstripedpanel;

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
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumStripedPanelStairsColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumStripedPanelStairsColoredBlockBuilder(String name, Item dyeItem) {
        super(name, new StairsBlock(ModBlocks.TITANIUM_STRIPED_PANELS.getBlock(dyeItem).getDefaultState(), TitaniumStripedPanelColoredBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
        this.dyeItem = dyeItem;
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem).getName());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createStairsBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.TITANIUM_PANEL);
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), this.getId());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock(), 4)
            .input('#', ModBlocks.TITANIUM_STRIPED_PANELS.blockBuilders.get(this.dyeItem).getBlock())
            .pattern("#  ")
            .pattern("## ")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_STRIPED_PANELS.blockBuilders.get(this.dyeItem).getBlock()))
            .offerTo(exporter);
    }
}

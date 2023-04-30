package net.drgmes.dwm.blocks.decorative.titaniumstripedpanel;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.StairsBlock;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumStripedPanelStairsColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumStripedPanelStairsColoredBlockBuilder(String name, Item dyeItem) {
        super(name, () -> new StairsBlock(ModBlocks.TITANIUM_STRIPED_PANELS.getBlock(dyeItem).getDefaultState(), TitaniumStripedPanelColoredBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
        this.dyeItem = dyeItem;
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem).getName());
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.STAIRS, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.ALL, ModBlocks.TITANIUM_PANEL.getTexture());
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 4)
            .input('#', ModBlocks.TITANIUM_STRIPED_PANELS.blockBuilders.get(this.dyeItem).getBlock())
            .pattern("#  ")
            .pattern("## ")
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_STRIPED_PANELS.blockBuilders.get(this.dyeItem).getBlock()))
            .offerTo(exporter);
    }
}

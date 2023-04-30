package net.drgmes.dwm.blocks.decorative.titaniumstripedpanel;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumStripedPanelColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumStripedPanelColoredBlockBuilder(String name, Item dyeItem) {
        super(name, ModCreativeTabs.DECORATIONS);
        this.dyeItem = dyeItem;
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE_WITH_MODEL_AND_UNIQUE_SIDE, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.TOP, ModBlocks.TITANIUM_PANEL.getTexture());
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 8)
            .input('#', ModBlocks.TITANIUM_PANEL.getBlock())
            .input('0', this.dyeItem)
            .pattern("###")
            .pattern("#0#")
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_PANEL.getBlock()))
            .offerTo(exporter);
    }
}

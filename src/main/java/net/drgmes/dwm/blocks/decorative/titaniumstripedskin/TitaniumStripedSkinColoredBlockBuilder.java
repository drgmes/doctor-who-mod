package net.drgmes.dwm.blocks.decorative.titaniumstripedskin;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumStripedSkinColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumStripedSkinColoredBlockBuilder(String name, Item dyeItem) {
        super(name, ModCreativeTabs.DECORATIONS);
        this.dyeItem = dyeItem;
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createSimpleBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.TITANIUM_SKIN.getTexture());
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), this.getId());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock(), 8)
            .input('#', ModBlocks.TITANIUM_SKIN.getBlock())
            .input('0', this.dyeItem)
            .pattern("###")
            .pattern("#0#")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_SKIN.getBlock()))
            .offerTo(exporter);
    }
}

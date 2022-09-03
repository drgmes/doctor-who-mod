package net.drgmes.dwm.blocks.decorative.titaniumstripedpanel;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.block.WallBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumStripedPanelWallColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumStripedPanelWallColoredBlockBuilder(String name, Item dyeItem) {
        super(name, new WallBlock(TitaniumStripedPanelColoredBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
        this.dyeItem = dyeItem;
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem).getName());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createWallBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.TITANIUM_PANEL, true);
        ModelHelper.createWallXYItemModel(blockStateModelGenerator, this.getTexture(), ModBlocks.TITANIUM_PANEL.getTexture(), DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        BlockBuilder stripedBuilder = ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem);

        ShapedRecipeJsonBuilder.create(this.getBlock(), 6)
            .input('#', stripedBuilder.getBlock())
            .pattern("###")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(stripedBuilder.getBlock()))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.WALLS);
    }
}

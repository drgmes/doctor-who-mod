package net.drgmes.dwm.blocks.decorative.titaniumpanellamp;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableBlock;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumPanelLampColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumPanelLampColoredBlockBuilder(String name, Item dyeItem) {
        super(name, new BaseRotatableBlock(getBlockSettings()), ModCreativeTabs.DECORATIONS);
        this.dyeItem = dyeItem;
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().luminance((blockState) -> 16);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createRotatableBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.TITANIUM_PANEL.getTexture(), ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem).getTexture(), this.getTexture(), this.getTexture(), DWM.getIdentifier("block/decorative/titanium_panel_lamp"));
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), this.getId());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        BlockBuilder stripedBuilder = ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem);

        ShapedRecipeJsonBuilder.create(this.getBlock())
            .input('#', stripedBuilder.getBlock())
            .input('0', Items.DIAMOND)
            .input('1', Items.REDSTONE)
            .pattern("0")
            .pattern("1")
            .pattern("#")
            .criterion("has_titanium_striped_panel", RecipeProvider.conditionsFromItem(stripedBuilder.getBlock()))
            .offerTo(exporter, provider.getConversionRecipeName(this.getName(), stripedBuilder.getName()));

        ShapedRecipeJsonBuilder.create(this.getBlock())
            .input('#', ModBlocks.TITANIUM_PANEL_LAMP_BASE.getBlock())
            .input('0', this.dyeItem)
            .pattern("0")
            .pattern("#")
            .criterion("has_titanium_panel_lamp_base", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_PANEL_LAMP_BASE.getBlock()))
            .offerTo(exporter, provider.getConversionRecipeName(this.getName(), ModBlocks.TITANIUM_PANEL_LAMP_BASE.getName()));
    }
}

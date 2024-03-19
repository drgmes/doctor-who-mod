package net.drgmes.dwm.blocks.decorative.titaniumpanellamp;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableBlock;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumPanelLampColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumPanelLampColoredBlockBuilder(String name, Item dyeItem) {
        super(name, () -> new BaseRotatableBlock(getBlockSettings()));
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
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.ROTATABLE, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.TOP, ModBlocks.TITANIUM_PANEL.getTexture())
            .addBlockTexture(TextureKey.SIDE, ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem).getTexture())
            .addBlockTexture(TextureKey.FRONT, this.getTexture())
            .addBlockTexture(TextureKey.SOUTH, this.getTexture())
            .addBlockTexture(TextureKey.BOTTOM, DWM.getIdentifier("block/decorative/titanium_panel_lamp"));
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        BlockBuilder stripedBuilder = ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock())
            .input('#', stripedBuilder.getBlock())
            .input('0', Items.DIAMOND)
            .input('1', Items.REDSTONE)
            .pattern("0")
            .pattern("1")
            .pattern("#")
            .criterion("has_titanium_striped_panel", RecipeHelper.conditionsFromItem(stripedBuilder.getBlock()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), stripedBuilder.getName()));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock())
            .input('#', ModBlocks.TITANIUM_PANEL_LAMP_BASE.getBlock())
            .input('0', this.dyeItem)
            .pattern("0")
            .pattern("#")
            .criterion("has_titanium_panel_lamp_base", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_PANEL_LAMP_BASE.getBlock()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModBlocks.TITANIUM_PANEL_LAMP_BASE.getName()));
    }
}

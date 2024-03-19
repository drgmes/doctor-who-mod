package net.drgmes.dwm.blocks.decorative.titaniumskinlamp;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableBlock;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumSkinLampColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumSkinLampColoredBlockBuilder(String name, Item dyeItem) {
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
            .addBlockTexture(TextureKey.TOP, ModBlocks.TITANIUM_SKIN.getTexture())
            .addBlockTexture(TextureKey.SIDE, ModBlocks.TITANIUM_STRIPED_SKINS.get(this.dyeItem).getTexture())
            .addBlockTexture(TextureKey.FRONT, this.getTexture())
            .addBlockTexture(TextureKey.SOUTH, this.getTexture())
            .addBlockTexture(TextureKey.BOTTOM, DWM.getIdentifier("block/decorative/titanium_skin_lamp"));
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        BlockBuilder stripedBuilder = ModBlocks.TITANIUM_STRIPED_SKINS.get(this.dyeItem);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock())
            .input('#', stripedBuilder.getBlock())
            .input('0', Blocks.GLASS)
            .input('1', Items.REDSTONE)
            .pattern("0")
            .pattern("1")
            .pattern("#")
            .criterion("has_titanium_striped_skin", RecipeHelper.conditionsFromItem(stripedBuilder.getBlock()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), stripedBuilder.getName()));

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock())
            .input('#', ModBlocks.TITANIUM_SKIN_LAMP_BASE.getBlock())
            .input('0', this.dyeItem)
            .pattern("0")
            .pattern("#")
            .criterion("has_titanium_skin_lamp_base", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_SKIN_LAMP_BASE.getBlock()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModBlocks.TITANIUM_SKIN_LAMP_BASE.getName()));
    }
}

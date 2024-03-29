package net.drgmes.dwm.blocks.decorative.carbonfiber;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.WallBlock;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

public class CarbonFiberWallBlockBuilder extends BlockBuilder {
    public CarbonFiberWallBlockBuilder(String name) {
        super(name, () -> new WallBlock(CarbonFiberBlockBuilder.getBlockSettings()));
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.CARBON_FIBER.getName());
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.WALL, BlockModelDataBuilder.ItemType.WALL)
            .addItemTexture(TextureKey.ALL, ModBlocks.CARBON_FIBER.getTexture())
            .addItemTexture(TextureKey.CONTENT, DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 6)
            .input('#', ModBlocks.CARBON_FIBER.getBlock())
            .pattern("###")
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.CARBON_FIBER.getBlock()))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.WALLS);
    }
}

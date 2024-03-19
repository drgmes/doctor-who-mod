package net.drgmes.dwm.blocks.decorative.titaniumsmooth;

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

public class TitaniumSmoothWallBlockBuilder extends BlockBuilder {
    public TitaniumSmoothWallBlockBuilder(String name) {
        super(name, () -> new WallBlock(TitaniumSmoothBlockBuilder.getBlockSettings()));
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_SMOOTH.getName());
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.WALL, BlockModelDataBuilder.ItemType.WALL)
            .addItemTexture(TextureKey.ALL, ModBlocks.TITANIUM_SMOOTH.getTexture())
            .addItemTexture(TextureKey.CONTENT, DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 6)
            .input('#', ModBlocks.TITANIUM_SMOOTH.getBlock())
            .pattern("###")
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_SMOOTH.getBlock()))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.WALLS);
    }
}

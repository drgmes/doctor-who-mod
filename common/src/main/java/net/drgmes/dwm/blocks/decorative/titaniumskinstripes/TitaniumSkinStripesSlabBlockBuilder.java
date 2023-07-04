package net.drgmes.dwm.blocks.decorative.titaniumskinstripes;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumSkinStripesSlabBlockBuilder extends BlockBuilder {
    public TitaniumSkinStripesSlabBlockBuilder(String name) {
        super(name, () -> new SlabBlock(TitaniumSkinStripesBlockBuilder.getBlockSettings()));
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_SKIN.getName());
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SOLID_SLAB, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.ALL, ModBlocks.TITANIUM_SKIN_STRIPES.getTexture())
            .addBlockTexture(TextureKey.CONTENT, ModBlocks.TITANIUM_SKIN_STRIPES.getId());
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 6)
            .input('#', ModBlocks.TITANIUM_SKIN_STRIPES.getBlock())
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_SKIN_STRIPES.getBlock()))
            .offerTo(exporter);
    }
}

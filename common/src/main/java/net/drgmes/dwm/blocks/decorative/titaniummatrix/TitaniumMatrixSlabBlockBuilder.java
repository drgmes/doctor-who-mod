package net.drgmes.dwm.blocks.decorative.titaniummatrix;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumMatrixSlabBlockBuilder extends BlockBuilder {
    public TitaniumMatrixSlabBlockBuilder(String name) {
        super(name, () -> new SlabBlock(TitaniumMatrixBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + this.getName());
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SOLID_SLAB, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.ALL, ModBlocks.TITANIUM_MATRIX.getTexture())
            .addBlockTexture(TextureKey.CONTENT, ModBlocks.TITANIUM_MATRIX.getId());
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 6)
            .input('#', ModBlocks.TITANIUM_MATRIX.getBlock())
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_MATRIX.getBlock()))
            .offerTo(exporter);
    }
}
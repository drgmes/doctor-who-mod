package net.drgmes.dwm.blocks.decorative.titaniummatrix;

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
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumMatrixWallBlockBuilder extends BlockBuilder {
    public TitaniumMatrixWallBlockBuilder(String name) {
        super(name, new WallBlock(TitaniumMatrixBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_MATRIX.getName());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createWallBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.TITANIUM_MATRIX, false);
        ModelHelper.createWallItemModel(blockStateModelGenerator, ModBlocks.TITANIUM_MATRIX.getTexture(), DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock(), 6)
            .input('#', ModBlocks.TITANIUM_MATRIX.getBlock())
            .pattern("###")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_MATRIX.getBlock()))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.WALLS);
    }
}

package net.drgmes.dwm.blocks.decorative.titaniumskincoil;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumSkinCoilSlabBlockBuilder extends BlockBuilder {
    public TitaniumSkinCoilSlabBlockBuilder(String name) {
        super(name, new SlabBlock(TitaniumSkinCoilBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_SKIN.getName());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createSlabBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.TITANIUM_SKIN_COIL);
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), this.getId());
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock(), 6)
            .input('#', ModBlocks.TITANIUM_SKIN_COIL.getBlock())
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_SKIN_COIL.getBlock()))
            .offerTo(exporter);
    }
}

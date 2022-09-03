package net.drgmes.dwm.blocks.decorative.carbonfiber;

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

public class CarbonFiberWallBlockBuilder extends BlockBuilder {
    public CarbonFiberWallBlockBuilder(String name) {
        super(name, new WallBlock(CarbonFiberBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.CARBON_FIBER.getName());
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createWallBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.CARBON_FIBER, false);
        ModelHelper.createWallItemModel(blockStateModelGenerator, ModBlocks.CARBON_FIBER.getTexture(), DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock(), 6)
            .input('#', ModBlocks.CARBON_FIBER.getBlock())
            .pattern("###")
            .pattern("###")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.CARBON_FIBER.getBlock()))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.WALLS);
    }
}

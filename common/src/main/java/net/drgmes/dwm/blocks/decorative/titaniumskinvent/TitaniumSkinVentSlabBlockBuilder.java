package net.drgmes.dwm.blocks.decorative.titaniumskinvent;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.SlabBlock;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumSkinVentSlabBlockBuilder extends BlockBuilder {
    public TitaniumSkinVentSlabBlockBuilder(String name) {
        super(name, () -> new SlabBlock(TitaniumSkinVentBlockBuilder.getBlockSettings()));
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_SKIN.getName());
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SOLID_SLAB, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.ALL, ModBlocks.TITANIUM_SKIN_VENT.getTexture())
            .addBlockTexture(TextureKey.CONTENT, ModBlocks.TITANIUM_SKIN_VENT.getId());
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 6)
            .input('#', ModBlocks.TITANIUM_SKIN_VENT.getBlock())
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_SKIN_VENT.getBlock()))
            .offerTo(exporter);
    }
}

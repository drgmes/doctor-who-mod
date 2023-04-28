package net.drgmes.dwm.blocks.decorative.titaniumstripedskin;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.WallBlock;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumStripedSkinWallColoredBlockBuilder extends BlockBuilder {
    private final Item dyeItem;

    public TitaniumStripedSkinWallColoredBlockBuilder(String name, Item dyeItem) {
        super(name, () -> new WallBlock(TitaniumStripedSkinColoredBlockBuilder.getBlockSettings()), ModCreativeTabs.DECORATIONS);
        this.dyeItem = dyeItem;
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/decorative/" + ModBlocks.TITANIUM_STRIPED_SKINS.get(this.dyeItem).getName());
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.WALL, BlockModelDataBuilder.ItemType.WALL_XY)
            .addBlockTexture(TextureKey.ALL, ModBlocks.TITANIUM_SKIN.getTexture())
            .addItemTexture(TextureKey.ALL, ModBlocks.TITANIUM_SKIN.getTexture())
            .addItemTexture(TextureKey.SIDE, this.getTexture())
            .addItemTexture(TextureKey.CONTENT, DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        BlockBuilder stripedBuilder = ModBlocks.TITANIUM_STRIPED_SKINS.get(this.dyeItem);

        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock(), 6)
            .input('#', stripedBuilder.getBlock())
            .pattern("###")
            .pattern("###")
            .criterion("has_item", RecipeHelper.conditionsFromItem(stripedBuilder.getBlock()))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.WALLS);
    }
}

package net.drgmes.dwm.blocks.decorative.titaniumdoor;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;
import net.minecraft.recipe.book.RecipeCategory;

import java.util.function.Consumer;

public class TitaniumDoorColoredBlockBuilder extends TitaniumDoorBlockBuilder {
    private final Item dyeItem;

    public TitaniumDoorColoredBlockBuilder(String name, Item dyeItem) {
        super(name);
        this.dyeItem = dyeItem;
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.DOOR, BlockModelDataBuilder.ItemType.GENERATED)
            .addBlockTexture(TextureKey.TOP, ModBlocks.TITANIUM_DOOR_BASE.getTexture())
            .addBlockTexture(TextureKey.BOTTOM, ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem).getTexture())
            .addItemTexture(TextureKey.ALL, DWM.getIdentifier("item/decorative/" + this.getName()))
            .addItemTexture(TextureKey.CONTENT, DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock())
            .input('#', ModBlocks.TITANIUM_DOOR_BASE.getBlock())
            .input('0', this.dyeItem)
            .pattern("#")
            .pattern("0")
            .criterion("has_item", RecipeHelper.conditionsFromItem(ModBlocks.TITANIUM_DOOR_BASE.getBlock()))
            .offerTo(exporter);
    }
}

package net.drgmes.dwm.blocks.decorative.titaniumdoor;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Item;

import java.util.function.Consumer;

public class TitaniumDoorColoredBlockBuilder extends TitaniumDoorBlockBuilder {
    private final Item dyeItem;

    public TitaniumDoorColoredBlockBuilder(String name, Item dyeItem) {
        super(name);
        this.dyeItem = dyeItem;
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createDoorBlockStateAndModel(blockStateModelGenerator, this, ModBlocks.TITANIUM_DOOR_BASE.getTexture(), ModBlocks.TITANIUM_STRIPED_PANELS.get(this.dyeItem).getTexture());
        ModelHelper.createGeneratedItemModel(blockStateModelGenerator, DWM.getIdentifier("item/decorative/" + this.getName()), DWM.getIdentifier("item/" + this.getName()));
    }

    @Override
    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock())
            .input('#', ModBlocks.TITANIUM_DOOR_BASE.getBlock())
            .input('0', this.dyeItem)
            .pattern("#")
            .pattern("0")
            .criterion("has_item", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_DOOR_BASE.getBlock()))
            .offerTo(exporter);
    }
}

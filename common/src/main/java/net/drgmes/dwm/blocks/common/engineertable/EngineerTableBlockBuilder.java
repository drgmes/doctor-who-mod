package net.drgmes.dwm.blocks.common.engineertable;

import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Consumer;

public class EngineerTableBlockBuilder extends BlockBuilder {
    public EngineerTableBlockBuilder(String name) {
        super(name, () -> new EngineerTableBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).strength(1.0f).nonOpaque();
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.ROTATABLE_WITHOUT_MODEL, BlockModelDataBuilder.ItemType.PARENTED);
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.DECORATIONS, this.getBlock())
            .input('#', Blocks.CRAFTING_TABLE)
            .input('0', Blocks.CHEST)
            .input('1', Items.COPPER_INGOT)
            .input('2', Items.IRON_INGOT)
            .input('3', Items.GOLD_INGOT)
            .input('4', ItemTags.PLANKS)
            .pattern("123")
            .pattern("4#4")
            .pattern("404")
            .criterion("has_planks", RecipeHelper.conditionsFromTag(ItemTags.PLANKS))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        this.tags.add(BlockTags.AXE_MINEABLE);
    }
}

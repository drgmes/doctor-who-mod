package net.drgmes.dwm.blocks.common.engineertable;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.Material;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.server.RecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.Items;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;

import java.util.function.Consumer;

public class EngineerTableBlockBuilder extends BlockBuilder {
    public EngineerTableBlockBuilder(String name) {
        super(name, new EngineerTableBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).strength(1.0f).nonOpaque();
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(this.getBlock());
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), DWM.getIdentifier("block/" + this.getName()));
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(this.getBlock())
            .input('i', Items.IRON_INGOT)
            .input('c', Items.COPPER_INGOT)
            .input('g', Items.GOLD_INGOT)
            .input('p', ItemTags.PLANKS)
            .input('w', Blocks.CRAFTING_TABLE)
            .input('s', Blocks.CHEST)
            .pattern("icg")
            .pattern("pwp")
            .pattern("psp")
            .criterion("has_planks", RecipeProvider.conditionsFromTag(ItemTags.PLANKS))
            .offerTo(exporter);
    }

    @Override
    public void registerTags() {
        this.tags.add(BlockTags.AXE_MINEABLE);
    }
}

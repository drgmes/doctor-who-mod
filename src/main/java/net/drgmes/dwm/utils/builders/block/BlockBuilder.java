package net.drgmes.dwm.utils.builders.block;

import net.drgmes.dwm.datagen.common.ModLootTableProvider;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.TagKey;

import java.util.ArrayList;
import java.util.function.Consumer;

public class BlockBuilder {
    public final String name;
    public final ArrayList<TagKey<Block>> tags = new ArrayList<>();

    private final Block block;
    private final Item blockItem;

    public BlockBuilder(String name, Block block, Item item) {
        this.name = name;
        this.block = Registration.registerBlock(name, block);
        this.blockItem = Registration.registerItem(name, item);

        this.registerTags();
        ModBlocks.BLOCK_BUILDERS.add(this);
    }

    public BlockBuilder(String name, Block block) {
        this(name, block, new BlockItem(block, new FabricItemSettings().group(ModCreativeTabs.GENERAL)));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(0.5f);
    }

    public String getName() {
        return this.name;
    }

    public Block getBlock() {
        return this.block;
    }

    public Item getBlockItem() {
        return this.blockItem;
    }

    public void registerCustomRender() {
    }

    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
    }

    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
    }

    public void registerDrop(ModLootTableProvider modLootTableProvider) {
        modLootTableProvider.addDrop(this.getBlock());
    }

    public void registerTags() {
    }
}

package net.drgmes.dwm.utils.builders;

import dev.architectury.registry.CreativeTabRegistry;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockLootDataBuilder;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class BlockBuilder {
    public final List<TagKey<Block>> tags = new ArrayList<>();
    public final Supplier<Block> block;
    public final Supplier<Item> blockItem;

    private final String name;

    public BlockBuilder(String name, Supplier<Block> blockSupplier, CreativeTabRegistry.TabSupplier tab) {
        this.name = name;
        this.block = Registration.registerBlock(name, blockSupplier);
        this.blockItem = Registration.registerItem(name, () -> new BlockItem(this.getBlock(), new Item.Settings().arch$tab(tab)));

        this.registerTags();
        ModBlocks.BLOCK_BUILDERS.add(this);
    }

    public BlockBuilder(String name, CreativeTabRegistry.TabSupplier tab) {
        this(name, () -> new Block(getBlockSettings()), tab);
    }

    public BlockBuilder(String name, Supplier<Block> blockSupplier) {
        this(name, blockSupplier, ModCreativeTabs.GENERAL);
    }

    public BlockBuilder(String name) {
        this(name, ModCreativeTabs.GENERAL);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.of(Material.METAL).sounds(BlockSoundGroup.METAL).strength(2.0f);
    }

    public Identifier getId() {
        return DWM.getIdentifier("block/" + this.getName());
    }

    public Identifier getTexture() {
        return this.getId();
    }

    public String getName() {
        return this.name;
    }

    public Block getBlock() {
        return this.block.get();
    }

    public Item getBlockItem() {
        return this.blockItem.get();
    }

    @Environment(EnvType.CLIENT)
    public RenderLayer getRenderLayer() {
        return null;
    }

    public BlockLootDataBuilder getBlockLootDataBuilder() {
        return new BlockLootDataBuilder(this, BlockLootDataBuilder.LootType.SELF);
    }

    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE_WITH_MODEL, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.ALL, this.getTexture());
    }

    public ItemModelDataBuilder getItemModelDataBuilder() {
        return null;
    }

    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
    }

    public void registerTags() {
        this.tags.add(BlockTags.PICKAXE_MINEABLE);
    }
}

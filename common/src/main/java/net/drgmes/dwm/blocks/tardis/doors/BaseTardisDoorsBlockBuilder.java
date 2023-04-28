package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockLootDataBuilder;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.TextureKey;
import net.minecraft.registry.tag.BlockTags;

import java.util.function.Supplier;

public abstract class BaseTardisDoorsBlockBuilder extends BlockBuilder {
    public BaseTardisDoorsBlockBuilder(String name, Supplier<BaseTardisDoorsBlock<?>> blockSupplier) {
        super(name, blockSupplier::get);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().nonOpaque();
    }

    @Override
    public BlockLootDataBuilder getBlockLootDataBuilder() {
        return new BlockLootDataBuilder(this, BlockLootDataBuilder.LootType.DOOR);
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE)
            .addBlockTexture(TextureKey.ALL, DWM.MODELS.BLOCK_INVISIBLE);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getBlockItem(), this.getId(), DWM.getIdentifier("item/block/tardis/doors/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }

    @Override
    public void registerTags() {
        this.tags.add(BlockTags.AXE_MINEABLE);
        this.tags.add(BlockTags.IMPERMEABLE);
        this.tags.add(BlockTags.DOORS);
    }
}

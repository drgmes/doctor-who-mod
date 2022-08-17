package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.datagen.common.ModLootTableProvider;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.tag.BlockTags;

public abstract class BaseTardisDoorsBlockBuilder extends BlockBuilder {
    public BaseTardisDoorsBlockBuilder(String name, BaseTardisDoorsBlock<?> block) {
        super(name, block);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().nonOpaque();
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        ModelHelper.createBlockStateWithModel(blockStateModelGenerator, this.getBlock(), ModelHelper.BLOCK_INVISIBLE);
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getBlockItem(), "item/tardis/doors/" + this.getName());
    }

    public void registerDrop(ModLootTableProvider modLootTableProvider) {
        modLootTableProvider.addDrop(this.getBlock(), BlockLootTableGenerator.doorDrops(this.getBlock()));
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.DOORS);
        this.tags.add(BlockTags.IMPERMEABLE);
    }
}

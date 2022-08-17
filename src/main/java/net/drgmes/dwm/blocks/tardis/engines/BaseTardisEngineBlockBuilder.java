package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public abstract class BaseTardisEngineBlockBuilder extends BlockBuilder {
    public BaseTardisEngineBlockBuilder(String name, BaseTardisEngineBlock factory) {
        super(name, factory);
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
        ModelHelper.createItemModel(itemModelGenerator, this.getBlockItem(), "item/tardis/engines/" + this.getName());
    }
}

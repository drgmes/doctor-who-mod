package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.helpers.BlockStateHelper;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;

public abstract class BaseTardisConsoleUnitBlockBuilder extends BlockBuilder {
    public BaseTardisConsoleUnitBlockBuilder(String name, BaseTardisConsoleUnitBlock<?> factory) {
        super(name, factory);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().nonOpaque();
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        BlockStateHelper.createSimpleBlockStateWithModel(blockStateModelGenerator, this, ModelHelper.BLOCK_INVISIBLE);
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getBlockItem(), "item/block/tardis/console_units/" + this.getName());
    }
}

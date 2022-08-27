package net.drgmes.dwm.blocks.tardis.misc.tardisarscreator;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.data.client.BlockStateModelGenerator;

public class TardisArsCreatorBlockBuilder extends BlockBuilder {
    public TardisArsCreatorBlockBuilder(String name) {
        super(name, new TardisArsCreatorBlock(getBlockSettings()));
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleState(this.getBlock());
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), DWM.getIdentifier("block/" + this.getName()));
    }
}

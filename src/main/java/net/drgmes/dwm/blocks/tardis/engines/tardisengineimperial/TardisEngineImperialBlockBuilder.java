package net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockBuilder;
import net.minecraft.data.client.BlockStateModelGenerator;

public class TardisEngineImperialBlockBuilder extends BaseTardisEngineBlockBuilder {
    public TardisEngineImperialBlockBuilder(String name) {
        super(name, new TardisEngineImperialBlock(getBlockSettings()));
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerSimpleState(this.getBlock());
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), DWM.getIdentifier("block/" + this.getName()));
    }
}

package net.drgmes.dwm.blocks.tardis.misc.tardisarscreator;

import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.data.client.TextureKey;

public class TardisArsCreatorBlockBuilder extends BlockBuilder {
    public TardisArsCreatorBlockBuilder(String name) {
        super(name, () -> new TardisArsCreatorBlock(getBlockSettings()));
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.ALL, this.getTexture());
    }
}

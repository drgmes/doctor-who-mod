package net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockBuilder;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.minecraft.data.client.TextureKey;

public class TardisEngineImperialBlockBuilder extends BaseTardisEngineBlockBuilder {
    public TardisEngineImperialBlockBuilder(String name) {
        super(name, () -> new TardisEngineImperialBlock(getBlockSettings()));
    }

    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.ALL, this.getTexture());
    }

    public ItemModelDataBuilder getItemModelDataBuilder() {
        return null;
    }
}

package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer;

import net.drgmes.dwm.datagen.BlockLootDataBuilder;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.TextureKey;

public class TardisArsDestroyerBlockBuilder extends BlockBuilder {
    public TardisArsDestroyerBlockBuilder(String name) {
        super(name, () -> new TardisArsDestroyerBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.copy(Blocks.BEDROCK);
    }

    @Override
    public BlockLootDataBuilder getBlockLootDataBuilder() {
        return null;
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE, BlockModelDataBuilder.ItemType.PARENTED)
            .addBlockTexture(TextureKey.ALL, this.getTexture());
    }

    @Override
    public void registerTags() {
    }
}

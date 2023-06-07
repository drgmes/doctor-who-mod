package net.drgmes.dwm.blocks.tardis.misc.tardisteleporter;

import net.drgmes.dwm.datagen.BlockLootDataBuilder;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.data.client.TextureKey;

public class TardisTeleporterBlockBuilder extends BlockBuilder {
    public TardisTeleporterBlockBuilder(String name) {
        super(name, () -> new TardisTeleporterBlock(getBlockSettings()));
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

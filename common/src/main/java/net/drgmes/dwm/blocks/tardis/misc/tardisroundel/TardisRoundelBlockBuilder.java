package net.drgmes.dwm.blocks.tardis.misc.tardisroundel;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.TextureKey;

public class TardisRoundelBlockBuilder extends BlockBuilder {
    public TardisRoundelBlockBuilder(String name) {
        super(name, () -> new TardisRoundelBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().nonOpaque().luminance((blockState) -> (
            blockState.get(TardisRoundelBlock.LIT) ? 15 : 0
        ));
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE)
            .addBlockTexture(TextureKey.ALL, DWM.MODELS.BLOCK_INVISIBLE);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getBlockItem(), this.getId(), DWM.getIdentifier("item/block/tardis/misc/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }
}

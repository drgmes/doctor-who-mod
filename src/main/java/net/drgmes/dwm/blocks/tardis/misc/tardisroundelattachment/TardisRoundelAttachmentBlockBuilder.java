package net.drgmes.dwm.blocks.tardis.misc.tardisroundelattachment;

import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.tag.BlockTags;

public class TardisRoundelAttachmentBlockBuilder extends BlockBuilder {
    public TardisRoundelAttachmentBlockBuilder(String name, boolean doLit) {
        super(name, new TardisRoundelAttachmentBlock(getBlockSettings(), doLit), ModCreativeTabs.DECORATIONS);
    }

    public TardisRoundelAttachmentBlockBuilder(String name) {
        this(name, false);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().nonOpaque().luminance((blockState) -> (
            blockState.get(TardisRoundelAttachmentBlock.LIT) ? 16 : 0
        ));
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(this.getBlock());
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), this.getId());
    }

    @Override
    public void registerTags() {
        this.tags.add(BlockTags.AXE_MINEABLE);
    }
}

package net.drgmes.dwm.data.common;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, DWM.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            for (TagKey<Block> tag : blockBuilder.tags) {
                this.tag(tag).add((Block) blockBuilder.blockObject.get());
            }
        }
    }
}

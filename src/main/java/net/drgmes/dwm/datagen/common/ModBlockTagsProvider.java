package net.drgmes.dwm.datagen.common;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.block.Block;
import net.minecraft.tag.TagKey;

public class ModBlockTagsProvider extends FabricTagProvider.BlockTagProvider {
    public ModBlockTagsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    @Override
    protected void generateTags() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            for (TagKey<Block> tag : blockBuilder.tags) {
                this.getTagBuilder(tag).add(DWM.getIdentifier(blockBuilder.getName()));
            }
        }
    }
}

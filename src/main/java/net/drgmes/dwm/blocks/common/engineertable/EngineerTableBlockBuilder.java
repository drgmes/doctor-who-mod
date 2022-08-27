package net.drgmes.dwm.blocks.common.engineertable;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;

public class EngineerTableBlockBuilder extends BlockBuilder {
    public EngineerTableBlockBuilder(String name) {
        super(name, new EngineerTableBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.of(Material.WOOD).sounds(BlockSoundGroup.WOOD).strength(1.0f).nonOpaque();
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(this.getBlock());
        blockStateModelGenerator.registerParentedItemModel(this.getBlockItem(), DWM.getIdentifier("block/" + this.getName()));
    }

    @Override
    public void registerTags() {
        this.tags.add(BlockTags.AXE_MINEABLE);
    }
}

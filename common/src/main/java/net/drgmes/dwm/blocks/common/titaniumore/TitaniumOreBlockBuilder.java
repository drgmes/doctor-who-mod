package net.drgmes.dwm.blocks.common.titaniumore;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockLootDataBuilder;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.ExperienceDroppingBlock;
import net.minecraft.block.MapColor;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.intprovider.ConstantIntProvider;

public class TitaniumOreBlockBuilder extends BlockBuilder {
    public TitaniumOreBlockBuilder(String name) {
        super(name, () -> new ExperienceDroppingBlock(ConstantIntProvider.create(0), getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.create()
            .mapColor(MapColor.STONE_GRAY)
            .sounds(BlockSoundGroup.STONE)
            .strength(3.5F, 3.0F)
            .requiresTool();
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/common/" + this.getName());
    }

    @Override
    public BlockLootDataBuilder getBlockLootDataBuilder() {
        return new BlockLootDataBuilder(this, BlockLootDataBuilder.LootType.ORE, ModItems.TITANIUM_RAW.getItem());
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.NEEDS_IRON_TOOL);
    }
}

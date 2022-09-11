package net.drgmes.dwm.blocks.common.titaniumore;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModLootTableProvider;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.block.OreBlock;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

public class TitaniumOreBlockBuilder extends BlockBuilder {
    public TitaniumOreBlockBuilder(String name) {
        super(name, new OreBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings
            .of(Material.STONE)
            .sounds(BlockSoundGroup.STONE)
            .strength(3.0F, 3.0F)
            .requiresTool();
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/common/" + this.getName());
    }

    @Override
    public void registerDrop(ModLootTableProvider modLootTableProvider) {
        modLootTableProvider.addDrop(this.getBlock(), BlockLootTableGenerator.oreDrops(this.getBlock(), ModItems.TITANIUM_RAW.getItem()));
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.NEEDS_IRON_TOOL);
    }
}

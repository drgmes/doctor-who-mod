package net.drgmes.dwm.blocks.common.titaniumore;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModLootTableProvider;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.OreBlock;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;

public class TitaniumOreDeepslateBlockBuilder extends BlockBuilder {
    public TitaniumOreDeepslateBlockBuilder(String name) {
        super(name, new OreBlock(getBlockSettings()));
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return TitaniumOreBlockBuilder.getBlockSettings()
            .sounds(BlockSoundGroup.DEEPSLATE)
            .strength(4.5F, 3.0F);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("block/common/" + this.getName());
    }

    @Override
    public void registerDrop(ModLootTableProvider modLootTableProvider) {
        modLootTableProvider.addDrop(this.getBlock(), BlockLootTableGenerator.oreDrops(this.getBlock(), ModItems.TITANIUM_RAW.getItem()));
    }

//    @Override
//    public void registerWorldGen() {
//        ModWorldGen.appendOreTarget(this, OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.TITANIUM_ORE.name);
//        ModWorldGen.appendOreTarget(this, OreFeatures.DEEPSLATE_ORE_REPLACEABLES, ModBlocks.TITANIUM_ORE.name + "_large");
//    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.NEEDS_IRON_TOOL);
    }
}

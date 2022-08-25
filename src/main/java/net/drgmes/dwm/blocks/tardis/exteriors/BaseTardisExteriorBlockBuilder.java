package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.datagen.common.ModLootTableProvider;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.helpers.ModelHelper;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.BlockLootTableGenerator;
import net.minecraft.tag.BlockTags;

public abstract class BaseTardisExteriorBlockBuilder extends BlockBuilder {
    public BaseTardisExteriorBlockBuilder(String name, BaseTardisExteriorBlock<?> block) {
        super(name, block);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.copy(Blocks.BEDROCK).luminance((blockState) -> (
            blockState.get(BaseTardisExteriorBlock.HALF) == DoubleBlockHalf.UPPER && blockState.get(BaseTardisExteriorBlock.LIT) ? 16 : 0
        ));
    }

    @Override
    public void registerBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator) {
        ModelHelper.createBlockStateWithModel(blockStateModelGenerator, this.getBlock(), ModelHelper.BLOCK_INVISIBLE);
    }

    @Override
    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        ModelHelper.createItemModel(itemModelGenerator, this.getBlockItem(), "item/block/tardis/exteriors/" + this.getName());
    }

    @Override
    public void registerDrop(ModLootTableProvider modLootTableProvider) {
        modLootTableProvider.addDrop(this.getBlock(), BlockLootTableGenerator.dropsNothing());
    }

    @Override
    public void registerTags() {
        this.tags.add(BlockTags.DRAGON_IMMUNE);
    }
}

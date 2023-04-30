package net.drgmes.dwm.blocks.tardis.exteriors;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockLootDataBuilder;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.data.client.TextureKey;
import net.minecraft.registry.tag.BlockTags;

import java.util.function.Supplier;

public abstract class BaseTardisExteriorBlockBuilder extends BlockBuilder {
    public BaseTardisExteriorBlockBuilder(String name, Supplier<BaseTardisExteriorBlock<?>> blockSupplier) {
        super(name, blockSupplier::get);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return AbstractBlock.Settings.copy(Blocks.BEDROCK).luminance((blockState) -> (
            blockState.get(BaseTardisExteriorBlock.HALF) == DoubleBlockHalf.UPPER && blockState.get(BaseTardisExteriorBlock.LIT) ? 16 : 0
        ));
    }

    @Override
    public BlockLootDataBuilder getBlockLootDataBuilder() {
        return null;
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE)
            .addBlockTexture(TextureKey.ALL, DWM.MODELS.BLOCK_INVISIBLE);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getBlockItem(), this.getId(), DWM.getIdentifier("item/block/tardis/exteriors/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }

    @Override
    public void registerTags() {
        super.registerTags();
        this.tags.add(BlockTags.DRAGON_IMMUNE);
    }
}

package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.block.AbstractBlock;
import net.minecraft.data.client.TextureKey;

import java.util.function.Supplier;

public abstract class BaseTardisConsoleUnitBlockBuilder extends BlockBuilder {
    public BaseTardisConsoleUnitBlockBuilder(String name, Supplier<BaseTardisConsoleUnitBlock<?>> blockSupplier) {
        super(name, blockSupplier::get);
    }

    public static AbstractBlock.Settings getBlockSettings() {
        return BlockBuilder.getBlockSettings().nonOpaque();
    }

    @Override
    public BlockModelDataBuilder getBlockModelDataBuilder() {
        return new BlockModelDataBuilder(this, BlockModelDataBuilder.BlockType.SIMPLE)
            .addBlockTexture(TextureKey.ALL, DWM.MODELS.BLOCK_INVISIBLE);
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getBlockItem(), this.getId(), DWM.getIdentifier("item/block/tardis/console_units/" + this.getName()), ItemModelDataBuilder.ItemType.PARENTED);
    }
}

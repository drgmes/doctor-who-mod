package net.drgmes.dwm.common.tardis.doors;

import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.builders.BlockEntityBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class TardisDoorsEntry {
    public final String name;
    public final float entranceWidth;
    public final float entranceHeight;
    public final Supplier<BlockBuilder> blockBuilderSupplier;
    public final Supplier<BlockEntityBuilder<?>> blockEntityBuilderSupplier;

    public TardisDoorsEntry(String name, Supplier<BlockBuilder> blockBuilderSupplier, Supplier<BlockEntityBuilder<?>> blockEntityBuilderSupplier, float entranceWidth, float entranceHeight) {
        this.name = name;
        this.blockBuilderSupplier = blockBuilderSupplier;
        this.blockEntityBuilderSupplier = blockEntityBuilderSupplier;
        this.entranceWidth = entranceWidth;
        this.entranceHeight = entranceHeight;

        TardisDoors.TYPES.put(name, this);
    }

    public Block getBlock() {
        return this.blockBuilderSupplier.get().getBlock();
    }

    public BlockEntityType<?> getBlockEntityType() {
        return this.blockEntityBuilderSupplier.get().getBlockEntityType();
    }
}

package net.drgmes.dwm.common.tardis.exteriors;

import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.builders.BlockEntityBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class TardisExteriorTypeEntry {
    public final String name;
    public final float entranceWidth;
    public final float entranceHeight;
    public final Supplier<BlockBuilder> blockBuilderSupplier;
    public final Supplier<BlockEntityBuilder<?>> blockEntityBuilderSupplier;

    public TardisExteriorTypeEntry(String name, float entranceWidth, float entranceHeight, Supplier<BlockBuilder> blockBuilderSupplier, Supplier<BlockEntityBuilder<?>> blockEntityBuilderSupplier) {
        this.name = name;
        this.entranceWidth = entranceWidth;
        this.entranceHeight = entranceHeight;
        this.blockBuilderSupplier = blockBuilderSupplier;
        this.blockEntityBuilderSupplier = blockEntityBuilderSupplier;
        TardisExteriorTypes.EXTERIOR_TYPES.put(name, this);
    }

    public Block getBlock() {
        return this.blockBuilderSupplier.get().getBlock();
    }

    public BlockEntityType<?> getBlockEntityType() {
        return this.blockEntityBuilderSupplier.get().getBlockEntityType();
    }
}

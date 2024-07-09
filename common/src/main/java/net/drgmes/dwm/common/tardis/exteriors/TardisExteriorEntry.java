package net.drgmes.dwm.common.tardis.exteriors;

import net.drgmes.dwm.common.tardis.doors.TardisDoorsEntry;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.builders.BlockEntityBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;

import java.util.function.Supplier;

public class TardisExteriorEntry {
    public final String name;
    public final float entranceWidth;
    public final float entranceHeight;
    public final TardisDoorsEntry doorsType;
    public final Supplier<BlockBuilder> blockBuilderSupplier;
    public final Supplier<BlockEntityBuilder<?>> blockEntityBuilderSupplier;

    public TardisExteriorEntry(String name, TardisDoorsEntry doorsType, Supplier<BlockBuilder> blockBuilderSupplier, Supplier<BlockEntityBuilder<?>> blockEntityBuilderSupplier, float entranceWidth, float entranceHeight) {
        this.name = name;
        this.doorsType = doorsType;
        this.blockBuilderSupplier = blockBuilderSupplier;
        this.blockEntityBuilderSupplier = blockEntityBuilderSupplier;
        this.entranceWidth = entranceWidth;
        this.entranceHeight = entranceHeight;

        TardisExteriors.TYPES.put(name, this);
    }

    public Block getBlock() {
        return this.blockBuilderSupplier.get().getBlock();
    }

    public Block getDoorsBlock() {
        return this.doorsType.getBlock();
    }

    public BlockEntityType<?> getBlockEntityType() {
        return this.blockEntityBuilderSupplier.get().getBlockEntityType();
    }
}

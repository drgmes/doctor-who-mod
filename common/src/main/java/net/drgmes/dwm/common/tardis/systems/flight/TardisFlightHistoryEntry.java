package net.drgmes.dwm.common.tardis.systems.flight;

import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public record TardisFlightHistoryEntry(
    RegistryKey<World> dimension,
    BlockPos blockPos,
    Direction facing,
    long timestamp
) {
    public TardisFlightHistoryEntry(RegistryKey<World> dimension, BlockPos blockPos, Direction facing) {
        this(dimension, blockPos, facing, System.currentTimeMillis());
    }

    public static TardisFlightHistoryEntry createFromNbt(NbtCompound tag) {
        return new TardisFlightHistoryEntry(
            tag.contains("dimension") ? DimensionHelper.getWorldKey(tag.getString("dimension")) : null,
            tag.contains("blockPos") ? BlockPos.fromLong(tag.getLong("blockPos")) : null,
            tag.contains("facing") ? Direction.byName(tag.getString("facing")) : null,
            tag.contains("timestamp") ? tag.getLong("timestamp") : 0
        );
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        if (this.dimension != null) tag.putString("dimension", this.dimension.getValue().toString());
        if (this.blockPos != null) tag.putLong("blockPos", this.blockPos.asLong());
        if (this.facing != null) tag.putString("facing", this.facing.getName());
        tag.putLong("timestamp", this.timestamp);

        return tag;
    }
}

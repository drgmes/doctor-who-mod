package net.drgmes.dwm.common.tardis.systems.flight;

import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public record TardisFlightHistoryEntry(
    RegistryKey<World> dimension,
    BlockPos blockPos,
    Direction facing,
    long timestamp
) {
    public static final PacketCodec<PacketByteBuf, TardisFlightHistoryEntry> PACKET_CODEC = PacketCodec.tuple(
        RegistryKey.createPacketCodec(RegistryKeys.WORLD), TardisFlightHistoryEntry::dimension,
        BlockPos.PACKET_CODEC, TardisFlightHistoryEntry::blockPos,
        Direction.PACKET_CODEC, TardisFlightHistoryEntry::facing,
        PacketCodecs.VAR_LONG, TardisFlightHistoryEntry::timestamp,
        TardisFlightHistoryEntry::new
    );

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
        tag.putString("dimension", this.dimension.getValue().toString());
        tag.putLong("blockPos", this.blockPos.asLong());
        tag.putString("facing", this.facing.getName());
        tag.putLong("timestamp", this.timestamp);

        return tag;
    }

    public boolean equals(TardisFlightHistoryEntry entry) {
        if (!this.dimension.toString().equals(entry.dimension.toString())) return false;
        if (!this.blockPos.equals(entry.blockPos)) return false;
        if (!this.facing.equals(entry.facing)) return false;
        if (this.timestamp != entry.timestamp) return false;
        return true;
    }
}

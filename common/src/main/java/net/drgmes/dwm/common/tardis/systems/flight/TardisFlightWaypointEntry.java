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
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public record TardisFlightWaypointEntry(
    String id,
    RegistryKey<World> dimension,
    BlockPos blockPos,
    Direction facing,
    String name,
    long timestamp
) {
    public static final PacketCodec<PacketByteBuf, TardisFlightWaypointEntry> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, TardisFlightWaypointEntry::id,
        RegistryKey.createPacketCodec(RegistryKeys.WORLD), TardisFlightWaypointEntry::dimension,
        BlockPos.PACKET_CODEC, TardisFlightWaypointEntry::blockPos,
        Direction.PACKET_CODEC, TardisFlightWaypointEntry::facing,
        PacketCodecs.STRING, TardisFlightWaypointEntry::name,
        PacketCodecs.VAR_LONG, TardisFlightWaypointEntry::timestamp,
        TardisFlightWaypointEntry::new
    );

    public TardisFlightWaypointEntry(RegistryKey<World> dimension, BlockPos blockPos, Direction facing, String name) {
        this(MathHelper.randomUuid(Random.create()).toString(), dimension, blockPos, facing, name, System.currentTimeMillis());
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof TardisFlightWaypointEntry other)) return false;
        return this.id.equals(other.id);
    }

    public static TardisFlightWaypointEntry createFromNbt(NbtCompound tag) {
        return new TardisFlightWaypointEntry(
            tag.contains("id") ? tag.getString("id") : MathHelper.randomUuid(Random.create()).toString(),
            tag.contains("dimension") ? DimensionHelper.getWorldKey(tag.getString("dimension")) : null,
            tag.contains("blockPos") ? BlockPos.fromLong(tag.getLong("blockPos")) : null,
            tag.contains("facing") ? Direction.byName(tag.getString("facing")) : null,
            tag.contains("name") ? tag.getString("name") : "",
            tag.contains("timestamp") ? tag.getLong("timestamp") : 0
        );
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putString("id", this.id);
        tag.putString("dimension", this.dimension.getValue().toString());
        tag.putLong("blockPos", this.blockPos.asLong());
        tag.putString("facing", this.facing.getName());
        tag.putString("name", this.name);
        tag.putLong("timestamp", this.timestamp);

        return tag;
    }
}

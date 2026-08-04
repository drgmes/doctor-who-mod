package net.drgmes.dwm.network.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public record TardisConsoleUnitMonitorOpenPacket(
    BlockPos blockPos,
    String tardisId,
    String owner,
    NbtCompound tardisTag,
    NbtCompound roomsTag
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_monitor_open");
    public static final CustomPayload.Id<TardisConsoleUnitMonitorOpenPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitMonitorOpenPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisConsoleUnitMonitorOpenPacket::blockPos,
        PacketCodecs.STRING, TardisConsoleUnitMonitorOpenPacket::tardisId,
        PacketCodecs.STRING, TardisConsoleUnitMonitorOpenPacket::owner,
        PacketCodecs.NBT_COMPOUND, TardisConsoleUnitMonitorOpenPacket::tardisTag,
        PacketCodecs.NBT_COMPOUND, TardisConsoleUnitMonitorOpenPacket::roomsTag,
        TardisConsoleUnitMonitorOpenPacket::new
    );

    public TardisConsoleUnitMonitorOpenPacket(BlockPos blockPos, String tardisId, String owner, NbtCompound tardisTag) {
        this(blockPos, tardisId, owner, tardisTag, createRoomsTag(tardisTag.getString("consoleRoom")));
    }

    public TardisConsoleUnitMonitorOpenPacket(ServerPlayerEntity player, BlockPos blockPos, String tardisId, NbtCompound tardisTag) {
        this(blockPos, tardisId, getOwnerName(player, tardisTag), tardisTag);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    private static String getOwnerName(ServerPlayerEntity player, NbtCompound tardisTag) {
        String ownerName = "NONE";

        if (tardisTag.contains("owner") && player.getServer() != null) {
            UUID uuid = tardisTag.getUuid("owner");
            ServerPlayerEntity owner = player.getServer().getPlayerManager().getPlayer(uuid);
            ownerName = owner != null ? owner.getName().getString() : uuid.toString();
        }

        return ownerName;
    }

    private static NbtCompound createRoomsTag(String currentConsoleRoomId) {
        List<TardisConsoleRoomEntry> consoleRooms = TardisConsoleRooms.CONSOLE_ROOMS.values().stream().toList();
        AtomicInteger i = new AtomicInteger();
        NbtCompound tag = new NbtCompound();

        consoleRooms.forEach((consoleRoom) -> {
            int index = consoleRoom.name.equals(currentConsoleRoomId) ? 0 : i.incrementAndGet();
            tag.put(CommonHelper.formatIndexString(index), consoleRoom.toNbt());
        });

        return tag;
    }
}

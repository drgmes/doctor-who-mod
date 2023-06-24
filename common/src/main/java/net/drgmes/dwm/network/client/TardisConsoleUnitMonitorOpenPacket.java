package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitMonitorConsoleMainScreen;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class TardisConsoleUnitMonitorOpenPacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final String tardisId;
    private final String owner;

    private final NbtCompound tardisTag;
    private final NbtCompound roomsTag;

    public TardisConsoleUnitMonitorOpenPacket(BlockPos blockPos, String tardisId, String owner, NbtCompound tardisTag, NbtCompound roomsTag) {
        this.blockPos = blockPos;
        this.tardisId = tardisId;
        this.owner = owner;

        this.tardisTag = tardisTag;
        this.roomsTag = roomsTag;
    }

    public TardisConsoleUnitMonitorOpenPacket(BlockPos blockPos, String tardisId, String owner, NbtCompound tardisTag) {
        this(blockPos, tardisId, owner, tardisTag, createRoomsTag(tardisTag.getString("consoleRoom")));
    }

    public TardisConsoleUnitMonitorOpenPacket(ServerPlayerEntity player, BlockPos blockPos, String tardisId, NbtCompound tardisTag) {
        this(blockPos, tardisId, getOwnerName(player, tardisTag), tardisTag);
    }

    public static TardisConsoleUnitMonitorOpenPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitMonitorOpenPacket(buf.readBlockPos(), buf.readString(), buf.readString(), buf.readNbt(), buf.readNbt());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_MONITOR_OPEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeString(this.tardisId);
        buf.writeString(this.owner);

        buf.writeNbt(this.tardisTag);
        buf.writeNbt(this.roomsTag);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            NbtCompound tag = new NbtCompound();
            tag.put("tardisTag", this.tardisTag);
            tag.put("roomsTag", this.roomsTag);
            mc.setScreen(new TardisConsoleUnitMonitorConsoleMainScreen(tardisConsoleUnitBlockEntity, this.tardisId, this.owner, tag));
        }
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
        List<TardisConsoleRoomEntry> list = TardisConsoleRooms.CONSOLE_ROOMS.values().stream().filter((consoleRoom) -> !consoleRoom.isHidden).toList();
        AtomicInteger i = new AtomicInteger();
        NbtCompound tag = new NbtCompound();

        list.forEach((entry) -> {
            int index = entry.name.equals(currentConsoleRoomId) ? 0 : i.incrementAndGet();
            tag.put(String.format("%1$" + 5 + "s", index).replace(' ', '0'), entry.writeNbt(new NbtCompound()));
        });

        return tag;
    }
}

package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitMonitorConsoleRoomsScreen;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class TardisConsoleUnitMonitorOpenPacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final String tardisId;
    private final String consoleRoomId;
    private final NbtCompound tag;

    public TardisConsoleUnitMonitorOpenPacket(BlockPos blockPos, String tardisId, String consoleRoomId, NbtCompound tag) {
        this.blockPos = blockPos;
        this.tardisId = tardisId;
        this.consoleRoomId = consoleRoomId;
        this.tag = tag;
    }

    public static TardisConsoleUnitMonitorOpenPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitMonitorOpenPacket(buf.readBlockPos(), buf.readString(), buf.readString(), buf.readNbt());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_MONITOR_OPEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeString(this.tardisId);
        buf.writeString(this.consoleRoomId);
        buf.writeNbt(this.tag);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            NbtCompound consoleRoomsTag = tag.getCompound("consoleRooms");
            List<TardisConsoleRoomEntry> consoleRooms = new ArrayList<>();
            List<String> keys = new ArrayList<>(consoleRoomsTag.getKeys().stream().toList());

            keys.sort(Comparator.comparing((key) -> key));
            keys.forEach((key) -> consoleRooms.add(TardisConsoleRoomEntry.create(consoleRoomsTag.getCompound(key), false)));

            mc.setScreen(new TardisConsoleUnitMonitorConsoleRoomsScreen(tardisConsoleUnitBlockEntity, tardisId, consoleRoomId, tag.getInt("selectedConsoleRoomIndex"), consoleRooms));
        }
    }
}

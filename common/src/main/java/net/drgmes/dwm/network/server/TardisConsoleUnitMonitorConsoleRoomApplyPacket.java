package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.setup.ModNetwork;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class TardisConsoleUnitMonitorConsoleRoomApplyPacket extends BaseC2SMessage {
    private final String tardisId;
    private final String consoleRoomId;

    public TardisConsoleUnitMonitorConsoleRoomApplyPacket(String tardisId, String consoleRoomId) {
        this.tardisId = tardisId;
        this.consoleRoomId = consoleRoomId;
    }

    public static TardisConsoleUnitMonitorConsoleRoomApplyPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitMonitorConsoleRoomApplyPacket(buf.readString(), buf.readString());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_MONITOR_CONSOLE_ROOM_APPLY;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.tardisId);
        buf.writeString(this.consoleRoomId);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();
        Text failMessage = Text.translatable("message." + DWM.MODID + ".tardis.monitor.console_rooms.failed");

        if (!TardisConsoleRooms.CONSOLE_ROOMS.containsKey(consoleRoomId)) {
            player.sendMessage(failMessage, true);
            return;
        }

        ServerWorld tardisWorld = DimensionHelper.getModWorld(tardisId, player.getServer());
        if (tardisWorld == null) {
            player.sendMessage(failMessage, true);
            return;
        }

        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            TardisConsoleRoomEntry consoleRoom = TardisConsoleRooms.getConsoleRoom(consoleRoomId, tardis.isBroken());
            boolean isConsoleRoomGenerated = consoleRoom.place(tardis);

            if (isConsoleRoomGenerated) {
                tardis.setConsoleRoom(consoleRoom);
                tardis.updateConsoleTiles();
                tardis.updateEntrancePortals();
                tardis.updateRoomEntrancePortals();
            }

            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.monitor.console_rooms." + (isConsoleRoomGenerated ? "success" : "failed")), true);
        });
    }
}

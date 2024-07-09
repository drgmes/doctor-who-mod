package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemConsoleRoom;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.setup.ModNetwork;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;

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

        ServerWorld tardisWorld = DimensionHelper.getModWorld(this.tardisId, player.getServer());
        if (tardisWorld == null) {
            player.sendMessage(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_FAILED, true);
            return;
        }

        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            if (!tardis.checkAccess(player, false, true)) {
                ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
                player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
                return;
            }

            TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
            if (flightSystem.inProgress()) {
                player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_LANDED, true);
                return;
            }

            TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
            if (materializationSystem.inProgress() || !materializationSystem.isMaterialized()) {
                player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_MATERIALIZED, true);
                return;
            }

            TardisSystemConsoleRoom consoleRoomSystem = tardis.getSystem(TardisSystemConsoleRoom.class);
            if (consoleRoomSystem.inProgress()) {
                player.sendMessage(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_IN_PROGRESS, true);
                return;
            }

            if (!consoleRoomSystem.init(this.consoleRoomId, player)) {
                player.sendMessage(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_FAILED, true);
            }
        });
    }
}

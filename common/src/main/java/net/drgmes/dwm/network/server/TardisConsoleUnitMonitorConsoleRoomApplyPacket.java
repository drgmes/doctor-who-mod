package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemConsoleRoom;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public record TardisConsoleUnitMonitorConsoleRoomApplyPacket(
    String tardisId,
    String consoleRoomId
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_monitor_console_room_apply");
    public static final CustomPayload.Id<TardisConsoleUnitMonitorConsoleRoomApplyPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitMonitorConsoleRoomApplyPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, TardisConsoleUnitMonitorConsoleRoomApplyPacket::tardisId,
        PacketCodecs.STRING, TardisConsoleUnitMonitorConsoleRoomApplyPacket::consoleRoomId,
        TardisConsoleUnitMonitorConsoleRoomApplyPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(TardisConsoleUnitMonitorConsoleRoomApplyPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();

            ServerWorld tardisWorld = DimensionHelper.getModWorld(payload.tardisId, player.getServer());
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

                if (!consoleRoomSystem.init(payload.consoleRoomId, player.getUuid())) {
                    player.sendMessage(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_FAILED, true);
                }
            });
        });
    }
}

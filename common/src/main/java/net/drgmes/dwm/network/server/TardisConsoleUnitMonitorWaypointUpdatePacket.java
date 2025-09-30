package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.flight.TardisFlightWaypointEntry;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;

public record TardisConsoleUnitMonitorWaypointUpdatePacket(
    String tardisId,
    TardisFlightWaypointEntry oldWaypointEntry,
    TardisFlightWaypointEntry newWaypointEntry
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_monitor_waypoint_update");
    public static final Id<TardisConsoleUnitMonitorWaypointUpdatePacket> PACKET_ID = new Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitMonitorWaypointUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, TardisConsoleUnitMonitorWaypointUpdatePacket::tardisId,
        TardisFlightWaypointEntry.PACKET_CODEC, TardisConsoleUnitMonitorWaypointUpdatePacket::oldWaypointEntry,
        TardisFlightWaypointEntry.PACKET_CODEC, TardisConsoleUnitMonitorWaypointUpdatePacket::newWaypointEntry,
        TardisConsoleUnitMonitorWaypointUpdatePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(TardisConsoleUnitMonitorWaypointUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();
            ServerWorld tardisWorld = DimensionHelper.getModWorld(payload.tardisId, player.getServer());

            TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
                boolean flag = tardis.getSystem(TardisSystemFlight.class).updateWaypointEntry(payload.oldWaypointEntry, payload.newWaypointEntry);
                if (flag) player.sendMessage(DWM.TEXTS.MONITOR_WAYPOINT_UPDATED, true);
            });
        });
    }
}

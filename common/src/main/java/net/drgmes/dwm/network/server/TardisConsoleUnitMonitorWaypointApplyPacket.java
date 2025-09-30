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

public record TardisConsoleUnitMonitorWaypointApplyPacket(
    String tardisId,
    TardisFlightWaypointEntry waypointEntry
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_monitor_waypoint_apply");
    public static final Id<TardisConsoleUnitMonitorWaypointApplyPacket> PACKET_ID = new Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitMonitorWaypointApplyPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, TardisConsoleUnitMonitorWaypointApplyPacket::tardisId,
        TardisFlightWaypointEntry.PACKET_CODEC, TardisConsoleUnitMonitorWaypointApplyPacket::waypointEntry,
        TardisConsoleUnitMonitorWaypointApplyPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(TardisConsoleUnitMonitorWaypointApplyPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();
            ServerWorld tardisWorld = DimensionHelper.getModWorld(payload.tardisId, player.getServer());

            TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
                TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);

                if (!flightSystem.isEnabled()) {
                    player.sendMessage(DWM.TEXTS.FLIGHT_SYSTEM_NOT_INSTALLED, true);
                    return;
                }

                if (flightSystem.inProgress()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_LANDED, true);
                    return;
                }

                player.sendMessage(DWM.TEXTS.MONITOR_WAYPOINT_LOADED, true);
                tardis.setDestinationDimension(payload.waypointEntry.dimension());
                tardis.setDestinationPosition(payload.waypointEntry.blockPos());
                tardis.setDestinationFacing(payload.waypointEntry.facing());
                tardis.markConsoleTilesUpdated();
            });
        });
    }
}

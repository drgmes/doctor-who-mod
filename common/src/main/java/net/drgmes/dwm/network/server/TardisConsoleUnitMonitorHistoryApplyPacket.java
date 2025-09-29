package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public record TardisConsoleUnitMonitorHistoryApplyPacket(
    String tardisId,
    RegistryKey<World> dimension,
    BlockPos blockPos,
    Direction facing
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_monitor_history_apply");
    public static final Id<TardisConsoleUnitMonitorHistoryApplyPacket> PACKET_ID = new Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitMonitorHistoryApplyPacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, TardisConsoleUnitMonitorHistoryApplyPacket::tardisId,
        RegistryKey.createPacketCodec(RegistryKeys.WORLD), TardisConsoleUnitMonitorHistoryApplyPacket::dimension,
        BlockPos.PACKET_CODEC, TardisConsoleUnitMonitorHistoryApplyPacket::blockPos,
        Direction.PACKET_CODEC, TardisConsoleUnitMonitorHistoryApplyPacket::facing,
        TardisConsoleUnitMonitorHistoryApplyPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(TardisConsoleUnitMonitorHistoryApplyPacket payload, NetworkManager.PacketContext context) {
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

                player.sendMessage(DWM.TEXTS.MONITOR_HISTORY_COORDS_LOADED, true);
                tardis.setDestinationDimension(payload.dimension);
                tardis.setDestinationPosition(payload.blockPos);
                tardis.setDestinationFacing(payload.facing);
                tardis.markConsoleTilesUpdated();
            });
        });
    }
}

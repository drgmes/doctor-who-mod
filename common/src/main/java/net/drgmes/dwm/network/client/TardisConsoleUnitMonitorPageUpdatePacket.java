package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.IPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisConsoleUnitMonitorPageUpdatePacket(
    BlockPos blockPos,
    int monitorPage
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_monitor_page_update");
    public static final CustomPayload.Id<TardisConsoleUnitMonitorPageUpdatePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitMonitorPageUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisConsoleUnitMonitorPageUpdatePacket::blockPos,
        PacketCodecs.INTEGER, TardisConsoleUnitMonitorPageUpdatePacket::monitorPage,
        TardisConsoleUnitMonitorPageUpdatePacket::new
    );

    @Environment(EnvType.CLIENT)
    public static void handle(TardisConsoleUnitMonitorPageUpdatePacket payload, NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(payload.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            tardisConsoleUnitBlockEntity.monitorPage = payload.monitorPage;
        }
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

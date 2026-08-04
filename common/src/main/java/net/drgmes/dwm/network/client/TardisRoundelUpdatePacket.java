package net.drgmes.dwm.network.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisRoundelUpdatePacket(
    BlockPos blockPos,
    boolean uncovered,
    boolean lightMode
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_roundel_update");
    public static final CustomPayload.Id<TardisRoundelUpdatePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisRoundelUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisRoundelUpdatePacket::blockPos,
        PacketCodecs.BOOL, TardisRoundelUpdatePacket::uncovered,
        PacketCodecs.BOOL, TardisRoundelUpdatePacket::lightMode,
        TardisRoundelUpdatePacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}

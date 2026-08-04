package net.drgmes.dwm.network.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ArsDestroyerOpenPacket(
    BlockPos blockPos,
    ArsStructure arsStructure
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("ars_destroyer_open");
    public static final CustomPayload.Id<ArsDestroyerOpenPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, ArsDestroyerOpenPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, ArsDestroyerOpenPacket::blockPos,
        ArsStructure.PACKET_CODEC, ArsDestroyerOpenPacket::arsStructure,
        ArsDestroyerOpenPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}

package net.drgmes.dwm.network.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisToyotaSpinnerUpdatePacket(
    BlockPos blockPos,
    boolean inProgress
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_toyota_spinner_update");
    public static final CustomPayload.Id<TardisToyotaSpinnerUpdatePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisToyotaSpinnerUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisToyotaSpinnerUpdatePacket::blockPos,
        PacketCodecs.BOOL, TardisToyotaSpinnerUpdatePacket::inProgress,
        TardisToyotaSpinnerUpdatePacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}

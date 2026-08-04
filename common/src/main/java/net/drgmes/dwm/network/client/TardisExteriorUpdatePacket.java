package net.drgmes.dwm.network.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.enums.TardisExteriorAction;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisExteriorUpdatePacket(
    BlockPos blockPos,
    TardisExteriorAction exteriorAction
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_exterior_update");
    public static final CustomPayload.Id<TardisExteriorUpdatePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisExteriorUpdatePacket> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(PacketByteBuf buf, TardisExteriorUpdatePacket payload) {
            buf.writeBlockPos(payload.blockPos);
            buf.writeString(payload.exteriorAction.name());
        }

        @Override
        public TardisExteriorUpdatePacket decode(PacketByteBuf buf) {
            return new TardisExteriorUpdatePacket(
                buf.readBlockPos(),
                TardisExteriorAction.valueOf(buf.readString())
            );
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}

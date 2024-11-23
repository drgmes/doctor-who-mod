package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.enums.TardisExteriorAction;
import net.drgmes.dwm.network.IPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
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

    @Environment(EnvType.CLIENT)
    public static void handle(TardisExteriorUpdatePacket payload, NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(payload.blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            switch (payload.exteriorAction) {
                case NORMALIZE -> tardisExteriorBlockEntity.normalize();
                case DEMAT -> tardisExteriorBlockEntity.demat();
                case REMAT -> tardisExteriorBlockEntity.remat();
                case PULSE -> tardisExteriorBlockEntity.pulse();
            }
        }
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.network.IPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisRoundelBlockTemplateUpdatePacket(
    BlockPos blockPos,
    String blockTemplateId
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_roundel_block_template_update");
    public static final CustomPayload.Id<TardisRoundelBlockTemplateUpdatePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisRoundelBlockTemplateUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisRoundelBlockTemplateUpdatePacket::blockPos,
        PacketCodecs.STRING, TardisRoundelBlockTemplateUpdatePacket::blockTemplateId,
        TardisRoundelBlockTemplateUpdatePacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    @Environment(EnvType.CLIENT)
    public static void handle(TardisRoundelBlockTemplateUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                tardisRoundelBlockEntity.blockTemplate = Identifier.of(payload.blockTemplateId);
            }
        });
    }
}

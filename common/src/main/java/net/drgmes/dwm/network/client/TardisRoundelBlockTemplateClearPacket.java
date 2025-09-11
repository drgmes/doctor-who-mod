package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.network.IPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisRoundelBlockTemplateClearPacket(
    BlockPos blockPos
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_roundel_block_template_clear");
    public static final CustomPayload.Id<TardisRoundelBlockTemplateClearPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisRoundelBlockTemplateClearPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisRoundelBlockTemplateClearPacket::blockPos,
        TardisRoundelBlockTemplateClearPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    @Environment(EnvType.CLIENT)
    public static void handle(TardisRoundelBlockTemplateClearPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            final MinecraftClient mc = MinecraftClient.getInstance();

            if (mc.world.getBlockEntity(payload.blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
                tardisRoundelBlockEntity.blockTemplate = null;
            }
        });
    }
}

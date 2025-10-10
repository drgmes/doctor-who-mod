package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.screens.TardisArsCreatorScreen;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.network.IPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public record ArsCreatorOpenPacket(
    BlockPos blockPos,
    Map<String, ArsCategory> arsCategories,
    Map<String, ArsStructure> arsStructures
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("ars_creator_open");
    public static final CustomPayload.Id<ArsCreatorOpenPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, ArsCreatorOpenPacket> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(PacketByteBuf buf, ArsCreatorOpenPacket payload) {
            buf.writeBlockPos(payload.blockPos);
            buf.writeMap(payload.arsCategories, PacketByteBuf::writeString, ArsCategory.PACKET_CODEC);
            buf.writeMap(payload.arsStructures, PacketByteBuf::writeString, ArsStructure.PACKET_CODEC);
        }

        @Override
        public ArsCreatorOpenPacket decode(PacketByteBuf buf) {
            return new ArsCreatorOpenPacket(
                buf.readBlockPos(),
                buf.readMap(PacketByteBuf::readString, ArsCategory.PACKET_CODEC),
                buf.readMap(PacketByteBuf::readString, ArsStructure.PACKET_CODEC)
            );
        }
    };

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    @Environment(EnvType.CLIENT)
    public static void handle(ArsCreatorOpenPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            ClientPlayerEntity player = (ClientPlayerEntity) context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos) instanceof TardisArsCreatorBlockEntity) {
                MinecraftClient.getInstance().setScreen(new TardisArsCreatorScreen(payload.blockPos, payload.arsCategories, payload.arsStructures));
            }
        });
    }
}

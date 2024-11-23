package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.screens.TardisArsDestroyerScreen;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.network.IPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
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

    @Environment(EnvType.CLIENT)
    public static void handle(ArsDestroyerOpenPacket payload, NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(payload.blockPos) instanceof TardisArsDestroyerBlockEntity) {
            mc.setScreen(new TardisArsDestroyerScreen(payload.blockPos, payload.arsStructure));
        }
    }

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

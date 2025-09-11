package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record ArsCreatorApplyPacket(
    BlockPos blockPos,
    String arsStructureName
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("ars_creator_apply");
    public static final CustomPayload.Id<ArsCreatorApplyPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, ArsCreatorApplyPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, ArsCreatorApplyPacket::blockPos,
        PacketCodecs.STRING, ArsCreatorApplyPacket::arsStructureName,
        ArsCreatorApplyPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(ArsCreatorApplyPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();

            ServerWorld serverWorld = (ServerWorld) player.getWorld();
            if (!TardisHelper.isTardisDimension(serverWorld)) return;

            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                if (tardis.isBroken()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
                    return;
                }

                ArsStructure arsStructure = ArsStructures.STRUCTURES.get(payload.arsStructureName);
                boolean flag = arsStructure.place(player, tardis, payload.blockPos);

                player.sendMessage(flag ? DWM.TEXTS.ARS_SECONDARY_ROOM_BUILD_SUCCESS : DWM.TEXTS.ARS_SECONDARY_ROOM_BUILD_FAILED, true);
            });
        });
    }
}

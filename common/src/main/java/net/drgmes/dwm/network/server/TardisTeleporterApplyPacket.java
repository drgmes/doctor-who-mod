package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.TardisTeleporterBlockEntity;
import net.drgmes.dwm.enums.TardisTeleporterEntityTypes;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public record TardisTeleporterApplyPacket(
    BlockPos blockPos,
    BlockPos destinationBlockPos,
    boolean isLocked,
    List<TardisTeleporterEntityTypes> allowedEntityTypes
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_teleporter_apply");
    public static final Id<TardisTeleporterApplyPacket> PACKET_ID = new Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisTeleporterApplyPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisTeleporterApplyPacket::blockPos,
        BlockPos.PACKET_CODEC, TardisTeleporterApplyPacket::destinationBlockPos,
        PacketCodecs.BOOL, TardisTeleporterApplyPacket::isLocked,
        TardisTeleporterBlockEntity.ENTITY_TYPES_PACKET_CODEC, TardisTeleporterApplyPacket::allowedEntityTypes,
        TardisTeleporterApplyPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(TardisTeleporterApplyPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos) instanceof TardisTeleporterBlockEntity tardisTeleporterBlockEntity) {
                tardisTeleporterBlockEntity.destinationBlockPos = payload.destinationBlockPos;
                tardisTeleporterBlockEntity.isLocked = payload.isLocked;
                tardisTeleporterBlockEntity.allowedEntityTypes = payload.allowedEntityTypes;
                tardisTeleporterBlockEntity.markDirty();
            }
        });
    }
}

package net.drgmes.dwm.network.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.TardisTeleporterBlockEntity;
import net.drgmes.dwm.enums.TardisTeleporterEntityTypes;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

import java.util.List;

public record TardisTeleporterOpenPacket(
    BlockPos blockPos,
    BlockPos destinationBlockPos,
    boolean isLocked,
    List<TardisTeleporterEntityTypes> allowedEntityTypes
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_teleporter_open");
    public static final Id<TardisTeleporterOpenPacket> PACKET_ID = new Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisTeleporterOpenPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisTeleporterOpenPacket::blockPos,
        BlockPos.PACKET_CODEC, TardisTeleporterOpenPacket::destinationBlockPos,
        PacketCodecs.BOOL, TardisTeleporterOpenPacket::isLocked,
        TardisTeleporterBlockEntity.ENTITY_TYPES_PACKET_CODEC, TardisTeleporterOpenPacket::allowedEntityTypes,
        TardisTeleporterOpenPacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}

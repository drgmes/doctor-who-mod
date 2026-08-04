package net.drgmes.dwm.network.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket(
    BlockPos blockPos,
    NbtCompound tag
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_telepathic_interface_map_banners_open");
    public static final CustomPayload.Id<TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket::blockPos,
        PacketCodecs.NBT_COMPOUND, TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket::tag,
        TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}

package net.drgmes.dwm.network.client;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SonicDeviceUpdatePacket(
    ItemStack itemStack,
    String slotName
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("sonic_device_update");
    public static final Id<SonicDeviceUpdatePacket> PACKET_ID = new Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, SonicDeviceUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
        ItemStack.OPTIONAL_PACKET_CODEC, SonicDeviceUpdatePacket::itemStack,
        PacketCodecs.STRING, SonicDeviceUpdatePacket::slotName,
        SonicDeviceUpdatePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

}

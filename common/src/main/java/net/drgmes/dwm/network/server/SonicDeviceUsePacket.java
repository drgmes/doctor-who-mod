package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.items.sonicdevices.ISonicDeviceItem;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SonicDeviceUsePacket(
    ItemStack itemStack,
    String slot,
    boolean isAlternativeAction
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("sonic_device_use");
    public static final CustomPayload.Id<SonicDeviceUsePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, SonicDeviceUsePacket> PACKET_CODEC = PacketCodec.tuple(
        ItemStack.OPTIONAL_PACKET_CODEC, SonicDeviceUsePacket::itemStack,
        PacketCodecs.STRING, SonicDeviceUsePacket::slot,
        PacketCodecs.BOOL, SonicDeviceUsePacket::isAlternativeAction,
        SonicDeviceUsePacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(SonicDeviceUsePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();

            if (payload.itemStack.getItem() instanceof ISonicDeviceItem sonicDeviceItem) {
                sonicDeviceItem.useSonicDevice(player.getWorld(), player, EquipmentSlot.byName(payload.slot), payload.isAlternativeAction);
            }
        });
    }
}

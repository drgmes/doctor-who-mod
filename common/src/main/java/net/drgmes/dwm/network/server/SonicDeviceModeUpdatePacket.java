package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.enums.SonicDeviceMode;
import net.drgmes.dwm.network.IPacket;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record SonicDeviceModeUpdatePacket(
    String mode,
    String slotName
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("sonic_device_mode_update");
    public static final CustomPayload.Id<SonicDeviceModeUpdatePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<RegistryByteBuf, SonicDeviceModeUpdatePacket> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.STRING, SonicDeviceModeUpdatePacket::mode,
        PacketCodecs.STRING, SonicDeviceModeUpdatePacket::slotName,
        SonicDeviceModeUpdatePacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(SonicDeviceModeUpdatePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();
            EquipmentSlot slot = EquipmentSlot.byName(payload.slotName);
            ItemStack equippedStack = player.getEquippedStack(slot);

            if (SonicDevice.checkItemStackIsSonicDevice(equippedStack)) {
                SonicDevice.setInteractionMode(equippedStack, SonicDeviceMode.valueOf(payload.mode));
                player.getItemCooldownManager().set(equippedStack.getItem(), 4);
                player.equipStack(slot, equippedStack);
            }
        });
    }
}

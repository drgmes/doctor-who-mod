package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.setup.ModNetwork;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class SonicDeviceUpdatePacket extends BaseC2SMessage {
    private final ItemStack itemStack;
    private final String slot;

    public SonicDeviceUpdatePacket(ItemStack itemStack, String slot) {
        this.itemStack = itemStack;
        this.slot = slot;
    }

    public static SonicDeviceUpdatePacket create(PacketByteBuf buf) {
        return new SonicDeviceUpdatePacket(buf.readItemStack(), buf.readString());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.SONIC_DEVICE_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeItemStack(this.itemStack);
        buf.writeString(this.slot);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        if (SonicDevice.checkItemStackIsSonicDevice(player.getEquippedStack(EquipmentSlot.byName(this.slot)))) {
            player.equipStack(EquipmentSlot.byName(this.slot), this.itemStack);
            player.getItemCooldownManager().set(this.itemStack.getItem(), 4);
        }
    }
}

package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.items.sonicdevices.ISonicDeviceItem;
import net.drgmes.dwm.setup.ModNetwork;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;

public class SonicDeviceUsePacket extends BaseC2SMessage {
    private final ItemStack itemStack;
    private final String slot;
    private final boolean isAlternativeAction;

    public SonicDeviceUsePacket(ItemStack itemStack, String slot, boolean isAlternativeAction) {
        this.itemStack = itemStack;
        this.slot = slot;
        this.isAlternativeAction = isAlternativeAction;
    }

    public static SonicDeviceUsePacket create(PacketByteBuf buf) {
        return new SonicDeviceUsePacket(buf.readItemStack(), buf.readString(), buf.readBoolean());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.SONIC_DEVICE_USE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeItemStack(this.itemStack);
        buf.writeString(this.slot);
        buf.writeBoolean(this.isAlternativeAction);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        if (this.itemStack.getItem() instanceof ISonicDeviceItem sonicDeviceItem) {
            sonicDeviceItem.useSonicDevice(player.getWorld(), player, EquipmentSlot.byName(this.slot), this.isAlternativeAction);
        }
    }
}

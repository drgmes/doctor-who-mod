package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.common.sonicscrewdriver.SonicScrewdriver;
import net.drgmes.dwm.setup.ModNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;

public class SonicScrewdriverUpdatePacket extends BaseC2SMessage {
    private final ItemStack itemStack;
    private final boolean isMainHand;

    public SonicScrewdriverUpdatePacket(ItemStack itemStack, boolean isMainHand) {
        this.itemStack = itemStack;
        this.isMainHand = isMainHand;
    }

    public static SonicScrewdriverUpdatePacket create(PacketByteBuf buf) {
        return new SonicScrewdriverUpdatePacket(buf.readItemStack(), buf.readBoolean());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.SONIC_SCREWDRIVER_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeItemStack(this.itemStack);
        buf.writeBoolean(this.isMainHand);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        if (SonicScrewdriver.checkItemStackIsSonicScrewdriver(player.getStackInHand(this.isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND))) {
            player.setStackInHand(this.isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND, this.itemStack);
            player.getItemCooldownManager().set(this.itemStack.getItem(), 4);
        }
    }
}

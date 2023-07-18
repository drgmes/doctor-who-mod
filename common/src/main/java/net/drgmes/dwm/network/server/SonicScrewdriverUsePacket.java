package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.common.sonicscrewdriver.SonicScrewdriver;
import net.drgmes.dwm.items.sonicscrewdriver.SonicScrewdriverItem;
import net.drgmes.dwm.setup.ModNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.Hand;

public class SonicScrewdriverUsePacket extends BaseC2SMessage {
    private final ItemStack itemStack;
    private final boolean isMainHand;
    private final boolean isAlternativeAction;

    public SonicScrewdriverUsePacket(ItemStack itemStack, boolean isMainHand, boolean isAlternativeAction) {
        this.itemStack = itemStack;
        this.isMainHand = isMainHand;
        this.isAlternativeAction = isAlternativeAction;
    }

    public static SonicScrewdriverUsePacket create(PacketByteBuf buf) {
        return new SonicScrewdriverUsePacket(buf.readItemStack(), buf.readBoolean(), buf.readBoolean());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.SONIC_SCREWDRIVER_USE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeItemStack(this.itemStack);
        buf.writeBoolean(this.isMainHand);
        buf.writeBoolean(this.isAlternativeAction);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        if (SonicScrewdriver.checkItemStackIsSonicScrewdriver(this.itemStack)) {
            ((SonicScrewdriverItem) this.itemStack.getItem()).useSonicScrewdriver(player.getWorld(), player, this.isMainHand ? Hand.MAIN_HAND : Hand.OFF_HAND, this.isAlternativeAction);
        }
    }
}

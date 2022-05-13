package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundScrewdriverUpdatePacket {
    public final ItemStack screwdriverItemStack;
    public final boolean isMainHand;

    public ServerboundScrewdriverUpdatePacket(ItemStack itemStack, boolean isMainHand) {
        this.screwdriverItemStack = itemStack;
        this.isMainHand = isMainHand;
    }

    public ServerboundScrewdriverUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readItem(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(this.screwdriverItemStack);
        buffer.writeBoolean(this.isMainHand);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            ItemStack itemStack = ctx.get().getSender().getItemInHand(isMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND);

            if (itemStack.getItem() instanceof ScrewdriverItem) {
                ctx.get().getSender().setItemInHand(isMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, screwdriverItemStack);
                success.set(true);
            }
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

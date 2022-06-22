package net.drgmes.dwm.network;

import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ServerboundScrewdriverUsePacket {
    public final ItemStack screwdriverItemStack;
    public final boolean isMainHand;
    public final boolean isAlternativeAction;

    public ServerboundScrewdriverUsePacket(ItemStack itemStack, boolean isMainHand, boolean isAlternativeAction) {
        this.screwdriverItemStack = itemStack;
        this.isMainHand = isMainHand;
        this.isAlternativeAction = isAlternativeAction;
    }

    public ServerboundScrewdriverUsePacket(ItemStack itemStack, InteractionHand hand, boolean isAlternativeAction) {
        this(itemStack, hand == InteractionHand.MAIN_HAND, isAlternativeAction);
    }

    public ServerboundScrewdriverUsePacket(FriendlyByteBuf buffer) {
        this(buffer.readItem(), buffer.readBoolean(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeItem(this.screwdriverItemStack);
        buffer.writeBoolean(this.isMainHand);
        buffer.writeBoolean(this.isAlternativeAction);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            if (screwdriverItemStack.getItem() instanceof ScrewdriverItem screwdriverItem) {
                screwdriverItem.useScrewdriver(ctx.get().getSender().level, ctx.get().getSender(), isMainHand ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND, isAlternativeAction);
                success.set(true);
            }
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

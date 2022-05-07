package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisConsoleScrewdriverSlotUpdatePacket {
    private final BlockPos blockPos;
    private final ItemStack screwdriverItemStack;

    public ClientboundTardisConsoleScrewdriverSlotUpdatePacket(BlockPos blockPos, ItemStack screwdriverItemStack) {
        this.blockPos = blockPos;
        this.screwdriverItemStack = screwdriverItemStack;
    }

    public ClientboundTardisConsoleScrewdriverSlotUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readItem());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeItem(this.screwdriverItemStack);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();

                if (mc.level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
                    tardisConsoleBlockEntity.screwdriverItemStack = screwdriverItemStack;
                    tardisConsoleBlockEntity.setChanged();
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

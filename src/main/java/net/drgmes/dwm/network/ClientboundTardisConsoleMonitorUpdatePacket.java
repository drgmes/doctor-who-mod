package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisConsoleMonitorUpdatePacket {
    private final BlockPos blockPos;
    private final int monitorPage;

    public ClientboundTardisConsoleMonitorUpdatePacket(BlockPos blockPos, int monitorPage) {
        this.blockPos = blockPos;
        this.monitorPage = monitorPage;
    }

    public ClientboundTardisConsoleMonitorUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeInt(this.monitorPage);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();

                if (mc.level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
                    tardisConsoleBlockEntity.monitorPage = monitorPage;
                    tardisConsoleBlockEntity.setChanged();
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

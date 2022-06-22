package net.drgmes.dwm.network;

import net.drgmes.dwm.common.tardis.boti.IBoti;
import net.drgmes.dwm.common.tardis.boti.storage.BotiStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientboundBotiUpdatePacket {
    private final BlockPos blockPos;
    private final BotiStorage botiStorage;

    public ClientboundBotiUpdatePacket(BlockPos blockPos, BotiStorage botiStorage) {
        this.blockPos = blockPos;
        this.botiStorage = botiStorage;
    }

    public ClientboundBotiUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), BotiStorage.create(buffer));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        this.botiStorage.writeToBuffer(buffer);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();

                if (mc.level.getBlockEntity(blockPos) instanceof IBoti botiTile) {
                    botiTile.setBotiStorage(botiStorage);
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

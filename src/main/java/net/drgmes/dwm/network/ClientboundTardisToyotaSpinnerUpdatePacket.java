package net.drgmes.dwm.network;

import net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientboundTardisToyotaSpinnerUpdatePacket {
    private final BlockPos blockPos;
    private final boolean inProgress;

    public ClientboundTardisToyotaSpinnerUpdatePacket(BlockPos blockPos, boolean inProgress) {
        this.blockPos = blockPos;
        this.inProgress = inProgress;
    }

    public ClientboundTardisToyotaSpinnerUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeBoolean(this.inProgress);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();

                if (mc.level.getBlockEntity(blockPos) instanceof TardisToyotaSpinnerBlockEntity tardisToyotaSpinnerBlockEntity) {
                    tardisToyotaSpinnerBlockEntity.inProgress = inProgress;
                    tardisToyotaSpinnerBlockEntity.setChanged();
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisEngineUpdatePacket {
    private final BlockPos blockPos;
    private final CompoundTag tag;

    public ClientboundTardisEngineUpdatePacket(BlockPos blockPos, CompoundTag tag) {
        this.blockPos = blockPos;
        this.tag = tag;
    }

    public ClientboundTardisEngineUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readNbt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeNbt(this.tag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();

                if (mc.level.getBlockEntity(blockPos) instanceof BaseTardisEngineBlockEntity tardisEngineBlockEntity) {
                    tardisEngineBlockEntity.load(tag);
                    tardisEngineBlockEntity.setChanged();
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

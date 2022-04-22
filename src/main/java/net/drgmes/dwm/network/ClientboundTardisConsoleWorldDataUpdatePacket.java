package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisConsoleWorldDataUpdatePacket {
    private final BlockPos blockPos;
    private final CompoundTag tardisWorldData;

    public ClientboundTardisConsoleWorldDataUpdatePacket(BlockPos blockPos, CompoundTag tardisWorldData) {
        this.blockPos = blockPos;
        this.tardisWorldData = tardisWorldData;
    }

    public ClientboundTardisConsoleWorldDataUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readNbt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeNbt(this.tardisWorldData);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();
                final BlockEntity blockEntity = mc.level.getBlockEntity(blockPos);
                if (blockEntity == null) return;

                blockEntity.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                    provider.deserializeNBT(tardisWorldData);
                    blockEntity.getLevel().sendBlockUpdated(blockPos, blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    blockEntity.setChanged();
                    success.set(true);
                });
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

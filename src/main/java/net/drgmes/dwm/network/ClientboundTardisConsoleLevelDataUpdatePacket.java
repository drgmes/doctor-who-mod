package net.drgmes.dwm.network;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ClientboundTardisConsoleLevelDataUpdatePacket {
    private final BlockPos blockPos;
    private final CompoundTag tardisWorldData;

    public ClientboundTardisConsoleLevelDataUpdatePacket(BlockPos blockPos, CompoundTag tardisWorldData) {
        this.blockPos = blockPos;
        this.tardisWorldData = tardisWorldData;
    }

    public ClientboundTardisConsoleLevelDataUpdatePacket(FriendlyByteBuf buffer) {
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

                if (mc.level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
                    tardisConsoleBlockEntity.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                        provider.deserializeNBT(tardisWorldData);
                        tardisConsoleBlockEntity.setChanged();
                        success.set(true);
                    });
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

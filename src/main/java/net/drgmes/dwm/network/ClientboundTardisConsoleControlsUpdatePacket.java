package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisConsoleControlsUpdatePacket {
    private final BlockPos blockPos;
    private final TardisConsoleControlsStorage controlsStorage;

    public ClientboundTardisConsoleControlsUpdatePacket(BlockPos blockPos, TardisConsoleControlsStorage controlsStorage) {
        this.blockPos = blockPos;
        this.controlsStorage = controlsStorage;
    }

    public ClientboundTardisConsoleControlsUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), new TardisConsoleControlsStorage());
        this.controlsStorage.load(buffer.readNbt());
    }

    public void encode(FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        this.controlsStorage.save(tag);

        buffer.writeBlockPos(this.blockPos);
        buffer.writeNbt(tag);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();

                if (mc.level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity baseTardisConsoleBlockEntity) {
                    BlockState blockState = baseTardisConsoleBlockEntity.getBlockState();
                    baseTardisConsoleBlockEntity.controlsStorage = controlsStorage;
                    baseTardisConsoleBlockEntity.getLevel().sendBlockUpdated(blockPos, blockState, blockState, 3);
                    baseTardisConsoleBlockEntity.setChanged();
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

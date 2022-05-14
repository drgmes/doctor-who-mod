package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

public class ServerboundTardisConsoleLevelDataUpdatePacket {
    private final BlockPos blockPos;

    public ServerboundTardisConsoleLevelDataUpdatePacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public ServerboundTardisConsoleLevelDataUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            if (!(ctx.get().getSender().level instanceof ServerLevel level)) return;

            if (level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
                level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                    if (!tardis.isValid()) return;

                    ModPackets.send(new ClientboundTardisConsoleLevelDataUpdatePacket(blockPos, tardis.serializeNBT()));
                    ModPackets.send(new ClientboundTardisConsoleControlsUpdatePacket(blockPos, tardisConsoleBlockEntity.controlsStorage));
                });
            }

            success.set(true);
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

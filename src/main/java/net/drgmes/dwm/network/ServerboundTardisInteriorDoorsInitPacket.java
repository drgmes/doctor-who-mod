package net.drgmes.dwm.network;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraftforge.network.NetworkEvent;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

public class ServerboundTardisInteriorDoorsInitPacket {
    private final BlockPos blockPos;

    public ServerboundTardisInteriorDoorsInitPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public ServerboundTardisInteriorDoorsInitPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> {
            if (!(ctx.get().getSender().level instanceof ServerLevel level)) return;

            if (level.getBlockEntity(blockPos) instanceof BaseTardisDoorsBlockEntity tardisDoorsBlockEntity) {
                tardisDoorsBlockEntity.init();
            }

            success.set(true);
        });

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

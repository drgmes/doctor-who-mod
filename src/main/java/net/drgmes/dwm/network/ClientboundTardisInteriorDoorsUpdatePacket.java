package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisInteriorDoorsUpdatePacket {
    private final BlockPos blockPos;
    private final boolean isDoorsOpened;

    public ClientboundTardisInteriorDoorsUpdatePacket(BlockPos blockPos, boolean isDoorsOpened) {
        this.blockPos = blockPos;
        this.isDoorsOpened = isDoorsOpened;
    }

    public ClientboundTardisInteriorDoorsUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeBoolean(this.isDoorsOpened);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();
                final BlockState blockState = mc.level.getBlockState(blockPos);

                if (blockState.getBlock() instanceof BaseTardisDoorsBlock && blockState.getValue(BaseTardisDoorsBlock.OPEN) != isDoorsOpened) {
                    mc.level.setBlock(blockPos, blockState.setValue(BaseTardisDoorsBlock.OPEN, isDoorsOpened), 10);
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

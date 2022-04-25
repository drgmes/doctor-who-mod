package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisExteriorUpdatePacket {
    private final BlockPos blockPos;
    private final boolean doorsOpened;
    private final boolean demat;
    private final boolean remat;

    public ClientboundTardisExteriorUpdatePacket(BlockPos blockPos, boolean doorsOpened, boolean demat, boolean remat) {
        this.blockPos = blockPos;
        this.doorsOpened = doorsOpened;
        this.demat = demat;
        this.remat = remat;
    }

    public ClientboundTardisExteriorUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeBoolean(this.doorsOpened);
        buffer.writeBoolean(this.demat);
        buffer.writeBoolean(this.remat);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();
                final BlockState blockState = mc.level.getBlockState(blockPos);

                if (mc.level.getBlockEntity(blockPos) instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
                    if (demat) tardisExteriorBlockEntity.demat();
                    success.set(true);
                }

                if (blockState.getBlock() instanceof TardisExteriorBlock && blockState.getValue(TardisExteriorBlock.OPEN) != doorsOpened) {
                    mc.level.setBlock(blockPos, blockState.setValue(TardisExteriorBlock.OPEN, doorsOpened), 10);
                    mc.level.levelEvent(mc.player, doorsOpened ? 1006 : 1012, blockPos, 0);
                    mc.level.gameEvent(mc.player, doorsOpened ? GameEvent.BLOCK_OPEN : GameEvent.BLOCK_CLOSE, blockPos);
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

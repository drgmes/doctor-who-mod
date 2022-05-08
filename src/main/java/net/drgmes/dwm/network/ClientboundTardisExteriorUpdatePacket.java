package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisExteriorUpdatePacket {
    private final BlockPos blockPos;
    private final boolean isDoorsOpened;
    private final boolean demat;

    public ClientboundTardisExteriorUpdatePacket(BlockPos blockPos, boolean isDoorsOpened, boolean demat) {
        this.blockPos = blockPos;
        this.isDoorsOpened = isDoorsOpened;
        this.demat = demat;
    }

    public ClientboundTardisExteriorUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readBoolean(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeBoolean(this.isDoorsOpened);
        buffer.writeBoolean(this.demat);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();
                final BlockState blockState = mc.level.getBlockState(blockPos);

                if (demat && mc.level.getBlockEntity(blockPos) instanceof TardisExteriorPoliceBoxBlockEntity tardisExteriorBlockEntity) {
                    tardisExteriorBlockEntity.demat();
                    success.set(true);
                }

                if (blockState.getBlock() instanceof TardisExteriorPoliceBoxBlock && blockState.getValue(TardisExteriorPoliceBoxBlock.OPEN) != isDoorsOpened) {
                    mc.level.setBlock(blockPos, blockState.setValue(TardisExteriorPoliceBoxBlock.OPEN, isDoorsOpened), 10);
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

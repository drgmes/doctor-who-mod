package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisExteriorUpdatePacket {
    private final BlockPos blockPos;
    private final int materializedPercent;
    private final boolean doorsOpened;
    private final boolean dematSound;
    private final boolean rematSound;

    public ClientboundTardisExteriorUpdatePacket(BlockPos blockPos, int materializedPercent, boolean doorsOpened, boolean dematSound, boolean rematSound) {
        this.blockPos = blockPos;
        this.materializedPercent = materializedPercent;
        this.dematSound = dematSound;
        this.rematSound = rematSound;
        this.doorsOpened = doorsOpened;
    }

    public ClientboundTardisExteriorUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readInt(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeInt(this.materializedPercent);
        buffer.writeBoolean(this.doorsOpened);
        buffer.writeBoolean(this.dematSound);
        buffer.writeBoolean(this.rematSound);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();
                final BlockState blockState = mc.level.getBlockState(blockPos);

                if (mc.level.getBlockEntity(blockPos) instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
                    if (dematSound) mc.level.playSound(mc.player, blockPos, ModSounds.TARDIS_TAKEOFF.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    if (rematSound) mc.level.playSound(mc.player, blockPos, ModSounds.TARDIS_LAND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
                    tardisExteriorBlockEntity.materializedPercent = materializedPercent;
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

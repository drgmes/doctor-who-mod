package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundEnergyTardisConsoleUpdatePacket {
    public final BlockPos blockPos;

    public final boolean controlDoor;
    public final boolean controlShields;
    public final boolean controlHandbrake;
    public final boolean controlStarter;
    public final boolean controlRandomizer;
    public final int controlFacing;

    public ClientboundEnergyTardisConsoleUpdatePacket(BlockPos blockPos, boolean controlDoor, boolean controlShields, boolean controlHandbrake, boolean controlStarter, boolean controlRandomizer, int controlFacing) {
        this.blockPos = blockPos;
        this.controlDoor = controlDoor;
        this.controlShields = controlShields;
        this.controlHandbrake = controlHandbrake;
        this.controlStarter = controlStarter;
        this.controlRandomizer = controlRandomizer;
        this.controlFacing = controlFacing;
    }

    public ClientboundEnergyTardisConsoleUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readBoolean(), buffer.readInt());
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeBoolean(this.controlDoor);
        buffer.writeBoolean(this.controlShields);
        buffer.writeBoolean(this.controlHandbrake);
        buffer.writeBoolean(this.controlStarter);
        buffer.writeBoolean(this.controlRandomizer);
        buffer.writeInt(this.controlFacing);
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();
                final BlockEntity blockEntity = mc.level.getBlockEntity(blockPos);

                if (blockEntity instanceof BaseTardisConsoleBlockEntity baseTardisConsoleBlockEntity) {
                    baseTardisConsoleBlockEntity.controlDoor = controlDoor;
                    baseTardisConsoleBlockEntity.controlShields = controlShields;
                    baseTardisConsoleBlockEntity.controlHandbrake = controlHandbrake;
                    baseTardisConsoleBlockEntity.controlStarter = controlStarter;
                    baseTardisConsoleBlockEntity.controlRandomizer = controlRandomizer;
                    baseTardisConsoleBlockEntity.controlFacing = controlFacing;

                    blockEntity.getLevel().sendBlockUpdated(blockPos, blockEntity.getBlockState(), blockEntity.getBlockState(), 3);
                    blockEntity.setChanged();
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

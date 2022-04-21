package net.drgmes.dwm.network;

import java.util.Map.Entry;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoleTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisConsoleUpdatePacket {
    private final BlockPos blockPos;
    private final TardisConsoleControlsStorage controlsStorage;

    public ClientboundTardisConsoleUpdatePacket(BlockPos blockPos, TardisConsoleControlsStorage controlsStorage) {
        this.blockPos = blockPos;
        this.controlsStorage = controlsStorage;
    }

    public ClientboundTardisConsoleUpdatePacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), new TardisConsoleControlsStorage());

        for (Entry<TardisConsoleControlRoles, Object> entry : this.controlsStorage.values.entrySet()) {
            if (entry.getKey().type == TardisConsoleControlRoleTypes.BOOLEAN) this.controlsStorage.values.put(entry.getKey(), buffer.readBoolean());
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.BOOLEAN_DIRECT) this.controlsStorage.values.put(entry.getKey(), buffer.readBoolean());
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.NUMBER) this.controlsStorage.values.put(entry.getKey(), buffer.readInt());
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.ANIMATION) this.controlsStorage.values.put(entry.getKey(), buffer.readInt());
        }
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);

        for (Entry<TardisConsoleControlRoles, Object> entry : this.controlsStorage.values.entrySet()) {
            if (entry.getKey().type == TardisConsoleControlRoleTypes.BOOLEAN) buffer.writeBoolean((boolean) entry.getValue());
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.BOOLEAN_DIRECT) buffer.writeBoolean((boolean) entry.getValue());
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.NUMBER) buffer.writeInt((int) entry.getValue());
            else if (entry.getKey().type == TardisConsoleControlRoleTypes.ANIMATION) buffer.writeInt((int) entry.getValue());
        }
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();
                final BlockEntity blockEntity = mc.level.getBlockEntity(blockPos);

                if (blockEntity instanceof BaseTardisConsoleBlockEntity baseTardisConsoleBlockEntity) {
                    baseTardisConsoleBlockEntity.controlsStorage = controlsStorage;
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

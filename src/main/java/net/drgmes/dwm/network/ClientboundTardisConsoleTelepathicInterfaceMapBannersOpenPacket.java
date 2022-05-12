package net.drgmes.dwm.network;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.blocks.tardis.consoles.screens.TardisConsoleTelepathicInterfaceMapBannersScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

public class ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket {
    private final BlockPos blockPos;
    private final MapItemSavedData mapData;

    public ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket(BlockPos blockPos, MapItemSavedData mapData) {
        this.blockPos = blockPos;
        this.mapData = mapData;
    }

    public ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket(FriendlyByteBuf buffer) {
        this(buffer.readBlockPos(), MapItemSavedData.load(buffer.readNbt()));
    }

    public void encode(FriendlyByteBuf buffer) {
        buffer.writeBlockPos(this.blockPos);
        buffer.writeNbt(this.mapData.save(new CompoundTag()));
    }

    public boolean handle(Supplier<NetworkEvent.Context> ctx) {
        final var success = new AtomicBoolean(false);

        ctx.get().enqueueWork(() -> DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                final Minecraft mc = Minecraft.getInstance();

                if (mc.level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
                    mc.setScreen(new TardisConsoleTelepathicInterfaceMapBannersScreen(tardisConsoleBlockEntity, mapData));
                    success.set(true);
                }
            }
        }));

        ctx.get().setPacketHandled(true);
        return success.get();
    }
}

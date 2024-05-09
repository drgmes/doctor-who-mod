package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.network.client.TardisConsoleUnitUpdatePacket;
import net.drgmes.dwm.setup.ModNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitInitPacket extends BaseC2SMessage {
    private final BlockPos blockPos;

    public TardisConsoleUnitInitPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static TardisConsoleUnitInitPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitInitPacket(buf.readBlockPos());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_INIT;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        if (player.getWorld().getBlockEntity(this.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            TardisStateManager.get((ServerWorld) player.getWorld()).ifPresent((tardis) -> {
                NbtCompound tardisData = tardisConsoleUnitBlockEntity.createNbt();

                tardisConsoleUnitBlockEntity.controlsStorage.applyDataToControlsStorage(tardis);
                tardisConsoleUnitBlockEntity.controlsStorage.save(tardisData);
                tardis.writeNbt(tardisData);

                tardisConsoleUnitBlockEntity.readNbt(tardisData);

                new TardisConsoleUnitUpdatePacket(this.blockPos, tardisData)
                    // TODO uncomment method when this will work properly
                    // .sendToChunkListeners(player.getWorld().getWorldChunk(this.blockPos));
                    .sendToLevel((ServerWorld) player.getWorld());
            });
        }
    }
}

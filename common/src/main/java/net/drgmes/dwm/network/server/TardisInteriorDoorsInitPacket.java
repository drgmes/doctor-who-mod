package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.setup.ModNetwork;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TardisInteriorDoorsInitPacket extends BaseC2SMessage {
    private final BlockPos blockPos;

    public TardisInteriorDoorsInitPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static TardisInteriorDoorsInitPacket create(PacketByteBuf buf) {
        return new TardisInteriorDoorsInitPacket(buf.readBlockPos());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_INTERIOR_DOORS_INIT;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        if (player.getWorld().getBlockEntity(this.blockPos) instanceof BaseTardisDoorsBlockEntity tardisDoorsBlockEntity) {
            tardisDoorsBlockEntity.init();
        }
    }
}

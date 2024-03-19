package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.misc.tardisroundel.TardisRoundelBlockEntity;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TardisRoundelUpdatePacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final boolean uncovered;
    private final boolean lightMode;

    public TardisRoundelUpdatePacket(BlockPos blockPos, boolean uncovered, boolean lightMode) {
        this.blockPos = blockPos;
        this.uncovered = uncovered;
        this.lightMode = lightMode;
    }

    public static TardisRoundelUpdatePacket create(PacketByteBuf buf) {
        return new TardisRoundelUpdatePacket(buf.readBlockPos(), buf.readBoolean(), buf.readBoolean());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_ROUNDEL_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeBoolean(this.uncovered);
        buf.writeBoolean(this.lightMode);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
            tardisRoundelBlockEntity.uncovered = this.uncovered;
            tardisRoundelBlockEntity.lightMode = this.lightMode;
        }
    }
}

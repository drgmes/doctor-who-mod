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

public class TardisRoundelBlockTemplateClearPacket extends BaseS2CMessage {
    private final BlockPos blockPos;

    public TardisRoundelBlockTemplateClearPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static TardisRoundelBlockTemplateClearPacket create(PacketByteBuf buf) {
        return new TardisRoundelBlockTemplateClearPacket(buf.readBlockPos());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_ROUNDEL_BLOCK_TEMPLATE_CLEAR;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
            tardisRoundelBlockEntity.blockTemplate = null;
        }
    }
}

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
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public class TardisRoundelBlockTemplateUpdatePacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final Identifier blockTemplate;

    public TardisRoundelBlockTemplateUpdatePacket(BlockPos blockPos, Identifier blockTemplate) {
        this.blockPos = blockPos;
        this.blockTemplate = blockTemplate;
    }

    public static TardisRoundelBlockTemplateUpdatePacket create(PacketByteBuf buf) {
        return new TardisRoundelBlockTemplateUpdatePacket(buf.readBlockPos(), buf.readIdentifier());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_ROUNDEL_BLOCK_TEMPLATE_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeIdentifier(this.blockTemplate);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof TardisRoundelBlockEntity tardisRoundelBlockEntity) {
            tardisRoundelBlockEntity.blockTemplate = this.blockTemplate;
        }
    }
}

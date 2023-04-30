package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner.TardisToyotaSpinnerBlockEntity;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TardisToyotaSpinnerUpdatePacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final boolean inProgress;

    public TardisToyotaSpinnerUpdatePacket(BlockPos blockPos, boolean inProgress) {
        this.blockPos = blockPos;
        this.inProgress = inProgress;
    }

    public static TardisToyotaSpinnerUpdatePacket create(PacketByteBuf buf) {
        return new TardisToyotaSpinnerUpdatePacket(buf.readBlockPos(), buf.readBoolean());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_TOYOTA_SPINNER_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeBoolean(this.inProgress);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof TardisToyotaSpinnerBlockEntity tardisToyotaSpinnerBlockEntity) {
            tardisToyotaSpinnerBlockEntity.inProgress = this.inProgress;
            tardisToyotaSpinnerBlockEntity.markDirty();
        }
    }
}

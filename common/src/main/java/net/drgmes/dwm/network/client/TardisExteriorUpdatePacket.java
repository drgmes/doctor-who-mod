package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.enums.TardisExteriorAction;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TardisExteriorUpdatePacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final TardisExteriorAction exteriorAction;

    public TardisExteriorUpdatePacket(BlockPos blockPos, TardisExteriorAction exteriorAction) {
        this.blockPos = blockPos;
        this.exteriorAction = exteriorAction;
    }

    public static TardisExteriorUpdatePacket create(PacketByteBuf buf) {
        return new TardisExteriorUpdatePacket(buf.readBlockPos(), TardisExteriorAction.valueOf(buf.readString()));
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_EXTERIOR_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeString(this.exteriorAction.name());
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            switch (exteriorAction) {
                case NORMALIZE -> tardisExteriorBlockEntity.normalize();
                case DEMAT -> tardisExteriorBlockEntity.demat();
                case REMAT -> tardisExteriorBlockEntity.remat();
                case PULSE -> tardisExteriorBlockEntity.pulse();
            }
        }
    }
}

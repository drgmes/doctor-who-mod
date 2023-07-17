package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.screens.TardisArsDestroyerScreen;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class ArsDestroyerOpenPacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final ArsStructure arsStructure;

    public ArsDestroyerOpenPacket(BlockPos blockPos, ArsStructure arsStructure) {
        this.blockPos = blockPos;
        this.arsStructure = arsStructure;
    }

    public ArsDestroyerOpenPacket(BlockPos blockPos, String arsStructureName) {
        this(blockPos, ArsStructures.STRUCTURES.get(arsStructureName));
    }

    public static ArsDestroyerOpenPacket create(PacketByteBuf buf) {
        return new ArsDestroyerOpenPacket(buf.readBlockPos(), ArsStructure.fromPacket(buf));
    }

    @Override
    public MessageType getType() {
        return ModNetwork.ARS_DESTROYER_OPEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        ArsStructure.toPacket(buf, this.arsStructure);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof TardisArsDestroyerBlockEntity) {
            mc.setScreen(new TardisArsDestroyerScreen(this.blockPos, this.arsStructure));
        }
    }
}

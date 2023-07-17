package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.screens.TardisArsCreatorScreen;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

import java.util.Map;

public class ArsCreatorOpenPacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final Map<String, ArsCategory> arsCategories;
    private final Map<String, ArsStructure> arsStructures;

    public ArsCreatorOpenPacket(BlockPos blockPos, Map<String, ArsCategory> arsCategories, Map<String, ArsStructure> arsStructures) {
        this.blockPos = blockPos;
        this.arsCategories = arsCategories;
        this.arsStructures = arsStructures;
    }

    public ArsCreatorOpenPacket(BlockPos blockPos) {
        this(blockPos, ArsCategories.CATEGORIES, ArsStructures.STRUCTURES);
    }

    public static ArsCreatorOpenPacket create(PacketByteBuf buf) {
        return new ArsCreatorOpenPacket(
            buf.readBlockPos(),
            buf.readMap(PacketByteBuf::readString, ArsCategory::fromPacket),
            buf.readMap(PacketByteBuf::readString, ArsStructure::fromPacket)
        );
    }

    @Override
    public MessageType getType() {
        return ModNetwork.ARS_CREATOR_OPEN;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeMap(this.arsCategories, PacketByteBuf::writeString, ArsCategory::toPacket);
        buf.writeMap(this.arsStructures, PacketByteBuf::writeString, ArsStructure::toPacket);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof TardisArsCreatorBlockEntity) {
            mc.setScreen(new TardisArsCreatorScreen(this.blockPos, this.arsCategories, this.arsStructures));
        }
    }
}

package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(DWM.MODID, "main"),
        () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void setup() {
        // int index = 0;

        // INSTANCE.messageBuilder(ClientboundTestUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
        //     .encoder(ClientboundTestUpdatePacket::encode).decoder(ClientboundTestUpdatePacket::new)
        //     .consumer(ClientboundTestUpdatePacket::handle).add();
    }

    public static void send(Object packet) {
        ModPackets.INSTANCE.send(PacketDistributor.ALL.noArg(), packet);
    }

    public static void send(LevelChunk levelChunk, Object packet) {
        ModPackets.INSTANCE.send(PacketDistributor.TRACKING_CHUNK.with(() -> levelChunk), packet);
    }

    public static void send(ServerPlayer serverPlayer, Object packet) {
        ModPackets.INSTANCE.send(PacketDistributor.PLAYER.with(() -> serverPlayer), packet);
    }
}

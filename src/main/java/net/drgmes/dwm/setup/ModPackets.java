package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.network.ClientboundTardisConsoleControlsUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleMonitorUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleScrewdriverSlotUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleTelepathicInterfaceOpenPacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleWorldDataUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisEngineUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisExteriorUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisInteriorDoorsUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisToyotaSpinnerUpdatePacket;
import net.drgmes.dwm.network.ServerboundTardisConsoleTelepathicInterfaceUpdatePacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModPackets {
    private static final String PROTOCOL_VERSION = "1";

    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
        new ResourceLocation(DWM.MODID, "main"),
        () -> PROTOCOL_VERSION,
        PROTOCOL_VERSION::equals,
        PROTOCOL_VERSION::equals
    );

    public static void setup() {
        int index = 0;

        INSTANCE.messageBuilder(ServerboundTardisConsoleTelepathicInterfaceUpdatePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(ServerboundTardisConsoleTelepathicInterfaceUpdatePacket::encode).decoder(ServerboundTardisConsoleTelepathicInterfaceUpdatePacket::new)
            .consumer(ServerboundTardisConsoleTelepathicInterfaceUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisConsoleTelepathicInterfaceOpenPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisConsoleTelepathicInterfaceOpenPacket::encode).decoder(ClientboundTardisConsoleTelepathicInterfaceOpenPacket::new)
            .consumer(ClientboundTardisConsoleTelepathicInterfaceOpenPacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisExteriorUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisExteriorUpdatePacket::encode).decoder(ClientboundTardisExteriorUpdatePacket::new)
            .consumer(ClientboundTardisExteriorUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisInteriorDoorsUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisInteriorDoorsUpdatePacket::encode).decoder(ClientboundTardisInteriorDoorsUpdatePacket::new)
            .consumer(ClientboundTardisInteriorDoorsUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisEngineUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisEngineUpdatePacket::encode).decoder(ClientboundTardisEngineUpdatePacket::new)
            .consumer(ClientboundTardisEngineUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisConsoleMonitorUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisConsoleMonitorUpdatePacket::encode).decoder(ClientboundTardisConsoleMonitorUpdatePacket::new)
            .consumer(ClientboundTardisConsoleMonitorUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisConsoleControlsUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisConsoleControlsUpdatePacket::encode).decoder(ClientboundTardisConsoleControlsUpdatePacket::new)
            .consumer(ClientboundTardisConsoleControlsUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisConsoleScrewdriverSlotUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisConsoleScrewdriverSlotUpdatePacket::encode).decoder(ClientboundTardisConsoleScrewdriverSlotUpdatePacket::new)
            .consumer(ClientboundTardisConsoleScrewdriverSlotUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisConsoleWorldDataUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisConsoleWorldDataUpdatePacket::encode).decoder(ClientboundTardisConsoleWorldDataUpdatePacket::new)
            .consumer(ClientboundTardisConsoleWorldDataUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisToyotaSpinnerUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisToyotaSpinnerUpdatePacket::encode).decoder(ClientboundTardisToyotaSpinnerUpdatePacket::new)
            .consumer(ClientboundTardisToyotaSpinnerUpdatePacket::handle).add();
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

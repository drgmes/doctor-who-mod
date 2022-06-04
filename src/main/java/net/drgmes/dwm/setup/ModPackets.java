package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.network.ClientboundBotiUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleControlsUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleLevelDataUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleMonitorUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleScrewdriverSlotUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket;
import net.drgmes.dwm.network.ClientboundTardisEngineUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisExteriorUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisInteriorDoorsUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisToyotaSpinnerUpdatePacket;
import net.drgmes.dwm.network.ServerboundScrewdriverUpdatePacket;
import net.drgmes.dwm.network.ServerboundScrewdriverUsePacket;
import net.drgmes.dwm.network.ServerboundTardisConsoleInitPacket;
import net.drgmes.dwm.network.ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket;
import net.drgmes.dwm.network.ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket;
import net.drgmes.dwm.network.ServerboundTardisInteriorDoorsInitPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
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

        INSTANCE.messageBuilder(ServerboundScrewdriverUsePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(ServerboundScrewdriverUsePacket::encode).decoder(ServerboundScrewdriverUsePacket::new)
            .consumer(ServerboundScrewdriverUsePacket::handle).add();

        INSTANCE.messageBuilder(ServerboundScrewdriverUpdatePacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(ServerboundScrewdriverUpdatePacket::encode).decoder(ServerboundScrewdriverUpdatePacket::new)
            .consumer(ServerboundScrewdriverUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ServerboundTardisInteriorDoorsInitPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(ServerboundTardisInteriorDoorsInitPacket::encode).decoder(ServerboundTardisInteriorDoorsInitPacket::new)
            .consumer(ServerboundTardisInteriorDoorsInitPacket::handle).add();

        INSTANCE.messageBuilder(ServerboundTardisConsoleInitPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(ServerboundTardisConsoleInitPacket::encode).decoder(ServerboundTardisConsoleInitPacket::new)
            .consumer(ServerboundTardisConsoleInitPacket::handle).add();

        INSTANCE.messageBuilder(ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket::encode).decoder(ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket::new)
            .consumer(ServerboundTardisConsoleTelepathicInterfaceLocationsApplyPacket::handle).add();

        INSTANCE.messageBuilder(ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket.class, index++, NetworkDirection.PLAY_TO_SERVER)
            .encoder(ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket::encode).decoder(ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket::new)
            .consumer(ServerboundTardisConsoleTelepathicInterfaceMapBannersApplyPacket::handle).add();

        INSTANCE.messageBuilder(ClientboundBotiUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundBotiUpdatePacket::encode).decoder(ClientboundBotiUpdatePacket::new)
            .consumer(ClientboundBotiUpdatePacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket::encode).decoder(ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket::new)
            .consumer(ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket::handle).add();

        INSTANCE.messageBuilder(ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket::encode).decoder(ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket::new)
            .consumer(ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket::handle).add();

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

        INSTANCE.messageBuilder(ClientboundTardisConsoleLevelDataUpdatePacket.class, index++, NetworkDirection.PLAY_TO_CLIENT)
            .encoder(ClientboundTardisConsoleLevelDataUpdatePacket::encode).decoder(ClientboundTardisConsoleLevelDataUpdatePacket::new)
            .consumer(ClientboundTardisConsoleLevelDataUpdatePacket::handle).add();

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

    public static void send(ServerLevel level, Object packet) {
        ModPackets.INSTANCE.send(PacketDistributor.DIMENSION.with(level::dimension), packet);
    }
}

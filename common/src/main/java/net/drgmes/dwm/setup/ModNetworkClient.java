package net.drgmes.dwm.setup;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.network.client.*;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

/**
 * Registers S2C packet receivers. Only referenced from client entrypoints so
 * {@link ClientPackets} is never loaded on a dedicated server.
 */
@Environment(EnvType.CLIENT)
public final class ModNetworkClient {
    private ModNetworkClient() {
    }

    public static void setup() {
        Registration.registerPacket(NetworkManager.Side.S2C, ArsCreatorOpenPacket.PACKET_ID, ArsCreatorOpenPacket.PACKET_CODEC, () -> ClientPackets::handleArsCreatorOpen);
        Registration.registerPacket(NetworkManager.Side.S2C, ArsDestroyerOpenPacket.PACKET_ID, ArsDestroyerOpenPacket.PACKET_CODEC, () -> ClientPackets::handleArsDestroyerOpen);
        Registration.registerPacket(NetworkManager.Side.S2C, DimensionAddPacket.PACKET_ID, DimensionAddPacket.PACKET_CODEC, () -> ClientPackets::handleDimensionAdd);
        Registration.registerPacket(NetworkManager.Side.S2C, DimensionRemovePacket.PACKET_ID, DimensionRemovePacket.PACKET_CODEC, () -> ClientPackets::handleDimensionRemove);
        Registration.registerPacket(NetworkManager.Side.S2C, SonicDeviceUpdatePacket.PACKET_ID, SonicDeviceUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleSonicDeviceUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitControlsStatesUpdatePacket.PACKET_ID, TardisConsoleUnitControlsStatesUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleTardisConsoleUnitControlsStatesUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitMonitorOpenPacket.PACKET_ID, TardisConsoleUnitMonitorOpenPacket.PACKET_CODEC, () -> ClientPackets::handleTardisConsoleUnitMonitorOpen);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitMonitorPageUpdatePacket.PACKET_ID, TardisConsoleUnitMonitorPageUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleTardisConsoleUnitMonitorPageUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitSonicScrewdriverSlotUpdatePacket.PACKET_ID, TardisConsoleUnitSonicScrewdriverSlotUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleTardisConsoleUnitSonicScrewdriverSlotUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket.PACKET_CODEC, () -> ClientPackets::handleTardisConsoleUnitTelepathicInterfaceLocationsOpen);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket.PACKET_CODEC, () -> ClientPackets::handleTardisConsoleUnitTelepathicInterfaceMapBannersOpen);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitUpdatePacket.PACKET_ID, TardisConsoleUnitUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleTardisConsoleUnitUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisExteriorUpdatePacket.PACKET_ID, TardisExteriorUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleTardisExteriorUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisRoundelBlockTemplateClearPacket.PACKET_ID, TardisRoundelBlockTemplateClearPacket.PACKET_CODEC, () -> ClientPackets::handleTardisRoundelBlockTemplateClear);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisRoundelBlockTemplateUpdatePacket.PACKET_ID, TardisRoundelBlockTemplateUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleTardisRoundelBlockTemplateUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisRoundelUpdatePacket.PACKET_ID, TardisRoundelUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleTardisRoundelUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisToyotaSpinnerUpdatePacket.PACKET_ID, TardisToyotaSpinnerUpdatePacket.PACKET_CODEC, () -> ClientPackets::handleTardisToyotaSpinnerUpdate);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisTeleporterOpenPacket.PACKET_ID, TardisTeleporterOpenPacket.PACKET_CODEC, () -> ClientPackets::handleTardisTeleporterOpen);
    }
}

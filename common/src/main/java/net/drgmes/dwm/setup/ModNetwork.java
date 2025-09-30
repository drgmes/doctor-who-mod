package net.drgmes.dwm.setup;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.network.client.*;
import net.drgmes.dwm.network.server.*;

public class ModNetwork {
    public static void setup() {
        Registration.registerPacket(NetworkManager.Side.S2C, ArsCreatorOpenPacket.PACKET_ID, ArsCreatorOpenPacket.PACKET_CODEC, () -> ArsCreatorOpenPacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, ArsDestroyerOpenPacket.PACKET_ID, ArsDestroyerOpenPacket.PACKET_CODEC, () -> ArsDestroyerOpenPacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, DimensionAddPacket.PACKET_ID, DimensionAddPacket.PACKET_CODEC, () -> DimensionAddPacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, DimensionRemovePacket.PACKET_ID, DimensionRemovePacket.PACKET_CODEC, () -> DimensionRemovePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, SonicDeviceUpdatePacket.PACKET_ID, SonicDeviceUpdatePacket.PACKET_CODEC, () -> SonicDeviceUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitControlsStatesUpdatePacket.PACKET_ID, TardisConsoleUnitControlsStatesUpdatePacket.PACKET_CODEC, () -> TardisConsoleUnitControlsStatesUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitMonitorOpenPacket.PACKET_ID, TardisConsoleUnitMonitorOpenPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorOpenPacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitMonitorPageUpdatePacket.PACKET_ID, TardisConsoleUnitMonitorPageUpdatePacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorPageUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitSonicScrewdriverSlotUpdatePacket.PACKET_ID, TardisConsoleUnitSonicScrewdriverSlotUpdatePacket.PACKET_CODEC, () -> TardisConsoleUnitSonicScrewdriverSlotUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket.PACKET_CODEC, () -> TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket.PACKET_CODEC, () -> TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisConsoleUnitUpdatePacket.PACKET_ID, TardisConsoleUnitUpdatePacket.PACKET_CODEC, () -> TardisConsoleUnitUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisExteriorUpdatePacket.PACKET_ID, TardisExteriorUpdatePacket.PACKET_CODEC, () -> TardisExteriorUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisRoundelBlockTemplateClearPacket.PACKET_ID, TardisRoundelBlockTemplateClearPacket.PACKET_CODEC, () -> TardisRoundelBlockTemplateClearPacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisRoundelBlockTemplateUpdatePacket.PACKET_ID, TardisRoundelBlockTemplateUpdatePacket.PACKET_CODEC, () -> TardisRoundelBlockTemplateUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisRoundelUpdatePacket.PACKET_ID, TardisRoundelUpdatePacket.PACKET_CODEC, () -> TardisRoundelUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisToyotaSpinnerUpdatePacket.PACKET_ID, TardisToyotaSpinnerUpdatePacket.PACKET_CODEC, () -> TardisToyotaSpinnerUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.S2C, TardisTeleporterOpenPacket.PACKET_ID, TardisTeleporterOpenPacket.PACKET_CODEC, () -> TardisTeleporterOpenPacket::handle);

        Registration.registerPacket(NetworkManager.Side.C2S, ArsCreatorApplyPacket.PACKET_ID, ArsCreatorApplyPacket.PACKET_CODEC, () -> ArsCreatorApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, ArsDestroyerApplyPacket.PACKET_ID, ArsDestroyerApplyPacket.PACKET_CODEC, () -> ArsDestroyerApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, SonicDeviceModeUpdatePacket.PACKET_ID, SonicDeviceModeUpdatePacket.PACKET_CODEC, () -> SonicDeviceModeUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, SonicDeviceUsePacket.PACKET_ID, SonicDeviceUsePacket.PACKET_CODEC, () -> SonicDeviceUsePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorConsoleRoomApplyPacket.PACKET_ID, TardisConsoleUnitMonitorConsoleRoomApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorConsoleRoomApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorExternalShellApplyPacket.PACKET_ID, TardisConsoleUnitMonitorExternalShellApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorExternalShellApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorHistoryApplyPacket.PACKET_ID, TardisConsoleUnitMonitorHistoryApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorHistoryApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorHistoryClearPacket.PACKET_ID, TardisConsoleUnitMonitorHistoryClearPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorHistoryClearPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorHistoryDeletePacket.PACKET_ID, TardisConsoleUnitMonitorHistoryDeletePacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorHistoryDeletePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitSoundPacket.PACKET_ID, TardisConsoleUnitSoundPacket.PACKET_CODEC, () -> TardisConsoleUnitSoundPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitTelepathicInterfaceLocationApplyPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceLocationApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitTelepathicInterfaceLocationApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisTeleporterApplyPacket.PACKET_ID, TardisTeleporterApplyPacket.PACKET_CODEC, () -> TardisTeleporterApplyPacket::handle);
    }
}

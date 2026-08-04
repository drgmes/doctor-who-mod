package net.drgmes.dwm.setup;

import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import dev.architectury.utils.EnvExecutor;
import net.drgmes.dwm.network.client.*;
import net.drgmes.dwm.network.server.*;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;

public class ModNetwork {
    public static void setup() {
        // Dedicated servers need S2C payload types without loading client receivers.
        // Clients register types+receivers together via ModNetworkClient.
        if (Platform.getEnvironment() == Env.SERVER) {
            registerS2CType(ArsCreatorOpenPacket.PACKET_ID, ArsCreatorOpenPacket.PACKET_CODEC);
            registerS2CType(ArsDestroyerOpenPacket.PACKET_ID, ArsDestroyerOpenPacket.PACKET_CODEC);
            registerS2CType(DimensionAddPacket.PACKET_ID, DimensionAddPacket.PACKET_CODEC);
            registerS2CType(DimensionRemovePacket.PACKET_ID, DimensionRemovePacket.PACKET_CODEC);
            registerS2CType(SonicDeviceUpdatePacket.PACKET_ID, SonicDeviceUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisConsoleUnitControlsStatesUpdatePacket.PACKET_ID, TardisConsoleUnitControlsStatesUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisConsoleUnitMonitorOpenPacket.PACKET_ID, TardisConsoleUnitMonitorOpenPacket.PACKET_CODEC);
            registerS2CType(TardisConsoleUnitMonitorPageUpdatePacket.PACKET_ID, TardisConsoleUnitMonitorPageUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisConsoleUnitSonicScrewdriverSlotUpdatePacket.PACKET_ID, TardisConsoleUnitSonicScrewdriverSlotUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket.PACKET_CODEC);
            registerS2CType(TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket.PACKET_CODEC);
            registerS2CType(TardisConsoleUnitUpdatePacket.PACKET_ID, TardisConsoleUnitUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisExteriorUpdatePacket.PACKET_ID, TardisExteriorUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisRoundelBlockTemplateClearPacket.PACKET_ID, TardisRoundelBlockTemplateClearPacket.PACKET_CODEC);
            registerS2CType(TardisRoundelBlockTemplateUpdatePacket.PACKET_ID, TardisRoundelBlockTemplateUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisRoundelUpdatePacket.PACKET_ID, TardisRoundelUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisToyotaSpinnerUpdatePacket.PACKET_ID, TardisToyotaSpinnerUpdatePacket.PACKET_CODEC);
            registerS2CType(TardisTeleporterOpenPacket.PACKET_ID, TardisTeleporterOpenPacket.PACKET_CODEC);
        }

        // Supplier is only invoked on the client, so ClientPackets is never loaded dedicated-server-side.
        EnvExecutor.runInEnv(Env.CLIENT, () -> ModNetworkClient::setup);

        Registration.registerPacket(NetworkManager.Side.C2S, ArsCreatorApplyPacket.PACKET_ID, ArsCreatorApplyPacket.PACKET_CODEC, () -> ArsCreatorApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, ArsDestroyerApplyPacket.PACKET_ID, ArsDestroyerApplyPacket.PACKET_CODEC, () -> ArsDestroyerApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, SonicDeviceModeUpdatePacket.PACKET_ID, SonicDeviceModeUpdatePacket.PACKET_CODEC, () -> SonicDeviceModeUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, SonicDeviceUsePacket.PACKET_ID, SonicDeviceUsePacket.PACKET_CODEC, () -> SonicDeviceUsePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorConsoleRoomApplyPacket.PACKET_ID, TardisConsoleUnitMonitorConsoleRoomApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorConsoleRoomApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorExternalShellApplyPacket.PACKET_ID, TardisConsoleUnitMonitorExternalShellApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorExternalShellApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorHistoryApplyPacket.PACKET_ID, TardisConsoleUnitMonitorHistoryApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorHistoryApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorHistoryClearPacket.PACKET_ID, TardisConsoleUnitMonitorHistoryClearPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorHistoryClearPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorHistoryDeletePacket.PACKET_ID, TardisConsoleUnitMonitorHistoryDeletePacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorHistoryDeletePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorWaypointApplyPacket.PACKET_ID, TardisConsoleUnitMonitorWaypointApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorWaypointApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorWaypointCreatePacket.PACKET_ID, TardisConsoleUnitMonitorWaypointCreatePacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorWaypointCreatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorWaypointDeletePacket.PACKET_ID, TardisConsoleUnitMonitorWaypointDeletePacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorWaypointDeletePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitMonitorWaypointUpdatePacket.PACKET_ID, TardisConsoleUnitMonitorWaypointUpdatePacket.PACKET_CODEC, () -> TardisConsoleUnitMonitorWaypointUpdatePacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitSoundPacket.PACKET_ID, TardisConsoleUnitSoundPacket.PACKET_CODEC, () -> TardisConsoleUnitSoundPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitTelepathicInterfaceLocationApplyPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceLocationApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitTelepathicInterfaceLocationApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket.PACKET_ID, TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket.PACKET_CODEC, () -> TardisConsoleUnitTelepathicInterfaceMapBannerApplyPacket::handle);
        Registration.registerPacket(NetworkManager.Side.C2S, TardisTeleporterApplyPacket.PACKET_ID, TardisTeleporterApplyPacket.PACKET_CODEC, () -> TardisTeleporterApplyPacket::handle);
    }

    private static <T extends CustomPayload> void registerS2CType(CustomPayload.Id<T> id, PacketCodec<? super RegistryByteBuf, T> codec) {
        NetworkManager.registerS2CPayloadType(id, codec);
    }
}

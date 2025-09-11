package net.drgmes.dwm.compat.immersiveportals;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.IPPerServerInfo;
import qouteall.q_misc_util.MiscNetworking;
import qouteall.q_misc_util.dimension.DimIntIdMap;

public class ImmersivePortalsUtils {
    public static void addWorldToIPRegistry(MinecraftServer server, RegistryKey<World> worldKey) {
        DimIntIdMap dimIntIdMap = IPPerServerInfo.of(server).dimIntIdMap;
        assert dimIntIdMap != null;

        if (dimIntIdMap.containsDimId(worldKey)) return;
        dimIntIdMap.add(worldKey, dimIntIdMap.getNextIntegerId());
        sendSyncPacket(server);
    }

    public static void removeWorldFromIPRegistry(MinecraftServer server, RegistryKey<World> worldKey) {
        DimIntIdMap dimIntIdMap = IPPerServerInfo.of(server).dimIntIdMap;
        assert dimIntIdMap != null;

        if (!dimIntIdMap.containsDimId(worldKey)) return;
        dimIntIdMap.remove(worldKey);
        sendSyncPacket(server);
    }

    @ExpectPlatform
    public static void sendSyncPacket(MinecraftServer server) {
        Packet<ClientCommonPacketListener> packet = MiscNetworking.DimIdSyncPacket.createPacket(server);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.networkHandler.send(packet, null);
        }
    }
}

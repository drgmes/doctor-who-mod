package net.drgmes.dwm.compat.immersiveportals.neoforge;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import qouteall.q_misc_util.MiscNetworking;

public class ImmersivePortalsUtilsImpl {
    public static void sendSyncPacket(MinecraftServer server) {
        MiscNetworking.DimIdSyncPacket packet = MiscNetworking.DimIdSyncPacket.createPacket(server);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.networkHandler.send(packet, null);
        }
    }
}

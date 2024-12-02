package net.drgmes.dwm.compat.immersiveportals.fabric;

import net.minecraft.network.listener.ClientCommonPacketListener;
import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import qouteall.q_misc_util.MiscNetworking;

public class ImmersivePortalsUtilsImpl {
    public static void sendSyncPacket(MinecraftServer server) {
        Packet<ClientCommonPacketListener> packet = MiscNetworking.DimIdSyncPacket.createPacket(server);

        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            player.networkHandler.send(packet, null);
        }
    }
}

package net.drgmes.dwm.compat.immersiveportals.neoforge;

import net.minecraft.network.packet.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;

/** NeoForge Immersive Portals is Mojang-mapped; call its API via reflection. */
public class ImmersivePortalsUtilsImpl {
    public static void sendSyncPacket(MinecraftServer server) {
        try {
            Class<?> packetClass = Class.forName("qouteall.q_misc_util.MiscNetworking$DimIdSyncPacket");
            Object packet = packetClass.getMethod("createPacket", MinecraftServer.class).invoke(null, server);

            for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
                player.networkHandler.send((Packet<?>) packet, null);
            }
        }
        catch (ReflectiveOperationException | ClassCastException e) {
            throw new RuntimeException("Failed to sync Immersive Portals dimension IDs", e);
        }
    }
}

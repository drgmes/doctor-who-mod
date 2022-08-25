package net.drgmes.dwm.utils.helpers;

import net.minecraft.client.MinecraftClient;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.chunk.WorldChunk;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.api.McRemoteProcedureCall;

import java.util.List;

public class PacketHelper {
    public static void sendToClient(Class<?> packetClass, String packetMethod, MinecraftServer server, Object... args) {
        for (ServerPlayerEntity player : server.getPlayerManager().getPlayerList()) {
            sendToClient(packetClass, packetMethod, player, args);
        }
    }

    public static void sendToClient(Class<?> packetClass, String packetMethod, ServerWorld world, Object... args) {
        for (ServerPlayerEntity player : world.getPlayers()) {
            sendToClient(packetClass, packetMethod, player, args);
        }
    }

    public static void sendToClient(Class<?> packetClass, String packetMethod, WorldChunk chunk, int radius, Object... args) {
        List<ServerPlayerEntity> players = ((ServerWorld) chunk.getWorld()).getPlayers();

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                ChunkPos chunkPos = new ChunkPos(chunk.getPos().x + x, chunk.getPos().z + z);

                for (ServerPlayerEntity player : players) {
                    if (player.getChunkPos().equals(chunkPos)) {
                        sendToClient(packetClass, packetMethod, player, args);
                    }
                }
            }
        }
    }

    public static void sendToClient(Class<?> packetClass, String packetMethod, WorldChunk chunk, Object... args) {
        sendToClient(packetClass, packetMethod, chunk, 3, args);
    }

    public static void sendToClient(Class<?> packetClass, String packetMethod, ServerPlayerEntity player, Object... args) {
        MiscHelper.getServer().execute(() -> {
            McRemoteProcedureCall.tellClientToInvoke(player, packetClass.getName() + "." + packetMethod, args);
        });
    }

    public static void sendToServer(Class<?> packetClass, String packetMethod, Object... args) {
        MinecraftClient.getInstance().execute(() -> {
            McRemoteProcedureCall.tellServerToInvoke(packetClass.getName() + "." + packetMethod, args);
        });
    }
}

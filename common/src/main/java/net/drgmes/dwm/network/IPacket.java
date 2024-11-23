package net.drgmes.dwm.network;

import dev.architectury.networking.NetworkManager;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;

public interface IPacket extends CustomPayload {
    default void sendToServer() {
        NetworkManager.sendToServer(this);
    }

    default void sendTo(ServerPlayerEntity player) {
        NetworkManager.sendToPlayer(player, this);
    }

    default void sendTo(Iterable<ServerPlayerEntity> players) {
        NetworkManager.sendToPlayers(players, this);
    }

    default void sendToAll(MinecraftServer server) {
        NetworkManager.sendToPlayers(server.getPlayerManager().getPlayerList(), this);
    }

    default void sendToWorld(ServerWorld world) {
        NetworkManager.sendToPlayers(world.getPlayers(), this);
    }

    default void sendToChunkListeners(WorldChunk chunk) {
        NetworkManager.sendToPlayers(((ServerChunkManager) chunk.getWorld().getChunkManager()).chunkLoadingManager.getPlayersWatchingChunk(chunk.getPos(), false), this);
    }
}

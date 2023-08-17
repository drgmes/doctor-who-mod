package net.drgmes.dwm.utils.helpers.fabric;

import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;

import java.util.Set;

public class CommonHelperImpl {
    public static boolean teleport(Entity entity, ServerWorld destination, Vec3d pos, float yaw) {
        if (entity instanceof ServerPlayerEntity player) {
            player.inTeleportationState = true;
            ServerWorld prevWorld = player.getServerWorld();

            WorldProperties worldProperties = destination.getLevelProperties();
            player.networkHandler.sendPacket(new PlayerRespawnS2CPacket(destination.getDimensionKey(), destination.getRegistryKey(), BiomeAccess.hashSeed(destination.getSeed()), player.interactionManager.getGameMode(), player.interactionManager.getPreviousGameMode(), destination.isDebugWorld(), destination.isFlat(), (byte) 3, player.getLastDeathPos(), player.getPortalCooldown()));
            player.networkHandler.sendPacket(new DifficultyS2CPacket(worldProperties.getDifficulty(), worldProperties.isDifficultyLocked()));

            PlayerManager playerManager = player.server.getPlayerManager();
            playerManager.sendCommandTree(player);
            prevWorld.removePlayer(player, Entity.RemovalReason.CHANGED_DIMENSION);
            player.unsetRemoved();

            player.setServerWorld(destination);
            player.networkHandler.requestTeleport(pos.x, pos.y, pos.z, yaw, 0);
            player.networkHandler.syncWithPlayerPosition();
            destination.onPlayerChangeDimension(player);

            player.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.getAbilities()));
            playerManager.sendWorldInfo(player, destination);
            playerManager.sendPlayerStatus(player);

            for (StatusEffectInstance statusEffectInstance : player.getStatusEffects()) {
                player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), statusEffectInstance));
            }

            player.syncedExperience = -1;
            player.syncedFoodLevel = -1;
            player.syncedHealth = -1.0f;
            return true;
        }

        return entity.teleport(destination, pos.x, pos.y, pos.z, Set.of(), yaw, 0);
    }
}

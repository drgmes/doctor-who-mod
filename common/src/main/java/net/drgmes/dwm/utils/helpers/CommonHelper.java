package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.network.packet.s2c.play.DifficultyS2CPacket;
import net.minecraft.network.packet.s2c.play.EntityStatusEffectS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerAbilitiesS2CPacket;
import net.minecraft.network.packet.s2c.play.PlayerRespawnS2CPacket;
import net.minecraft.server.PlayerManager;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.WorldProperties;
import net.minecraft.world.biome.source.BiomeAccess;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CommonHelper {
    private static final Map<String, Thread> threads = new HashMap<>();

    public static Thread runInThread(String name, Runnable consumer) {
        if (CommonHelper.threads.containsKey(name)) CommonHelper.threads.get(name).interrupt();

        Thread thread = new Thread(consumer);
        CommonHelper.threads.put(name, thread);
        thread.start();

        return thread;
    }

    public static Identifier loadRemoteImage(String id, URL url) {
        try {
            URLConnection uc = url.openConnection();
            uc.connect();

            NativeImageBackedTexture image = new NativeImageBackedTexture(NativeImage.read(uc.getInputStream()));
            return MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(DWM.MODID + "_" + id, image);
        } catch (IOException ignored) {
        }

        return null;
    }

    public static String capitaliseAllWords(String str) {
        if (str == null) return null;

        boolean space = true;
        StringBuilder buffer = new StringBuilder(str.length());

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (Character.isWhitespace(ch)) {
                buffer.append(ch);
                space = true;
            }
            else if (space) {
                buffer.append(Character.toTitleCase(ch));
                space = false;
            }
            else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }

    public static String formatNumberString(int number) {
        String str = String.valueOf(number);
        if (number >= 1000000000) str = String.format("%.1f", (float) number / 1000000000) + "b";
        else if (number >= 1000000) str = String.format("%.1f", (float) number / 1000000) + "m";
        else if (number >= 1000) str = String.format("%.1f", (float) number / 1000) + "k";
        return str.replace(",", ".");
    }

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

            destination.getServer().getPlayerManager().sendPlayerStatus(player);
            destination.getServer().getPlayerManager().sendWorldInfo(player, destination);
            player.networkHandler.sendPacket(new PlayerAbilitiesS2CPacket(player.getAbilities()));

            for (StatusEffectInstance statusEffectInstance : player.getStatusEffects()) {
                player.networkHandler.sendPacket(new EntityStatusEffectS2CPacket(player.getId(), statusEffectInstance));
            }

            return true;
        }

        return entity.teleport(destination, pos.x, pos.y, pos.z, Set.of(), yaw, 0);
    }
}

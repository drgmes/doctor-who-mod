package net.drgmes.dwm.utils.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

import java.util.UUID;

public class EntityHelper {
    public static void sendMessage(MinecraftServer server, UUID playerId, Text message) {
        if (playerId == null) return;

        PlayerEntity player = server.getPlayerManager().getPlayer(playerId);
        if (player != null) player.sendMessage(message, true);
    }

    public static void sendMessage(ServerWorld world, UUID playerId, Text message) {
        sendMessage(world.getServer(), playerId, message);
    }

    public static Entity teleport(Entity entity, ServerWorld destination, Vec3d position, float yaw, float pitch) {
        TeleportTarget.PostDimensionTransition transition = TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET);
        return entity.teleportTo(new TeleportTarget(destination, position, entity.getVelocity(), yaw, pitch, transition));
    }

    public static Entity teleport(Entity entity, ServerWorld destination, Vec3d position, float yaw) {
        return teleport(entity, destination, position, yaw, 0);
    }

    public static HitResult pick(Entity entity, double distance) {
        for (int i = 0; i < distance; i++) {
            HitResult hitResult = entity.raycast(i, 0.0F, false);

            if (hitResult.getType() != HitResult.Type.MISS) {
                return hitResult;
            }

            float range = 0.25F;
            Vec3d min = hitResult.getPos().add(range, range, range);
            Vec3d max = hitResult.getPos().add(-range, -range, -range);
            for (Entity e : entity.getWorld().getOtherEntities(entity, new Box(min.x, min.y, min.z, max.x, max.y, max.z))) {
                return new EntityHitResult(e);
            }
        }

        return null;
    }
}

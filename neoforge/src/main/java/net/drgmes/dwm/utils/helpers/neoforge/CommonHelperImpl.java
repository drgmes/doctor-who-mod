package net.drgmes.dwm.utils.helpers.neoforge;

import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class CommonHelperImpl {
    public static Entity teleport(Entity entity, ServerWorld destination, Vec3d position, float yaw, float pitch) {
        TeleportTarget.PostDimensionTransition transition = TeleportTarget.SEND_TRAVEL_THROUGH_PORTAL_PACKET.then(TeleportTarget.ADD_PORTAL_CHUNK_TICKET);
        return entity.teleportTo(new TeleportTarget(destination, position, entity.getVelocity(), yaw, pitch, transition));
    }
}

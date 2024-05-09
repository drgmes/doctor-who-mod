package net.drgmes.dwm.utils.helpers.fabric;

import net.fabricmc.fabric.api.dimension.v1.FabricDimensions;
import net.minecraft.entity.Entity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;

public class CommonHelperImpl {
    public static Entity teleport(Entity entity, ServerWorld destination, Vec3d pos, float yaw) {
        return FabricDimensions.teleport(entity, destination, new TeleportTarget(
            new Vec3d(pos.x, pos.y, pos.z),
            new Vec3d(0, 0, 0),
            yaw,
            0
        ));
    }
}

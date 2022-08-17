package net.drgmes.dwm.utils.helpers;

import net.minecraft.entity.Entity;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class PlayerHelper {
    public static HitResult pick(Entity entity, double distance) {
        for (int i = 0; i < distance; i++) {
            HitResult hitResult = entity.raycast(i, 0.0F, false);

            if (hitResult.getType() != HitResult.Type.MISS) {
                return hitResult;
            }

            float range = 0.25F;
            Vec3d min = hitResult.getPos().add(range, range, range);
            Vec3d max = hitResult.getPos().add(-range, -range, -range);
            for (Entity e : entity.world.getOtherEntities(entity, new Box(min.x, min.y, min.z, max.x, max.y, max.z))) {
                return new EntityHitResult(e);
            }
        }

        return null;
    }
}

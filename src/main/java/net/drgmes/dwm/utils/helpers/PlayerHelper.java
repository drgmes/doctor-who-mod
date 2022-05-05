package net.drgmes.dwm.utils.helpers;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class PlayerHelper {
    public static HitResult pick(Entity entity, double distance) {
        for (int i = 0; i < distance; i++) {
            HitResult hitResult = entity.pick(i, 0.0F, false);

            if (hitResult.getType() != HitResult.Type.MISS) {
                return hitResult;
            }

            float range = 0.25F;
            Vec3 min = hitResult.getLocation().add(range, range, range);
            Vec3 max = hitResult.getLocation().add(-range, -range, -range);
            for (Entity e : entity.level.getEntities(entity, new AABB(min.x, min.y, min.z, max.x, max.y, max.z))) {
                return new EntityHitResult(e);
            }
        }

        return null;
    }
}

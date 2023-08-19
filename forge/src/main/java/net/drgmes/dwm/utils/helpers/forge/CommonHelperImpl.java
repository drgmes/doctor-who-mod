package net.drgmes.dwm.utils.helpers.forge;

import net.minecraft.entity.Entity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Function;

public class CommonHelperImpl {
    public static boolean teleport(Entity entity, ServerWorld destination, Vec3d pos, float yaw) {
        return entity.changeDimension(destination, new DWMTeleporter(pos, yaw, 0)) != null;
    }

    private static class DWMTeleporter implements ITeleporter {
        private final Vec3d pos;
        private final float yaw;
        private final float pitch;

        public DWMTeleporter(Vec3d pos, float yaw, int pitch) {
            this.pos = pos;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        @Override
        public Entity placeEntity(Entity entity, ServerWorld origin, ServerWorld destination, float yaw, Function<Boolean, Entity> repositionEntity) {
            return repositionEntity.apply(false);
        }

        @Override
        public TeleportTarget getPortalInfo(Entity entity, ServerWorld destWorld, Function<ServerWorld, TeleportTarget> defaultPortalInfo) {
            return new TeleportTarget(this.pos, Vec3d.ZERO, this.yaw, this.pitch);
        }

        @Override
        public boolean playTeleportSound(ServerPlayerEntity player, ServerWorld origin, ServerWorld destination) {
            return false;
        }
    }
}

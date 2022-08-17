package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

import java.util.List;

public class TardisSystemShields implements ITardisSystem {
    private final TardisStateManager tardis;

    public TardisSystemShields(TardisStateManager tardis) {
        this.tardis = tardis;
    }

    @Override
    public boolean isEnabled() {
        return this.tardis.isSystemEnabled(this.getClass());
    }

    @Override
    public boolean inProgress() {
        return this.isEnabled() && this.tardis.isShieldsEnabled();
    }

    @Override
    public void load(NbtCompound tag) {
    }

    @Override
    public NbtCompound save() {
        return new NbtCompound();
    }

    @Override
    public void tick() {
        if (!this.inProgress()) return;

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.tardis.getCurrentExteriorDimension());
        if (exteriorWorld == null) return;

        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        if (exteriorWorld.getBlockState(exteriorBlockPos).getBlock() instanceof BaseTardisExteriorBlock) {
            double radius = 6.0D;
            Vec3d blockPosVec = Vec3d.ofBottomCenter(exteriorBlockPos);
            List<Entity> entities = exteriorWorld.getEntitiesByClass(Entity.class, Box.of(blockPosVec, radius, radius, radius), EntityPredicates.EXCEPT_SPECTATOR);

            for (Entity entity : entities) {
                if (entity instanceof PlayerEntity player) {
                    if (!player.isTouchingWater()) continue;
                    player.addStatusEffect(new StatusEffectInstance(StatusEffects.WATER_BREATHING, 40));
                    continue;
                }

                double distance = blockPosVec.distanceTo(entity.getPos());
                Vec3d deltaMovement = entity.getPos().subtract(blockPosVec).multiply(radius / distance / 10);
                entity.setVelocity(deltaMovement);
            }
        }
    }

    public void setState(boolean flag) {
        this.tardis.setShieldsState(this.isEnabled() && flag);
    }
}

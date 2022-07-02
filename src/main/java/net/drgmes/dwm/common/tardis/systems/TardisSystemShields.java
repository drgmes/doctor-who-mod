package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TardisSystemShields implements ITardisSystem {
    private final ITardisLevelData tardis;

    public TardisSystemShields(ITardisLevelData tardis) {
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
    public void load(CompoundTag tag) {
    }

    @Override
    public CompoundTag save() {
        return new CompoundTag();
    }

    @Override
    public void tick() {
        if (!this.inProgress()) return;

        ServerLevel level = this.tardis.getLevel();
        if (level == null) return;

        ServerLevel exteriorLevel = level.getServer().getLevel(this.tardis.getCurrentExteriorDimension());
        if (exteriorLevel == null) return;

        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        if (exteriorLevel.getBlockState(exteriorBlockPos).getBlock() instanceof BaseTardisExteriorBlock) {
            double radius = 6.0D;
            Vec3 blockPosVec = Vec3.atBottomCenterOf(exteriorBlockPos);
            List<Entity> entities = exteriorLevel.getEntitiesOfClass(Entity.class, AABB.ofSize(blockPosVec, radius, radius, radius));

            for (Entity entity : entities) {
                double distance = blockPosVec.distanceTo(entity.position());
                Vec3 deltaMovement = entity.position().subtract(blockPosVec).scale(radius / distance / 10);
                entity.setDeltaMovement(deltaMovement);
            }
        }
    }

    public void setState(boolean flag) {
        this.tardis.setShieldsState(this.isEnabled() && flag);
    }
}

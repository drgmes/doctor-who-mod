package net.drgmes.dwm.common.tardis.consoles.controls;

import net.drgmes.dwm.entities.tardis.consolecontrol.TardisConsoleControlEntity;
import net.drgmes.dwm.entities.tardis.consolecontrol.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;

public class TardisConsoleControl {
    public final Vec3 position;
    public final TardisConsoleControlRoles role;
    public final TardisConsoleControlEntityBuilder builder;

    public TardisConsoleControl(TardisConsoleControlRoles role, Vec3 position, TardisConsoleControlEntityBuilder builder) {
        this.role = role;
        this.position = position;
        this.builder = builder;
    }

    public EntityType<TardisConsoleControlEntity> getEntityType() {
        return this.builder.get();
    }

    public TardisConsoleControlEntity createEntity(BaseTardisConsoleBlockEntity tile, Level level, BlockPos blockPos) {
        TardisConsoleControlEntity entity = this.getEntityType().create(level);
        Direction direction = level.getBlockState(blockPos).getValue(HorizontalDirectionalBlock.FACING);
        Vec3 pos = Vec3.atCenterOf(blockPos).add(this.position.yRot(1.57F * ((direction.toYRot() - 90) / -90)));

        entity.setPos(pos);
        entity.setTardisConsole(tile);
        entity.setTardisControl(this);

        level.addFreshEntity(entity);
        return entity;
    }
}

package net.drgmes.dwm.common.tardis.consoles.controls;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntityBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.phys.Vec3;

public class TardisConsoleControlEntry {
    public final String modelPath;
    public final Vec3 position;
    public final TardisConsoleControlRoles role;
    public final TardisConsoleControlEntryTypes type;
    public final TardisConsoleControlEntityBuilder builder;

    public TardisConsoleControlEntry(TardisConsoleControlRoles controlRole, TardisConsoleControlEntryTypes controlType, Vec3 position, String modelPath, TardisConsoleControlEntityBuilder builder) {
        this.modelPath = modelPath;
        this.builder = builder;
        this.position = position;
        this.role = controlRole;
        this.type = controlType;
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
        entity.setTardisControlEntry(this);

        level.addFreshEntity(entity);
        return entity;
    }
}

package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntityBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TardisConsoleControlEntry {
    public final String modelPath;
    public final Vec3d position;
    public final ETardisConsoleUnitControlRole role;
    public final ETardisConsoleUnitControlEntry type;
    public final TardisConsoleControlEntityBuilder builder;

    public TardisConsoleControlEntry(ETardisConsoleUnitControlRole role, ETardisConsoleUnitControlEntry type, Vec3d position, String modelPath, TardisConsoleControlEntityBuilder builder) {
        this.role = role;
        this.type = type;
        this.position = position;
        this.modelPath = modelPath;
        this.builder = builder;
    }

    public EntityType<TardisConsoleControlEntity> getEntityType() {
        return this.builder.getEntityType();
    }

    public TardisConsoleControlEntity createEntity(BaseTardisConsoleUnitBlockEntity tile, World world, BlockPos blockPos) {
        TardisConsoleControlEntity entity = this.getEntityType().create(world);
        Direction direction = world.getBlockState(blockPos).get(Properties.HORIZONTAL_FACING);
        Vec3d pos = Vec3d.ofCenter(blockPos).add(this.position.rotateY(1.57F * ((direction.asRotation() - 90) / -90)));

        assert entity != null;
        entity.setPosition(pos);
        entity.setTardisControlRole(this.role);
        entity.setTardisConsolePos(tile);

        world.spawnEntity(entity);
        return entity;
    }
}

package net.drgmes.dwm.common.tardis.consoles.controls;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntityBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TardisConsoleControlEntry {
    public final String modelPath;
    public final Vec3d position;
    public final ETardisConsoleControlRole role;
    public final ETardisConsoleControlEntry type;
    public final TardisConsoleControlEntityBuilder builder;

    public TardisConsoleControlEntry(ETardisConsoleControlRole controlRole, ETardisConsoleControlEntry controlType, Vec3d position, String modelPath, TardisConsoleControlEntityBuilder builder) {
        this.modelPath = modelPath;
        this.builder = builder;
        this.position = position;
        this.role = controlRole;
        this.type = controlType;
    }

    public EntityType<TardisConsoleControlEntity> getEntityType() {
        return this.builder.getEntityType();
    }

    public TardisConsoleControlEntity createEntity(BaseTardisConsoleBlockEntity tile, World world, BlockPos blockPos) {
        TardisConsoleControlEntity entity = this.getEntityType().create(world);
        Direction direction = world.getBlockState(blockPos).get(Properties.HORIZONTAL_FACING);
        Vec3d pos = Vec3d.ofCenter(blockPos).add(this.position.rotateY(1.57F * ((direction.asRotation() - 90) / -90)));

        assert entity != null;
        entity.setPosition(pos);
        entity.setTardisConsole(tile);
        entity.setTardisControlEntry(this);

        world.spawnEntity(entity);
        return entity;
    }
}

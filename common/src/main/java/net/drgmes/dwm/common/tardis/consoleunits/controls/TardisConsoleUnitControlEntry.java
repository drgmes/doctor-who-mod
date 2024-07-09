package net.drgmes.dwm.common.tardis.consoleunits.controls;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntityBuilder;
import net.drgmes.dwm.enums.TardisConsoleUnitControlRole;
import net.drgmes.dwm.enums.TardisConsoleUnitControlType;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class TardisConsoleUnitControlEntry {
    public final String modelPath;
    public final Vec3d position;
    public final TardisConsoleUnitControlRole role;
    public final TardisConsoleUnitControlType type;
    public final TardisConsoleControlEntityBuilder builder;

    public TardisConsoleUnitControlEntry(TardisConsoleUnitControlRole role, TardisConsoleUnitControlType type, Vec3d position, String modelPath, TardisConsoleControlEntityBuilder builder) {
        this.role = role;
        this.type = type;
        this.position = position;
        this.modelPath = modelPath;
        this.builder = builder;
    }

    public EntityType<TardisConsoleControlEntity> getEntityType() {
        return this.builder.getEntityType();
    }

    public TardisConsoleControlEntity createEntity(BaseTardisConsoleUnitBlockEntity tile, World world, BlockPos blockPos, BlockState blockState) {
        TardisConsoleControlEntity entity = this.getEntityType().create(world);
        Direction direction = blockState.get(Properties.HORIZONTAL_FACING);
        Vec3d pos = Vec3d.ofCenter(blockPos).add(this.position.rotateY(1.57F * ((direction.asRotation() - 90) / -90)));

        assert entity != null;
        entity.setPosition(pos);
        entity.setTardisControlRole(this.role);
        entity.setTardisConsolePos(tile.getPos(), tile);

        String name = this.role.name;
        if (name == null || name.isEmpty()) name = this.role.name().toLowerCase();
        entity.setCustomName(Text.translatable("title.dwm.tardis.control.role." + name + ".name"));

        world.spawnEntity(entity);
        return entity;
    }
}

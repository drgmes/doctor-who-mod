package net.drgmes.dwm.utils.builders;

import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class EntityBuilder<T extends Entity> {
    private final String name;
    private final EntityType<T> entityType;

    public EntityBuilder(String name, EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, float width, float height, int trackingRange, int updateFreq) {
        this.name = name;
        this.entityType = Registration.registerEntity(name, factory, spawnGroup, width, height, trackingRange, updateFreq);

        ModEntities.ENTITY_BUILDERS.add(this);
    }

    public EntityBuilder(String name, EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, float width, float height) {
        this(name, factory, spawnGroup, width, height, 10, Integer.MAX_VALUE);
    }

    public String getName() {
        return this.name;
    }

    public EntityType<T> getEntityType() {
        return this.entityType;
    }

    public void registerCustomRender() {
    }
}

package net.drgmes.dwm.utils.builders;

import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

import java.util.function.Supplier;

public class EntityBuilder<C extends Entity> {
    public final Supplier<EntityType<C>> entityType;
    private final String name;

    public EntityBuilder(String name, Supplier<EntityType<C>> entityTypeFactory) {
        this.name = name;
        this.entityType = Registration.registerEntity(name, entityTypeFactory);

        ModEntities.ENTITY_BUILDERS.add(this);
    }

    public EntityBuilder(String name, EntityType.EntityFactory<C> factory, SpawnGroup spawnGroup, float width, float height, int maxTrackingRange, int trackingTickInterval) {
        this(name, () -> {
            EntityType.Builder<C> builder = EntityType.Builder.create(factory, spawnGroup);
            builder.setDimensions(width, height);
            builder.maxTrackingRange(maxTrackingRange);
            builder.trackingTickInterval(trackingTickInterval);
            return builder.build(name);
        });
    }

    public EntityBuilder(String name, SpawnGroup spawnGroup, float width, float height, int maxTrackingRange, int trackingTickInterval) {
        this(name, (type, world) -> null, spawnGroup, width, height, maxTrackingRange, trackingTickInterval);
    }

    public EntityBuilder(String name, EntityType.EntityFactory<C> factory, SpawnGroup spawnGroup, float width, float height) {
        this(name, factory, spawnGroup, width, height, 10, Integer.MAX_VALUE);
    }

    public EntityBuilder(String name, SpawnGroup spawnGroup, float width, float height) {
        this(name, spawnGroup, width, height, 10, Integer.MAX_VALUE);
    }

    public String getName() {
        return this.name;
    }

    public EntityType<C> getEntityType() {
        return this.entityType.get();
    }
}

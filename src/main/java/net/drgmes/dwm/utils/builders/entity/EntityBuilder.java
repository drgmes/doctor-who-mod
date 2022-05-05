package net.drgmes.dwm.utils.builders.entity;

import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.RegistryObject;

public class EntityBuilder<T extends Entity> {
    public final String name;
    public final RegistryObject<EntityType<T>> entityObject;

    public EntityBuilder(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate) {
        this.name = name;
        this.entityObject = Registration.registerEntity(name, factory, category, width, height, trackingRange, updateFreq, sendUpdate);

        ModEntities.ENTITY_BUILDERS.add(this);
    }

    public EntityBuilder(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height) {
        this(name, factory, category, width, height, 64, 40, false);
    }

    public EntityType<T> get() {
        return this.entityObject.get();
    }

    public String getResourceName() {
        return this.name;
    }

    public void registerCustomEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
    }

    public void registerCustomLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }
}

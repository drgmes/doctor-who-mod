package net.drgmes.dwm.entities.tardis.consoles.controls;

import net.drgmes.dwm.utils.builders.entity.EntityBuilder;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class TardisConsoleControlEntityBuilder extends EntityBuilder<TardisConsoleControlEntity> {
    public TardisConsoleControlEntityBuilder(String name, float width, float height) {
        super(name, TardisConsoleControlEntity::new, MobCategory.MISC, width, height);
    }

    public TardisConsoleControlEntityBuilder(String name, float size) {
        this(name, size, size);
    }

    public TardisConsoleControlEntityBuilder(String name) {
        this(name, 0.1F);
    }

    @Override
    public void registerCustomRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(this.get(), TardisConsoleControlEntityRenderer::new);
    }
}

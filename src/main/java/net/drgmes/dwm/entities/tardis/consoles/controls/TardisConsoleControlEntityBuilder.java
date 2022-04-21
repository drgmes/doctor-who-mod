package net.drgmes.dwm.entities.tardis.consoles.controls;

import net.drgmes.dwm.setup.ModEntities;
import net.drgmes.dwm.utils.builders.entity.EntityBuilder;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class TardisConsoleControlEntityBuilder extends EntityBuilder<TardisConsoleControlEntity> {
    public TardisConsoleControlEntityBuilder(String name) {
        super(name, TardisConsoleControlEntity::new, MobCategory.MISC, 0.1F, 0.1F);
    }

    @Override
    public void registerCustomRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.TARDIS_CONSOLE_CONTROL.get(), TardisConsoleControlEntityRenderer::new);
    }
}

package net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockBuilder;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.models.TardisEngineToyotaModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class TardisEngineToyotaBlockBuilder extends BaseTardisEngineBlockBuilder {
    public TardisEngineToyotaBlockBuilder(String name) {
        super(name, () -> new TardisEngineToyotaBlock(getBlockBehaviour()));
    }

    @Override
    public void registerCustomEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TARDIS_ENGINE_TOYOTA.get(), TardisEngineToyotaBlockRenderer::new);
    }

    @Override
    public void registerCustomLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisEngineToyotaModel.LAYER_LOCATION, TardisEngineToyotaModel::createBodyLayer);
    }
}

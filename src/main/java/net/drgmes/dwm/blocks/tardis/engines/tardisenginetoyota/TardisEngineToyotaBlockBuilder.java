package net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlockBuilder;
import net.drgmes.dwm.blocks.tardis.engines.tardisenginetoyota.models.TardisEngineToyotaModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class TardisEngineToyotaBlockBuilder extends BaseTardisEngineBlockBuilder {
    public TardisEngineToyotaBlockBuilder(String name) {
        super(name, new TardisEngineToyotaBlock(getBlockSettings()));
    }

    @Override
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisEngineToyotaModel.LAYER_LOCATION, TardisEngineToyotaModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_ENGINE_TOYOTA, TardisEngineToyotaBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisEngineToyotaItemRenderer());
    }
}

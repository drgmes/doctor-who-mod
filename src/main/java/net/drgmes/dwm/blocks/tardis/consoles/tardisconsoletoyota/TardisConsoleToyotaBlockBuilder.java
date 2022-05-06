package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota.models.TardisConsoleToyotaModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class TardisConsoleToyotaBlockBuilder extends BaseTardisConsoleBlockBuilder {
    public TardisConsoleToyotaBlockBuilder(String name) {
        super(name, () -> new TardisConsoleToyotaBlock(getBlockBehaviour()));
    }

    @Override
    public void registerCustomEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.TARDIS_CONSOLE_TOYOTA.get(), TardisConsoleToyotaBlockRenderer::new);
    }

    @Override
    public void registerCustomLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisConsoleToyotaModel.LAYER_LOCATION, TardisConsoleToyotaModel::createBodyLayer);
    }
}

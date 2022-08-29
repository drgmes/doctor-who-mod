package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota.models.TardisConsoleToyotaModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class TardisConsoleToyotaBlockBuilder extends BaseTardisConsoleBlockBuilder {
    public TardisConsoleToyotaBlockBuilder(String name) {
        super(name, new TardisConsoleToyotaBlock(getBlockSettings()));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisConsoleToyotaModel.LAYER_LOCATION, TardisConsoleToyotaModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_CONSOLE_TOYOTA, TardisConsoleToyotaBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisConsoleToyotaItemRenderer());
    }
}

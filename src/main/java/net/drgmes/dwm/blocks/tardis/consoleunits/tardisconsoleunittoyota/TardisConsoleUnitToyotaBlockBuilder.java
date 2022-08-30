package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota.models.TardisConsoleUnitToyotaModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class TardisConsoleUnitToyotaBlockBuilder extends BaseTardisConsoleUnitBlockBuilder {
    public TardisConsoleUnitToyotaBlockBuilder(String name) {
        super(name, new TardisConsoleUnitToyotaBlock(getBlockSettings()));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisConsoleUnitToyotaModel.LAYER_LOCATION, TardisConsoleUnitToyotaModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_CONSOLE_UNIT_TOYOTA, TardisConsoleUnitToyotaBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisConsoleUnitToyotaItemRenderer());
    }
}

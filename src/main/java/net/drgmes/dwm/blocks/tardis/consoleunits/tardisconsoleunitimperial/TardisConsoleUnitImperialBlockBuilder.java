package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial.models.TardisConsoleUnitImperialModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class TardisConsoleUnitImperialBlockBuilder extends BaseTardisConsoleUnitBlockBuilder {
    public TardisConsoleUnitImperialBlockBuilder(String name) {
        super(name, new TardisConsoleUnitImperialBlock(getBlockSettings()));
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisConsoleUnitImperialModel.LAYER_LOCATION, TardisConsoleUnitImperialModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_CONSOLE_UNIT_IMPERIAL, TardisConsoleUnitImperialBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisConsoleUnitImperialItemRenderer());
    }
}

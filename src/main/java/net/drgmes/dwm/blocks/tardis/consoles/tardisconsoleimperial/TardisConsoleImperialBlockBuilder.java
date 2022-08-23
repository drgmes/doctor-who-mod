package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoleimperial;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockBuilder;
import net.drgmes.dwm.blocks.tardis.consoles.tardisconsoleimperial.models.TardisConsoleImperialModel;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BuiltinItemRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;

public class TardisConsoleImperialBlockBuilder extends BaseTardisConsoleBlockBuilder {
    public TardisConsoleImperialBlockBuilder(String name) {
        super(name, new TardisConsoleImperialBlock(getBlockSettings()));
    }

    @Override
    public void registerCustomRender() {
        EntityModelLayerRegistry.registerModelLayer(TardisConsoleImperialModel.LAYER_LOCATION, TardisConsoleImperialModel::getTexturedModelData);
        BlockEntityRendererRegistry.register(ModBlockEntities.TARDIS_CONSOLE_IMPERIAL, TardisConsoleImperialBlockRenderer::new);
        BuiltinItemRendererRegistry.INSTANCE.register(this.getBlockItem(), new TardisConsoleImperialItemRenderer());
    }
}

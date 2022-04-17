package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardisexterior.models.TardisPoliceBoxExteriorModel;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ModRenderers {
    public static void setup(EntityRenderersEvent.RegisterRenderers event) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerCustomRenderer(event);
        }
    }

    public static void setupLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisPoliceBoxExteriorModel.LAYER_LOCATION, TardisPoliceBoxExteriorModel::createBodyLayer);
    }
}

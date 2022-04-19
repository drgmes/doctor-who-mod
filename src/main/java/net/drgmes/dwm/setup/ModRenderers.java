package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.consoles.tardisconsoletoyota.models.TardisConsoleToyotaModel;
import net.drgmes.dwm.blocks.tardisexterior.models.TardisPoliceBoxExteriorModel;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.builders.entity.EntityBuilder;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ModRenderers {
    public static void setup(EntityRenderersEvent.RegisterRenderers event) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerCustomRenderer(event);
        }

        for (EntityBuilder<?> entityBuilder : ModEntities.ENTITY_BUILDERS) {
            entityBuilder.registerCustomRenderer(event);
        }
    }

    public static void setupLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(TardisPoliceBoxExteriorModel.LAYER_LOCATION, TardisPoliceBoxExteriorModel::createBodyLayer);
        event.registerLayerDefinition(TardisConsoleToyotaModel.LAYER_LOCATION, TardisConsoleToyotaModel::createBodyLayer);
    }
}

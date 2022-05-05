package net.drgmes.dwm.setup;

import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.builders.entity.EntityBuilder;
import net.minecraftforge.client.event.EntityRenderersEvent;

public class ModRenderers {
    public static void setup(EntityRenderersEvent.RegisterRenderers event) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerCustomEntityRenderer(event);
        }

        for (EntityBuilder<?> entityBuilder : ModEntities.ENTITY_BUILDERS) {
            entityBuilder.registerCustomEntityRenderer(event);
        }
    }

    public static void setupLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerCustomLayerDefinitions(event);
        }

        for (EntityBuilder<?> entityBuilder : ModEntities.ENTITY_BUILDERS) {
            entityBuilder.registerCustomLayerDefinitions(event);
        }
    }
}

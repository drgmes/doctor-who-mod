package net.drgmes.dwm.setup;

import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.builders.EntityBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;

public class ModRenderers {
    public static void setup() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerCustomRender();
        }

        for (ItemBuilder itemBuilder : ModItems.ITEM_BUILDERS) {
            itemBuilder.registerCustomRender();
        }

        for (EntityBuilder<?> entityBuilder : ModEntities.ENTITY_BUILDERS) {
            entityBuilder.registerCustomRender();
        }
    }
}

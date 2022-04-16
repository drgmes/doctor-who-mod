package net.drgmes.dwm.setup;

import net.drgmes.dwm.utils.builders.block.BlockBuilder;

public class ModRender {
    public static void setup() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerCustomRender();
        }
    }
}

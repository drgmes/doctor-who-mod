package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.AbstractBlock;

public class TardisConsoleUnitImperialBlock extends BaseTardisConsoleUnitBlock<TardisConsoleUnitImperialBlockEntity> {
    public TardisConsoleUnitImperialBlock(AbstractBlock.Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_CONSOLE_UNIT_IMPERIAL);
    }
}

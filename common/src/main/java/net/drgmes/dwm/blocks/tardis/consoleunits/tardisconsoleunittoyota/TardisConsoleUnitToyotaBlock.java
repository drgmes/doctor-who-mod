package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.AbstractBlock;

public class TardisConsoleUnitToyotaBlock extends BaseTardisConsoleUnitBlock<TardisConsoleUnitToyotaBlockEntity> {
    public TardisConsoleUnitToyotaBlock(AbstractBlock.Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_CONSOLE_UNIT_TOYOTA);
    }
}

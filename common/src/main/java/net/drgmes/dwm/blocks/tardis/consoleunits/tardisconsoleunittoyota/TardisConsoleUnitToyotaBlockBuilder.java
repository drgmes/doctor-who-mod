package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockBuilder;

public class TardisConsoleUnitToyotaBlockBuilder extends BaseTardisConsoleUnitBlockBuilder {
    public TardisConsoleUnitToyotaBlockBuilder(String name) {
        super(name, () -> new TardisConsoleUnitToyotaBlock(getBlockSettings()));
    }
}

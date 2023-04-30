package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockBuilder;

public class TardisConsoleUnitImperialBlockBuilder extends BaseTardisConsoleUnitBlockBuilder {
    public TardisConsoleUnitImperialBlockBuilder(String name) {
        super(name, () -> new TardisConsoleUnitImperialBlock(getBlockSettings()));
    }
}

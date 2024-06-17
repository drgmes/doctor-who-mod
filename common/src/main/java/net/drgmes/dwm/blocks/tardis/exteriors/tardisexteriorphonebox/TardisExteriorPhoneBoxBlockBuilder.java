package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockBuilder;

public class TardisExteriorPhoneBoxBlockBuilder extends BaseTardisExteriorBlockBuilder {
    public TardisExteriorPhoneBoxBlockBuilder(String name) {
        super(name, () -> new TardisExteriorPhoneBoxBlock(getBlockSettings()));
    }
}

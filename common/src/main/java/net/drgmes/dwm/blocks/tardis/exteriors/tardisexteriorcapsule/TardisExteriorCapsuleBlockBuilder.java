package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorcapsule;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockBuilder;

public class TardisExteriorCapsuleBlockBuilder extends BaseTardisExteriorBlockBuilder {
    public TardisExteriorCapsuleBlockBuilder(String name) {
        super(name, () -> new TardisExteriorCapsuleBlock(getBlockSettings()));
    }
}

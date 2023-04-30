package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockBuilder;

public class TardisExteriorPoliceBoxBlockBuilder extends BaseTardisExteriorBlockBuilder {
    public TardisExteriorPoliceBoxBlockBuilder(String name) {
        super(name, () -> new TardisExteriorPoliceBoxBlock(getBlockSettings()));
    }
}

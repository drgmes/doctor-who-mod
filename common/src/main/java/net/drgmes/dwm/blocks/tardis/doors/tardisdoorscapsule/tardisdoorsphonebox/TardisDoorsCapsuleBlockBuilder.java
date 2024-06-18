package net.drgmes.dwm.blocks.tardis.doors.tardisdoorscapsule.tardisdoorsphonebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockBuilder;

public class TardisDoorsCapsuleBlockBuilder extends BaseTardisDoorsBlockBuilder {
    public TardisDoorsCapsuleBlockBuilder(String name) {
        super(name, () -> new TardisDoorsCapsuleBlock(getBlockSettings()));
    }
}

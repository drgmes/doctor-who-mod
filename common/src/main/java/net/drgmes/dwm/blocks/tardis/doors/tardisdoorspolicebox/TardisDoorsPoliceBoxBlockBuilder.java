package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockBuilder;

public class TardisDoorsPoliceBoxBlockBuilder extends BaseTardisDoorsBlockBuilder {
    public TardisDoorsPoliceBoxBlockBuilder(String name) {
        super(name, () -> new TardisDoorsPoliceBoxBlock(getBlockSettings()));
    }
}

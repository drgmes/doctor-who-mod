package net.drgmes.dwm.blocks.tardis.doors.tardisdoorsphonebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockBuilder;

public class TardisDoorsPhoneBoxBlockBuilder extends BaseTardisDoorsBlockBuilder {
    public TardisDoorsPhoneBoxBlockBuilder(String name) {
        super(name, () -> new TardisDoorsPhoneBoxBlock(getBlockSettings()));
    }
}

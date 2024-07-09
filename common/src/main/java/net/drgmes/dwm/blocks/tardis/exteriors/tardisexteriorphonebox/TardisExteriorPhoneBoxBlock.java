package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriors;

public class TardisExteriorPhoneBoxBlock extends BaseTardisExteriorBlock<TardisExteriorPhoneBoxBlockEntity> {
    public TardisExteriorPhoneBoxBlock(Settings settings) {
        super(settings, TardisExteriors.PHONE_BOX);
    }

    @Override
    public boolean isWooden() {
        return true;
    }
}

package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorphonebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorTypes;

public class TardisExteriorPhoneBoxBlock extends BaseTardisExteriorBlock<TardisExteriorPhoneBoxBlockEntity> {
    public TardisExteriorPhoneBoxBlock(Settings settings) {
        super(settings, TardisExteriorTypes.PHONE_BOX);
    }

    @Override
    public boolean isWooden() {
        return true;
    }
}

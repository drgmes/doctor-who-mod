package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorTypes;
import net.minecraft.block.AbstractBlock;

public class TardisExteriorPoliceBoxBlock extends BaseTardisExteriorBlock<TardisExteriorPoliceBoxBlockEntity> {
    public TardisExteriorPoliceBoxBlock(AbstractBlock.Settings settings) {
        super(settings, TardisExteriorTypes.POLICE_BOX);
    }

    @Override
    public boolean isWooden() {
        return true;
    }
}

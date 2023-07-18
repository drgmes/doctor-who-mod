package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.AbstractBlock;

public class TardisDoorsPoliceBoxBlock extends BaseTardisDoorsBlock<TardisDoorsPoliceBoxBlockEntity> {
    public TardisDoorsPoliceBoxBlock(AbstractBlock.Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_DOORS_POLICE_BOX);
    }

    @Override
    public boolean isWooden() {
        return true;
    }
}

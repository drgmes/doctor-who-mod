package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.common.tardis.doors.TardisDoors;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;

public class TardisDoorsPoliceBoxBlock extends BaseTardisDoorsBlock<TardisDoorsPoliceBoxBlockEntity> {
    public TardisDoorsPoliceBoxBlock(AbstractBlock.Settings settings) {
        super(
            settings,
            TardisDoors.POLICE_BOX,
            Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0),
            Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(-2.75, 0.0, 13.0, 0.0, 16.0, 16.0),
            Block.createCuboidShape(-2.75, 0.0, 0.0, 0.0, 16.0, 3.0),
            Block.createCuboidShape(0.0, 0.0, -2.75, 3.0, 16.0, 0.0),
            Block.createCuboidShape(13.0, 0.0, -2.75, 16.0, 16.0, 0.0),
            1.165F
        );
    }

    @Override
    public boolean isWooden() {
        return true;
    }
}

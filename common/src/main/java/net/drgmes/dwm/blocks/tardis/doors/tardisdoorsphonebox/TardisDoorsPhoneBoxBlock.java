package net.drgmes.dwm.blocks.tardis.doors.tardisdoorsphonebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.common.tardis.doors.TardisDoorsTypes;
import net.minecraft.block.Block;

public class TardisDoorsPhoneBoxBlock extends BaseTardisDoorsBlock<TardisDoorsPhoneBoxBlockEntity> {
    public TardisDoorsPhoneBoxBlock(Settings settings) {
        super(
            settings,
            TardisDoorsTypes.PHONE_BOX,
            Block.createCuboidShape(0.0, 0.0, 13.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0),
            Block.createCuboidShape(0.0, 0.0, 0.0, 3.0, 16.0, 16.0),
            Block.createCuboidShape(13.0, 0.0, 0.0, 16.0, 16.0, 16.0),
            Block.createCuboidShape(-1.425, 0.0, 13.25, 1.125, 16.0, 16.0),
            Block.createCuboidShape(-1.425, 0.0, 0.0, 1.125, 16.0, 2.75),
            Block.createCuboidShape(0.0, 0.0, -1.425, 2.75, 16.0, 1.125),
            Block.createCuboidShape(13.25, 0.0, -1.425, 16.0, 16.0, 1.125),
            1.0215F
        );
    }

    @Override
    public boolean isWooden() {
        return true;
    }
}

package net.drgmes.dwm.blocks.tardis.doors.tardisdoorsphonebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisDoorsPhoneBoxBlockEntity extends BaseTardisDoorsBlockEntity {
    public TardisDoorsPhoneBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_DOORS_PHONE_BOX.getBlockEntityType(), blockPos, blockState);
    }
}

package net.drgmes.dwm.blocks.tardis.doors.tardisdoorscapsule.tardisdoorsphonebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisDoorsCapsuleBlockEntity extends BaseTardisDoorsBlockEntity {
    public TardisDoorsCapsuleBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_DOORS_CAPSULE.getBlockEntityType(), blockPos, blockState);
    }
}

package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.util.math.BlockPos;

public class TardisDoorsPoliceBoxBlockEntity extends BaseTardisDoorsBlockEntity {
    public TardisDoorsPoliceBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_DOORS_POLICE_BOX, blockPos, blockState);
    }
}

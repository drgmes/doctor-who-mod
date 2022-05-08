package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public class TardisDoorsPoliceBoxBlockEntity extends BaseTardisDoorsBlockEntity {
    public String tardisLevelUUID;

    public TardisDoorsPoliceBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_DOORS_POLICE_BOX.get(), blockPos, blockState);
    }
}

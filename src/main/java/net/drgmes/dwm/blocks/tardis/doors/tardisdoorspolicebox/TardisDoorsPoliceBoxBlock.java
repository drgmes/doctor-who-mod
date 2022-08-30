package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;

public class TardisDoorsPoliceBoxBlock extends BaseTardisDoorsBlock<TardisDoorsPoliceBoxBlockEntity> {
    public TardisDoorsPoliceBoxBlock(AbstractBlock.Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_DOORS_POLICE_BOX);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) return null;
        return new TardisDoorsPoliceBoxBlockEntity(blockPos, blockState);
    }

    @Override
    public boolean isWooden() {
        return true;
    }
}

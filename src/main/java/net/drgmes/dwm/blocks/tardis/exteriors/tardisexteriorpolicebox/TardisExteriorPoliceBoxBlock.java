package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.util.math.BlockPos;

public class TardisExteriorPoliceBoxBlock extends BaseTardisExteriorBlock<TardisExteriorPoliceBoxBlockEntity> {
    public TardisExteriorPoliceBoxBlock(AbstractBlock.Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.get(HALF) != DoubleBlockHalf.LOWER) return null;
        return new TardisExteriorPoliceBoxBlockEntity(blockPos, blockState);
    }
}

package net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class TardisExteriorPoliceBoxBlock extends BaseTardisExteriorBlock<TardisExteriorPoliceBoxBlockEntity> {
    public TardisExteriorPoliceBoxBlock(BlockBehaviour.Properties properties) {
        super(properties, ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        if (blockState.getValue(HALF) != DoubleBlockHalf.LOWER) return null;
        return new TardisExteriorPoliceBoxBlockEntity(blockPos, blockState);
    }
}

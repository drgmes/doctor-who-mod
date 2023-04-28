package net.drgmes.dwm.blocks.tardis.engines.tardisengineimperial;

import net.drgmes.dwm.blocks.tardis.engines.BaseTardisEngineBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TardisEngineImperialBlock extends BaseTardisEngineBlock {
    public TardisEngineImperialBlock(AbstractBlock.Settings settings) {
        super(settings);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisEngineImperialBlockEntity(blockPos, blockState);
    }
}

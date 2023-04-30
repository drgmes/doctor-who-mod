package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunitimperial;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitImperialBlock extends BaseTardisConsoleUnitBlock<TardisConsoleUnitImperialBlockEntity> {
    public TardisConsoleUnitImperialBlock(AbstractBlock.Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_CONSOLE_UNIT_IMPERIAL);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisConsoleUnitImperialBlockEntity(blockPos, blockState);
    }
}

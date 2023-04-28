package net.drgmes.dwm.blocks.tardis.consoleunits.tardisconsoleunittoyota;

import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitToyotaBlock extends BaseTardisConsoleUnitBlock<TardisConsoleUnitToyotaBlockEntity> {
    public TardisConsoleUnitToyotaBlock(AbstractBlock.Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_CONSOLE_UNIT_TOYOTA);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisConsoleUnitToyotaBlockEntity(blockPos, blockState);
    }
}

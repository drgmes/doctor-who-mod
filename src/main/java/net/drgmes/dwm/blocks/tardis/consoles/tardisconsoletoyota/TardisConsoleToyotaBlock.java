package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleToyotaBlock extends BaseTardisConsoleBlock<TardisConsoleToyotaBlockEntity> {
    public TardisConsoleToyotaBlock(AbstractBlock.Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_CONSOLE_TOYOTA);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisConsoleToyotaBlockEntity(blockPos, blockState);
    }
}

package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoleimperial;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleImperialBlock extends BaseTardisConsoleBlock<TardisConsoleImperialBlockEntity> {
    public TardisConsoleImperialBlock(Settings settings) {
        super(settings, () -> ModBlockEntities.TARDIS_CONSOLE_IMPERIAL);
    }

    @Override
    public BlockEntity createBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisConsoleImperialBlockEntity(blockPos, blockState);
    }
}

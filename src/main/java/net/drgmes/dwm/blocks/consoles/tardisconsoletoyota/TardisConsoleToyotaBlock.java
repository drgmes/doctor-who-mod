package net.drgmes.dwm.blocks.consoles.tardisconsoletoyota;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.base.blocks.BaseTardisConsoleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TardisConsoleToyotaBlock extends BaseTardisConsoleBlock<TardisConsoleToyotaBlockEntity> {
    public TardisConsoleToyotaBlock(BlockBehaviour.Properties properties) {
        super(properties, ModBlockEntities.TARDIS_CONSOLE_TOYOTA);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisConsoleToyotaBlockEntity(blockPos, blockState);
    }
}

package net.drgmes.dwm.blocks.tardis.consoles.tardisconsoletoyota;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlock;
import net.drgmes.dwm.setup.ModBlockEntities;
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

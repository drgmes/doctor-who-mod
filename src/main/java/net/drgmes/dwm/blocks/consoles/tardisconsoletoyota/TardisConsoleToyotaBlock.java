package net.drgmes.dwm.blocks.consoles.tardisconsoletoyota;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TardisConsoleToyotaBlock extends BaseRotatableWaterloggedEntityBlock {
    public TardisConsoleToyotaBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new TardisConsoleToyotaBlockEntity(blockPos, blockState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return level.isClientSide ? null : createTickerHelper(blockEntityType, ModBlockEntities.TARDIS_CONSOLE_TOYOTA.get(), (l, bp, bs, blockEntity) -> {
            ((TardisConsoleToyotaBlockEntity) blockEntity).tick(l, bp, bs);
        });
    }
}

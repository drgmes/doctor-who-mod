package net.drgmes.dwm.blocks.tardis.consoles;

import net.drgmes.dwm.utils.base.blocks.BaseRotatableWaterloggedEntityBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Containers;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.RegistryObject;

public abstract class BaseTardisConsoleBlock<C extends BaseTardisConsoleBlockEntity> extends BaseRotatableWaterloggedEntityBlock {
    private final RegistryObject<BlockEntityType<C>> blockEntityTypeObject;

    public BaseTardisConsoleBlock(BlockBehaviour.Properties properties, RegistryObject<BlockEntityType<C>> blockEntityTypeObject) {
        super(properties);
        this.blockEntityTypeObject = blockEntityTypeObject;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState blockState, BlockEntityType<T> blockEntityType) {
        return createTickerHelper(blockEntityType, this.blockEntityTypeObject.get(), (l, bp, bs, blockEntity) -> {
            ((BaseTardisConsoleBlockEntity) blockEntity).tick();
        });
    }

    @Override
    public void onPlace(BlockState blockState, Level level, BlockPos blockPos, BlockState oldBlockState, boolean isMoving) {
        if (level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            tardisConsoleBlockEntity.loadAll();
        }
    }

    @Override
    @SuppressWarnings("deprecation")
    public void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState newBlockState, boolean isMoving) {
        if (level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            tardisConsoleBlockEntity.unloadAll();
        }

        super.onRemove(blockState, level, blockPos, newBlockState, isMoving);
    }

    @Override
    public void playerWillDestroy(Level level, BlockPos blockPos, BlockState blockState, Player player) {
        if (level.getBlockEntity(blockPos) instanceof BaseTardisConsoleBlockEntity tardisConsoleBlockEntity) {
            Vec3 pos = Vec3.atBottomCenterOf(blockPos);
            Containers.dropItemStack(level, pos.x, pos.y, pos.z, tardisConsoleBlockEntity.screwdriverItemStack);
            level.updateNeighbourForOutputSignal(blockPos, this);
        }

        super.playerWillDestroy(level, blockPos, blockState, player);
    }
}

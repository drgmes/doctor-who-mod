package net.drgmes.dwm.blocks.tardis.engines;

import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseTardisEngineBlockEntity extends BlockEntity {
    public BaseTardisEngineBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    private boolean checkTileIsInATardis() {
        return this.level != null && this.level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS);
    }
}

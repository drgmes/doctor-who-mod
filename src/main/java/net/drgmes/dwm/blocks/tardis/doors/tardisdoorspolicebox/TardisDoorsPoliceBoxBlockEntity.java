package net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TardisDoorsPoliceBoxBlockEntity extends BlockEntity {
    public String tardisLevelUUID;

    public TardisDoorsPoliceBoxBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_DOOR.get(), blockPos, blockState);
    }

	@Override
	public AABB getRenderBoundingBox() {
		return new AABB(this.worldPosition).inflate(3, 4, 3);
	}

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    public void onLoad() {
        super.onLoad();

        if (this.checkTileIsInATardis()) {
            this.tardisLevelUUID = this.level.dimension().location().getPath();

            if (!this.level.isClientSide) {
                this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
                    if (!levelProvider.isValid()) return;
    
                    levelProvider.setEntraceFacing(this.getBlockState().getValue(TardisDoorsPoliceBoxBlock.FACING));
                    levelProvider.setEntracePosition(this.getBlockPos());
                    levelProvider.getDoorTiles().add(this);
                    levelProvider.updateDoorTiles();
                });
            }
        }
    }

    @Override
    public void setRemoved() {
        this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
            if (levelProvider.isValid()) levelProvider.getDoorTiles().remove(this);
        });

        super.setRemoved();
    }

    private boolean checkTileIsInATardis() {
        return this.level != null && this.level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS);
    }
}

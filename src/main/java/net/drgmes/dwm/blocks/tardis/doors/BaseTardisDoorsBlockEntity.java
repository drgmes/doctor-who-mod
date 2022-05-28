package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.network.ServerboundTardisInteriorDoorsInitPacket;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public abstract class BaseTardisDoorsBlockEntity extends BlockEntity {
    public String tardisLevelUUID;

    public BaseTardisDoorsBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
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
        this.init();
    }

    @Override
    public void setRemoved() {
        this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            if (tardis.isValid()) tardis.getInteriorDoorTiles().remove(this);
        });

        super.setRemoved();
    }

    public void init() {
        if (TardisHelper.isTardisDimension(this.level)) {
            this.tardisLevelUUID = this.level.dimension().location().getPath();

            if (!this.level.isClientSide) {
                this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                    if (!tardis.isValid()) return;
                    if (tardis.getInteriorDoorTiles().contains(this)) return;

                    tardis.setEntraceFacing(this.getBlockState().getValue(BaseTardisDoorsBlock.FACING));
                    tardis.setEntracePosition(this.worldPosition);
                    tardis.getInteriorDoorTiles().add(this);
                    tardis.updateDoorsTiles();
                });
            }
            else {
                ModPackets.INSTANCE.sendToServer(new ServerboundTardisInteriorDoorsInitPacket(this.worldPosition));
            }
        }
    }
}

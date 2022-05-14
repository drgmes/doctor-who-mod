package net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner;

import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.network.ClientboundTardisToyotaSpinnerUpdatePacket;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

public class TardisToyotaSpinnerBlockEntity extends BlockEntity {
    public boolean inProgress = false;
    public float tickInProgress = 0;

    public TardisToyotaSpinnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_TOYOTA_SPINNER.get(), blockPos, blockState);
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

    public void tick() {
        if (!this.level.isClientSide && TardisHelper.isTardisDimension(this.level)) {
            this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                boolean prevInProgress = this.inProgress;

                if (tardis.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
                    if (!this.inProgress && materializationSystem.inProgress()) {
                        this.inProgress = true;
                    }
                    else if (this.inProgress && !materializationSystem.inProgress()) {
                        this.inProgress = false;
                    }
                }

                if (tardis.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
                    if (!this.inProgress && flightSystem.inProgress()) {
                        this.inProgress = true;
                    }
                }

                if (prevInProgress != this.inProgress) {
                    ClientboundTardisToyotaSpinnerUpdatePacket packet = new ClientboundTardisToyotaSpinnerUpdatePacket(this.worldPosition, this.inProgress);
                    ModPackets.send(level.getChunkAt(this.worldPosition), packet);
                }
            });
        }

        if (this.inProgress) {
            this.tickInProgress++;
            this.tickInProgress %= 360;
        }
    }
}

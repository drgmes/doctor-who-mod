package net.drgmes.dwm.blocks.tardis.misc.tardistoyotaspinner;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.network.client.TardisToyotaSpinnerUpdatePacket;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public class TardisToyotaSpinnerBlockEntity extends BlockEntity {
    public boolean inProgress = false;
    public float tickInProgress = 0;

    public TardisToyotaSpinnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_TOYOTA_SPINNER.getBlockEntityType(), blockPos, blockState);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    public void tick() {
        if (this.world != null && this.world instanceof ServerWorld serverWorld && TardisHelper.isTardisDimension(serverWorld)) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                boolean prevInProgress = this.inProgress;
                this.inProgress = tardis.getSystem(TardisSystemMaterialization.class).inProgress() || tardis.getSystem(TardisSystemFlight.class).inProgress();

                if (prevInProgress != this.inProgress) {
                    new TardisToyotaSpinnerUpdatePacket(this.getPos(), this.inProgress)
                        // TODO uncomment method when this will work properly
                        // .sendToChunkListeners(serverWorld.getWorldChunk(this.getPos()));
                        .sendToLevel(serverWorld);
                }
            });
        }

        if (this.inProgress) {
            this.tickInProgress++;
            this.tickInProgress %= 360;
        }
    }
}

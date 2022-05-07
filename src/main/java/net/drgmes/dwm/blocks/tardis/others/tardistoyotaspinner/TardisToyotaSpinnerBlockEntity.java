package net.drgmes.dwm.blocks.tardis.others.tardistoyotaspinner;

import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.network.ClientboundTardisToyotaSpinnerUpdatePacket;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TardisToyotaSpinnerBlockEntity extends BlockEntity {
    public float tickInProgress = 0;
    public boolean inProgress;

    public TardisToyotaSpinnerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_TOYOTA_SPINNER.get(), blockPos, blockState);
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
        if (!this.level.isClientSide && this.checkTileIsInATardis()) {
            this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
                if (levelProvider.isValid() && levelProvider.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
                    if (this.inProgress != flightSystem.inProgress()) {
                        this.inProgress = flightSystem.inProgress();

                        ClientboundTardisToyotaSpinnerUpdatePacket packet = new ClientboundTardisToyotaSpinnerUpdatePacket(this.worldPosition, this.inProgress);
                        ModPackets.send(level.getChunkAt(this.worldPosition), packet);
                    }
                }
            });
        }

        if (this.inProgress) {
            this.tickInProgress++;
            this.tickInProgress %= 360;
        }
    }

    private boolean checkTileIsInATardis() {
        return this.level != null && this.level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS);
    }
}

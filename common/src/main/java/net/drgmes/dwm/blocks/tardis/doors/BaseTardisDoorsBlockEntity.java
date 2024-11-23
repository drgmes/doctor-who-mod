package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.doors.TardisDoorsEntry;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class BaseTardisDoorsBlockEntity extends BlockEntity {
    private boolean inited;

    public BaseTardisDoorsBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (!this.inited) this.init();
        super.writeNbt(tag, registryLookup);
    }

    public TardisDoorsEntry getDoorsType() {
        return ((BaseTardisDoorsBlock<?>) this.getCachedState().getBlock()).doorsType;
    }

    public void init() {
        if (this.inited) return;
        this.inited = true;

        if (this.world instanceof ServerWorld serverWorld) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                tardis.addInteriorDoorsTile(this);
                tardis.markDoorsTilesUpdated();
                tardis.updateEntrancePortals();
            });
        }
    }

    public void remove() {
        if (this.world instanceof ServerWorld serverWorld) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                tardis.removeInteriorDoorsTile(this);
                tardis.updateEntrancePortals();
            });
        }
    }
}

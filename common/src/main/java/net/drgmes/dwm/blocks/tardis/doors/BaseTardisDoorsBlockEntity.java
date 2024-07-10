package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.doors.TardisDoorsEntry;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;

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
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        if (!this.inited) this.init();
        super.writeNbt(tag);
    }

    public TardisDoorsEntry getDoorsType() {
        return ((BaseTardisDoorsBlock<?>) this.getCachedState().getBlock()).doorsType;
    }

    public Box getRenderBoundingBox() {
        return WorldHelper.getRenderBoundingBox(this);
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

package net.drgmes.dwm.blocks.tardis.doors;

import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.network.server.TardisInteriorDoorsInitPacket;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

public abstract class BaseTardisDoorsBlockEntity extends BlockEntity {
    public String tardisId;
    private boolean isInited;

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
    public void markRemoved() {
        if (this.world instanceof ServerWorld serverWorld && TardisHelper.isTardisDimension(this.world)) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                if (!tardis.isValid()) return;

                tardis.getInteriorDoorTiles().remove(this);
                tardis.updateEntrancePortals();
                tardis.updateConsoleTiles();
            });
        }

        super.markRemoved();
    }

    public void tick() {
        if (!this.isInited) {
            this.isInited = true;
            this.init();
        }
    }

    public void init() {
        if (TardisHelper.isTardisDimension(this.world)) {
            this.tardisId = DimensionHelper.getWorldId(this.world);

            if (this.world instanceof ServerWorld serverWorld) {
                TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                    if (!tardis.isValid()) return;
                    if (tardis.getInteriorDoorTiles().contains(this)) return;

                    tardis.getInteriorDoorTiles().add(this);
                    tardis.updateEntrancePortals();
                    tardis.updateDoorsTiles();
                });
            }
            else {
                new TardisInteriorDoorsInitPacket(this.getPos()).sendToServer();
            }
        }
    }
}

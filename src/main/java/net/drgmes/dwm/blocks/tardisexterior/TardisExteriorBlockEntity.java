package net.drgmes.dwm.blocks.tardisexterior;

import java.util.UUID;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TardisExteriorBlockEntity extends BlockEntity {
    private String TARDIS_DIM_UUID;

    public TardisExteriorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_EXTERIOR.get(), blockPos, blockState);
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
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putString("tardisDimUUID", this.getTardisDimUUID());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.TARDIS_DIM_UUID = tag.getString("tardisDimUUID");
    }

    public String getTardisDimUUID() {
        if (this.TARDIS_DIM_UUID == null) {
            this.TARDIS_DIM_UUID = UUID.randomUUID().toString();
        }

        return this.TARDIS_DIM_UUID;
    }
}

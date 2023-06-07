package net.drgmes.dwm.blocks.tardis.misc.tardisarscreator;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;

public class TardisArsCreatorBlockEntity extends BlockEntity {
    public int index;
    public boolean isInitial;

    public TardisArsCreatorBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_ARS_CREATOR.getBlockEntityType(), blockPos, blockState);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        this.index = tag.getInt("index");
        this.isInitial = tag.getBoolean("isInitial");
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        tag.putInt("index", this.index);
        tag.putBoolean("isInitial", this.isInitial);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }
}

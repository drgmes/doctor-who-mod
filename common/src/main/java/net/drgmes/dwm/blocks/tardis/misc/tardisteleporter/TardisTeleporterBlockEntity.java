package net.drgmes.dwm.blocks.tardis.misc.tardisteleporter;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TardisTeleporterBlockEntity extends BlockEntity {
    public boolean isLocked;
    public BlockPos destinationBlockPos;
    public Direction destinationFacing;

    public TardisTeleporterBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_TELEPORTER.getBlockEntityType(), blockPos, blockState);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        this.isLocked = tag.getBoolean("isLocked");

        if (tag.contains("destinationBlockPos")) {
            this.destinationBlockPos = BlockPos.fromLong(tag.getLong("destinationBlockPos"));
        }

        if (tag.contains("destinationFacing")) {
            this.destinationFacing = Direction.byId(tag.getInt("destinationFacing"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        tag.putBoolean("isLocked", this.isLocked);

        if (this.destinationBlockPos != null) {
            tag.putLong("destinationBlockPos", this.destinationBlockPos.asLong());
        }

        if (this.destinationFacing != null) {
            tag.putInt("destinationFacing", this.destinationFacing.getId());
        }
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

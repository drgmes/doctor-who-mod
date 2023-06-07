package net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer;

import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsStructure;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public class TardisArsDestroyerBlockEntity extends BlockEntity {
    public ArsStructure arsStructure;
    public BlockPos tacBlockPos;
    public Direction tacFacing;
    public int tacIndex;
    public boolean tacIsInitial;

    public TardisArsDestroyerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_ARS_DESTROYER.getBlockEntityType(), blockPos, blockState);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if (tag.contains("arsStructureId")) {
            ArsCategory category = ArsCategories.CATEGORIES.get(tag.getString("arsCategoryId"));
            this.arsStructure = ArsStructures.STRUCTURES.get(category).get(tag.getString("arsStructureId"));
        }

        if (tag.contains("tacBlockPos")) {
            this.tacBlockPos = BlockPos.fromLong(tag.getLong("tacBlockPos"));
        }

        if (tag.contains("tacFacing")) {
            this.tacFacing = Direction.byId(tag.getInt("tacFacing"));
        }

        this.tacIndex = tag.getInt("tacIndex");
        this.tacIsInitial = tag.getBoolean("tacIsInitial");
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        if (this.arsStructure != null) {
            tag.putString("arsStructureId", this.arsStructure.getName());
            tag.putString("arsCategoryId", this.arsStructure.getCategory().getPath());
        }

        if (this.tacBlockPos != null) {
            tag.putLong("tacBlockPos", this.tacBlockPos.asLong());
        }

        if (this.tacFacing != null) {
            tag.putInt("tacFacing", this.tacFacing.getId());
        }

        tag.putInt("tacIndex", this.tacIndex);
        tag.putBoolean("tacIsInitial", this.tacIsInitial);
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

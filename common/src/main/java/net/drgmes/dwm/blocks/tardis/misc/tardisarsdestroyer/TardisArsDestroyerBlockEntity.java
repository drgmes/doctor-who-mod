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

import java.util.Objects;

public class TardisArsDestroyerBlockEntity extends BlockEntity {
    public ArsStructure arsStructure;

    public TardisArsDestroyerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_ARS_DESTROYER.getBlockEntityType(), blockPos, blockState);
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if (!Objects.equals(tag.getString("arsStructureId"), "")) {
            ArsCategory category = ArsCategories.CATEGORIES.get(tag.getString("arsCategoryId"));
            this.arsStructure = ArsStructures.STRUCTURES.get(category).get(tag.getString("arsStructureId"));
        }
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        if (this.arsStructure != null) {
            tag.putString("arsStructureId", this.arsStructure.getName());
            tag.putString("arsCategoryId", this.arsStructure.getCategory().getPath());
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

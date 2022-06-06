package net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer;

import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsCategory;
import net.drgmes.dwm.common.tardis.ars.ArsRoom;
import net.drgmes.dwm.common.tardis.ars.ArsRooms;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TardisRoomDestroyerBlockEntity extends BlockEntity {
    public ArsRoom room;

    public TardisRoomDestroyerBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(ModBlockEntities.TARDIS_ROOM_DESTROYER.get(), blockPos, blockState);
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

        if (this.room != null) {
            tag.putString("roomId", this.room.getName());
            tag.putString("roomCategoryId", this.room.getCategory().getPath());
        }
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        if (tag.getString("roomId") != "") {
            ArsCategory category = ArsCategories.CATEGORIES.get(tag.getString("roomCategoryId"));
            this.room = ArsRooms.ROOMS.get(category).get(tag.getString("roomId"));
        }
    }
}

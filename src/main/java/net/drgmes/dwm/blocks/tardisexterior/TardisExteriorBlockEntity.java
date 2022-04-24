package net.drgmes.dwm.blocks.tardisexterior;

import java.util.UUID;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TardisExteriorBlockEntity extends BlockEntity {
    public String tardisLevelUUID;
    public String tardisConsoleRoom = "test";
    public int materializedPercent = 100;

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
        tag.putString("tardisConsoleRoom", this.tardisConsoleRoom);
        tag.putInt("materializedPercent", this.materializedPercent);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.tardisLevelUUID = tag.getString("tardisDimUUID");
        this.tardisConsoleRoom = tag.getString("tardisConsoleRoom");
        this.materializedPercent = tag.getInt("materializedPercent");
    }

    public String getTardisDimUUID() {
        if (this.tardisLevelUUID == null) this.tardisLevelUUID = UUID.randomUUID().toString();
        return this.tardisLevelUUID;
    }

    public ServerLevel getTardisDimension(Level level) {
        return TardisHelper.setupTardis(this, level);
    }
}

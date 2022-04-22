package net.drgmes.dwm.blocks.tardisexterior;

import java.util.UUID;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModCapabilities;
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
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.tardisLevelUUID = tag.getString("tardisDimUUID");
        this.tardisConsoleRoom = tag.getString("tardisConsoleRoom");
    }

    @Override
    public void onLoad() {
        super.onLoad();

        this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            if (!provider.isValid()) return;

            BlockState blockState = this.level.getBlockState(this.worldPosition);
            level.setBlock(this.worldPosition, blockState.setValue(TardisExteriorBlock.OPEN, provider.isDoorsOpened()), 10);
        });
    }

    public String getTardisDimUUID() {
        if (this.tardisLevelUUID == null) this.tardisLevelUUID = UUID.randomUUID().toString();
        return this.tardisLevelUUID;
    }

    public ServerLevel getTardisDimension(Level level, BlockPos blockPos) {
        return TardisHelper.setupTardis(this, level, blockPos);
    }
}

package net.drgmes.dwm.blocks.tardisexterior;

import java.util.UUID;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class TardisExteriorBlockEntity extends BlockEntity {
    public String tardisLevelUUID;
    public String tardisConsoleRoom = "toyota_natured";

    private int tickInProgress = 0;
    private boolean isMaterialized = false;
    private boolean inRematProgress = false;
    private boolean inDematProgress = false;

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

        tag.putInt("tickInProgress", this.tickInProgress);
        tag.putBoolean("isMaterialized", this.isMaterialized);
        tag.putBoolean("inRematProgress", this.inRematProgress);
        tag.putBoolean("inDematProgress", this.inDematProgress);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.tardisLevelUUID = tag.getString("tardisDimUUID");
        this.tardisConsoleRoom = tag.getString("tardisConsoleRoom");

        this.tickInProgress = tag.getInt("tickInProgress");
        this.isMaterialized = tag.getBoolean("isMaterialized");
        this.inRematProgress = tag.getBoolean("inRematProgress");
        this.inDematProgress = tag.getBoolean("inDematProgress");
    }

    public void tick() {
        float goal = this.inRematProgress ? DWM.TIMINGS.REMAT : (this.inDematProgress ? DWM.TIMINGS.DEMAT : 0);

        if (goal > 0) {
            if (this.inRematProgress) this.inDematProgress = false;
            if (this.inDematProgress) this.inRematProgress = false;

            if (this.tickInProgress < goal) {
                this.tickInProgress++;
            }
            else {
                this.isMaterialized = this.inRematProgress;
                this.inRematProgress = false;
                this.inDematProgress = false;
                this.tickInProgress = 0;
            }
        }
    }

    public void remat() {
        this.inRematProgress = true;
        this.level.playSound(null, this.worldPosition, ModSounds.TARDIS_LAND.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void demat() {
        this.inDematProgress = true;
        this.level.playSound(null, this.worldPosition, ModSounds.TARDIS_TAKEOFF.get(), SoundSource.BLOCKS, 1.0F, 1.0F);
    }

    public void resetMaterializationState(boolean flag) {
        this.isMaterialized = flag;
        this.inRematProgress = false;
        this.inDematProgress = false;
        this.tickInProgress = 0;
    }

    public int getMaterializedPercent() {
        if (this.inDematProgress) return (int) Math.ceil((DWM.TIMINGS.DEMAT - this.tickInProgress) / DWM.TIMINGS.DEMAT * 100);
        if (this.inRematProgress) return (int) Math.ceil(this.tickInProgress / DWM.TIMINGS.REMAT * 100);
        return this.isMaterialized ? 100 : 0;
    }

    public String getTardisDimUUID() {
        if (this.tardisLevelUUID == null) this.tardisLevelUUID = UUID.randomUUID().toString();
        return this.tardisLevelUUID;
    }

    public ServerLevel getTardisDimension(Level level) {
        return TardisHelper.setupTardis(this, level);
    }
}

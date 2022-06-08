package net.drgmes.dwm.blocks.tardis.exteriors;

import java.util.List;
import java.util.UUID;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.boti.IBoti;
import net.drgmes.dwm.common.tardis.boti.storage.BotiStorage;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public abstract class BaseTardisExteriorBlockEntity extends BlockEntity implements IBoti {
    public String tardisConsoleRoom = "toyota_natured";
    public String tardisLevelUUID;
    public boolean shieldsEnabled;

    private BotiStorage botiStorage = new BotiStorage();

    private int tickInProgress = 0;
    private boolean isMaterialized = false;
    private boolean inRematProgress = false;
    private boolean inDematProgress = false;

    public BaseTardisExteriorBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

	@Override
	public AABB getRenderBoundingBox() {
		return new AABB(this.worldPosition).inflate(3, 4, 3);
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

        tag.putString("tardisDimUUID", this.getTardisLevelUUID());
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

    @Override
    public void setBotiStorage(BotiStorage botiStorage) {
        this.botiStorage = botiStorage;
    }

    @Override
    public BotiStorage getBotiStorage() {
        return this.botiStorage;
    }

    @Override
    public void updateBoti() {
        if (this.level.isClientSide) return;
        if (!this.isMaterialized) return;

        ServerLevel tardisLevel = this.getTardisLevel(this.level);
        if (tardisLevel == null) return;

        tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            this.botiStorage.setDirection(tardis.getEntraceFacing());
            this.botiStorage.setRadius(ModConfig.CLIENT.botiInteriorRadius.get());
            this.botiStorage.setDistance(ModConfig.CLIENT.botiInteriorDistance.get());
            this.botiStorage.updateBoti(tardisLevel, tardis.getEntracePosition());

            ModPackets.send(this.level.getChunkAt(this.worldPosition), this.getBotiUpdatePacket(this.worldPosition));
        });
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

        if (this.level.getGameTime() % 40 == 0) this.updateBoti();

        if (!this.level.isClientSide) {
            if (this.shieldsEnabled) {
                double radius = 6.0D;
                Vec3 blockPosVec = Vec3.atBottomCenterOf(this.getBlockPos());
                List<Entity> entities = this.level.getEntitiesOfClass(Entity.class, AABB.ofSize(blockPosVec, radius, radius, radius));

                for (Entity entity : entities) {
                    double distance = blockPosVec.distanceTo(entity.position());
                    Vec3 deltaMovement = entity.position().subtract(blockPosVec).scale(radius / distance / 10);
                    entity.setDeltaMovement(deltaMovement);
                }
            }
        }
    }

    public void unloadAll() {
        this.level.getCapability(ModCapabilities.TARDIS_CHUNK_LOADER).ifPresent((levelProvider) -> {
            SectionPos pos = SectionPos.of(this.worldPosition);
            int radius = DWM.CHUNKS_UPDATE_RADIUS;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    levelProvider.remove(pos.offset(x, 0, z), this.worldPosition);
                }
            }
        });
    }

    public void loadAll() {
        this.level.getCapability(ModCapabilities.TARDIS_CHUNK_LOADER).ifPresent((levelProvider) -> {
            SectionPos pos = SectionPos.of(this.worldPosition);
            int radius = DWM.CHUNKS_UPDATE_RADIUS;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    levelProvider.add(pos.offset(x, 0, z), this.worldPosition);
                }
            }
        });
    }

    public void remat() {
        this.inRematProgress = true;
        ModSounds.playTardisLandingSound(this.level, this.worldPosition);
    }

    public void demat() {
        this.inDematProgress = true;
        ModSounds.playTardisTakeoffSound(this.level, this.worldPosition);
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

    public String getTardisLevelUUID() {
        if (this.tardisLevelUUID == null) this.tardisLevelUUID = UUID.randomUUID().toString();
        return this.tardisLevelUUID;
    }

    public ServerLevel getTardisLevel(Level level) {
        return TardisHelper.getOrCreateTardisLevel(this, level);
    }
}

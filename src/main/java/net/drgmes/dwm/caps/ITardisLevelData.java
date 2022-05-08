package net.drgmes.dwm.caps;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.tardisdoorspolicebox.TardisDoorsPoliceBoxBlockEntity;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

public interface ITardisLevelData extends INBTSerializable<CompoundTag> {
    void addSystem(ITardisSystem system);
    Map<Class<? extends ITardisSystem>, ITardisSystem> getSystems();
    ITardisSystem getSystem(Class<? extends ITardisSystem> system);

    boolean isValid();
    boolean isDoorsLocked();
    boolean isDoorsOpened();
    boolean isLightEnabled();
    boolean isShieldsEnabled();
    boolean isEnergyArtronHarvesting();
    boolean isEnergyForgeHarvesting();

    ServerLevel getLevel();
    UUID getOwnerUUID();

    ResourceKey<Level> getPreviousExteriorDimension();
    ResourceKey<Level> getCurrentExteriorDimension();
    ResourceKey<Level> getDestinationExteriorDimension();

    Direction getEntraceFacing();
    Direction getPreviousExteriorFacing();
    Direction getCurrentExteriorFacing();
    Direction getDestinationExteriorFacing();

    BlockPos getEntracePosition();
    BlockPos getPreviousExteriorPosition();
    BlockPos getCurrentExteriorPosition();
    BlockPos getCurrentExteriorRelativePosition();
    BlockPos getDestinationExteriorPosition();

    int getXYZStep();
    int getEnergyArtron();
    int getEnergyForge();
    TardisDoorsPoliceBoxBlockEntity getMainInteriorDoorTile();
    List<TardisDoorsPoliceBoxBlockEntity> getInteriorDoorTiles();
    BaseTardisConsoleBlockEntity getMainConsoleTile();
    List<BaseTardisConsoleBlockEntity> getConsoleTiles();

    boolean setOwnerUUID(UUID uuid);

    boolean setDimension(ResourceKey<Level> dimension, boolean shouldUpdatePrev);
    boolean setDestinationDimension(ResourceKey<Level> dimension);

    boolean setEntraceFacing(Direction direction);
    boolean setFacing(Direction direction, boolean shouldUpdatePrev);
    boolean setDestinationFacing(Direction direction);

    boolean setEntracePosition(BlockPos blockPos);
    boolean setPosition(BlockPos blockPos, boolean shouldUpdatePrev);
    boolean setDestinationPosition(BlockPos blockPos);

    boolean setDoorsLockState(boolean flag, Player player);
    boolean setDoorsOpenState(boolean flag);
    boolean setLightState(boolean flag);
    boolean setShieldsState(boolean flag);
    boolean setEnergyArtronHarvesting(boolean flag);
    boolean setEnergyForgeHarvesting(boolean flag);

    void updateConsoleTiles();
    void applyDataToControlsStorage(TardisConsoleControlsStorage controlsStorage);
    void applyControlsStorageToData(TardisConsoleControlsStorage controlsStorage);
    void tick();

    public static class TardisLevelDataProvider implements ICapabilitySerializable<CompoundTag> {
        public final LazyOptional<ITardisLevelData> holder;
        private ITardisLevelData data;

        public TardisLevelDataProvider(Level level) {
            this.data = new TardisLevelDataCapability(level);
            this.holder = LazyOptional.of(() -> this.data);
        }

        @Override
        public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
            return cap == ModCapabilities.TARDIS_DATA ? this.holder.cast() : LazyOptional.empty();
        }

        @Override
        public CompoundTag serializeNBT() {
            return this.data.serializeNBT();
        }

        @Override
        public void deserializeNBT(CompoundTag nbt) {
            this.data.deserializeNBT(nbt);
        }

        public Tag writeNBT(Capability<ITardisLevelData> capability, ITardisLevelData instance, Direction side) {
            return instance.serializeNBT();
        }

        public void readNBT(Capability<ITardisLevelData> capability, ITardisLevelData instance, Direction side, Tag nbt) {
            instance.deserializeNBT((CompoundTag) nbt);
        }
    }
}

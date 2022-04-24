package net.drgmes.dwm.caps;

import java.util.List;
import java.util.Map;

import net.drgmes.dwm.blocks.tardisdoor.TardisDoorBlockEntity;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.utils.base.blockentities.BaseTardisConsoleBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
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
    boolean isDoorsOpened();
    boolean isShieldsEnabled();
    boolean isEnergyArtronHarvesting();
    boolean isEnergyForgeHarvesting();

    ServerLevel getLevel();

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
    TardisDoorBlockEntity getMainDoorTile();
    List<TardisDoorBlockEntity> getDoorTiles();
    BaseTardisConsoleBlockEntity getMainConsoleTile();
    List<BaseTardisConsoleBlockEntity> getConsoleTiles();

    void setDimension(ResourceKey<Level> dimension, boolean shouldUpdatePrev);
    void setDestinationDimension(ResourceKey<Level> dimension);

    void setEntraceFacing(Direction direction);
    void setFacing(Direction direction, boolean shouldUpdatePrev);
    void setDestinationFacing(Direction direction);

    void setEntracePosition(BlockPos blockPos);
    void setPosition(BlockPos blockPos, boolean shouldUpdatePrev);
    void setDestinationPosition(BlockPos blockPos);

    void setDoorsState(boolean flag, boolean shouldUpdate);
    void setShieldsState(boolean flag, boolean shouldUpdate);
    void setEnergyArtronHarvesting(boolean flag);
    void setEnergyForgeHarvesting(boolean flag);

    void updateDoorTiles();
    void updateConsoleTiles();

    void applyControlsStorage(TardisConsoleControlsStorage controlsStorage);
    void tick();

    public static class TardisLevelProvider implements ICapabilitySerializable<CompoundTag> {
        final private LazyOptional<ITardisLevelData> holder;
        private ITardisLevelData data;

        public TardisLevelProvider(Level level) {
            this.data = new TardisLevelCapability(level);
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

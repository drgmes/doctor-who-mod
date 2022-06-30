package net.drgmes.dwm.caps;

import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.common.tardis.boti.IBoti;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.setup.ModCapabilities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;

import java.util.List;
import java.util.UUID;

public interface ITardisLevelData extends INBTSerializable<CompoundTag>, IBoti {
    public static final int SYSTEM_COMPONENTS_CONTAINER_SIZE = 14;
    public static final int BATTERY_COMPONENTS_CONTAINER_SIZE = 0;
    public static final int UPGRADE_COMPONENTS_CONTAINER_SIZE = 0;

    public <T extends ITardisSystem> T getSystem(Class<T> system);
    boolean isSystemEnabled(Class<? extends ITardisSystem> system);

    void setSystemComponents(NonNullList<ItemStack> systemComponents);
    NonNullList<ItemStack> getSystemComponents();

    ServerLevel getLevel();
    UUID getOwnerUUID();

    boolean isValid();
    boolean isDoorsLocked();
    boolean isDoorsOpened();
    boolean isLightEnabled();
    boolean isShieldsEnabled();
    boolean isEnergyArtronHarvesting();
    boolean isEnergyForgeHarvesting();

    ResourceKey<Level> getPreviousExteriorDimension();
    ResourceKey<Level> getCurrentExteriorDimension();
    ResourceKey<Level> getDestinationExteriorDimension();

    Direction getPreviousExteriorFacing();
    Direction getCurrentExteriorFacing();
    Direction getDestinationExteriorFacing();

    Direction getEntranceFacing();
    BlockPos getEntrancePosition();
    BlockPos getCorePosition();

    BlockPos getPreviousExteriorPosition();
    BlockPos getCurrentExteriorPosition();
    BlockPos getCurrentExteriorRelativePosition();
    BlockPos getDestinationExteriorPosition();

    int getXYZStep();
    int getEnergyArtron();
    int getEnergyForge();

    List<BaseTardisDoorsBlockEntity> getInteriorDoorTiles();
    BaseTardisDoorsBlockEntity getMainInteriorDoorTile();

    List<BaseTardisConsoleBlockEntity> getConsoleTiles();
    BaseTardisConsoleBlockEntity getMainConsoleTile();

    boolean setOwnerUUID(UUID uuid);

    boolean setDimension(ResourceKey<Level> dimension, boolean shouldUpdatePrev);
    boolean setDestinationDimension(ResourceKey<Level> dimension);

    boolean setPosition(BlockPos blockPos, boolean shouldUpdatePrev);
    boolean setDestinationPosition(BlockPos blockPos);

    boolean setFacing(Direction direction, boolean shouldUpdatePrev);
    boolean setDestinationFacing(Direction direction);

    boolean setDoorsOpenState(boolean flag);
    boolean setDoorsLockState(boolean flag, Player player);

    boolean setLightState(boolean flag);
    boolean setShieldsState(boolean flag);
    boolean setEnergyArtronHarvesting(boolean flag);
    boolean setEnergyForgeHarvesting(boolean flag);

    void updateDoorsTiles();
    void updateConsoleTiles();

    void applyDataToControlsStorage(TardisConsoleControlsStorage controlsStorage);
    void applyControlsStorageToData(TardisConsoleControlsStorage controlsStorage);

    void tick();

    class TardisLevelDataProvider implements ICapabilitySerializable<CompoundTag> {
        public final LazyOptional<ITardisLevelData> holder;
        private final ITardisLevelData data;

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

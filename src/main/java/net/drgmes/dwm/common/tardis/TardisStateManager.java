package net.drgmes.dwm.common.tardis;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlock;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItem;
import net.drgmes.dwm.network.TardisConsoleUnitRemoteCallablePackets;
import net.drgmes.dwm.network.TardisExteriorRemoteCallablePackets;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.types.IMixinPortal;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.q_misc_util.MiscHelper;
import team.reborn.energy.api.base.SimpleEnergyStorage;

import java.util.*;

public class TardisStateManager extends PersistentState {
    public static final int SYSTEM_COMPONENTS_CONTAINER_SIZE = 14;
    public static final int BATTERY_COMPONENTS_CONTAINER_SIZE = 0;
    public static final int UPGRADE_COMPONENTS_CONTAINER_SIZE = 0;

    private final Map<Class<? extends ITardisSystem>, ITardisSystem> systems = new HashMap<>();
    private final List<BaseTardisDoorsBlockEntity> doorTiles = new ArrayList<>();
    private final List<BaseTardisConsoleUnitBlockEntity> consoleTiles = new ArrayList<>();

    private DefaultedList<ItemStack> systemComponents = DefaultedList.ofSize(SYSTEM_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);
    private DefaultedList<ItemStack> batteryComponents = DefaultedList.ofSize(BATTERY_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);
    private DefaultedList<ItemStack> upgradeComponents = DefaultedList.ofSize(UPGRADE_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);

    private ServerWorld world;
    private UUID owner;
    private SimpleEnergyStorage fuelStorage;
    private SimpleEnergyStorage energyStorage;
    private TardisConsoleRoomEntry consoleRoom;

    private List<Map.Entry<Portal, Portal>> portalsToRooms = new ArrayList<>();
    private Portal portalFromTardis;
    private Portal portalToTardis;

    private RegistryKey<World> prevExteriorDimension;
    private RegistryKey<World> currExteriorDimension;
    private RegistryKey<World> destExteriorDimension;

    private BlockPos prevExteriorPosition;
    private BlockPos currExteriorPosition;
    private BlockPos destExteriorPosition;

    private Direction prevExteriorFacing;
    private Direction currExteriorFacing;
    private Direction destExteriorFacing;

    private boolean broken = false;
    private boolean doorsLocked = false;
    private boolean doorsOpened = false;
    private boolean lightEnabled = false;
    private boolean shieldsEnabled = false;
    private boolean fuelHarvesting = false;
    private boolean energyHarvesting = false;
    private int xyzStep = 1;

    public TardisStateManager(ServerWorld world, boolean mustBeBroken) {
        this.broken = mustBeBroken;
        this.doorsLocked = mustBeBroken;

        this.setWorld(world);
        this.setFuelStorage(1024, 16, 16);
        this.setEnergyStorage(1024, 16, 16);
        this.addSystem(new TardisSystemMaterialization(this));
        this.addSystem(new TardisSystemFlight(this));
        this.addSystem(new TardisSystemShields(this));
    }

    public static Optional<TardisStateManager> get(ServerWorld world, boolean mustBeBroken) {
        if (world == null) return Optional.empty();

        TardisStateManager tardisStateManager = world.getPersistentStateManager().getOrCreate(
            (tag) -> TardisStateManager.createFromNbt(world, tag),
            () -> new TardisStateManager(world, mustBeBroken),
            DWM.LOCS.TARDIS.getPath()
        );

        if (tardisStateManager != null) tardisStateManager.setWorld(world);
        return Optional.ofNullable(tardisStateManager);
    }

    public static Optional<TardisStateManager> get(ServerWorld world) {
        return get(world, true);
    }

    public static TardisStateManager createFromNbt(ServerWorld world, NbtCompound tag) {
        TardisStateManager tardisStateManager = new TardisStateManager(world, false);
        tardisStateManager.readNbt(tag);
        return tardisStateManager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        NbtCompound tdTagSystemComponents = new NbtCompound();
        Inventories.writeNbt(tdTagSystemComponents, this.systemComponents);
        tag.put("tdTagSystemComponents", tdTagSystemComponents);

        NbtCompound tdTagBatteryComponents = new NbtCompound();
        Inventories.writeNbt(tdTagBatteryComponents, this.batteryComponents);
        tag.put("tdTagBatteryComponents", tdTagBatteryComponents);

        NbtCompound tdTagUpgradeComponents = new NbtCompound();
        Inventories.writeNbt(tdTagUpgradeComponents, this.upgradeComponents);
        tag.put("tdTagUpgradeComponents", tdTagUpgradeComponents);

        if (this.owner != null) tag.putUuid("owner", this.owner);

        if (this.prevExteriorDimension != null) tag.putString("prevExteriorDimension", this.prevExteriorDimension.getValue().toString());
        if (this.currExteriorDimension != null) tag.putString("currExteriorDimension", this.currExteriorDimension.getValue().toString());
        if (this.destExteriorDimension != null) tag.putString("destExteriorDimension", this.destExteriorDimension.getValue().toString());

        if (this.prevExteriorFacing != null) tag.putString("prevExteriorFacing", this.prevExteriorFacing.getName());
        if (this.currExteriorFacing != null) tag.putString("currExteriorFacing", this.currExteriorFacing.getName());
        if (this.destExteriorFacing != null) tag.putString("destExteriorFacing", this.destExteriorFacing.getName());

        if (this.prevExteriorPosition != null) tag.putLong("prevExteriorPosition", this.prevExteriorPosition.asLong());
        if (this.currExteriorPosition != null) tag.putLong("currExteriorPosition", this.currExteriorPosition.asLong());
        if (this.destExteriorPosition != null) tag.putLong("destExteriorPosition", this.destExteriorPosition.asLong());

        tag.putString("consoleRoom", this.getConsoleRoom().name);

        tag.putInt("xyzStep", this.xyzStep);
        tag.putLong("fuelAmount", this.fuelStorage.amount);
        tag.putLong("energyAmount", this.energyStorage.amount);

        tag.putBoolean("broken", this.broken);
        tag.putBoolean("doorsLocked", this.doorsLocked);
        tag.putBoolean("doorsOpened", this.doorsOpened);
        tag.putBoolean("lightEnabled", this.lightEnabled);
        tag.putBoolean("shieldsEnabled", this.shieldsEnabled);
        tag.putBoolean("fuelHarvesting", this.fuelHarvesting);
        tag.putBoolean("energyHarvesting", this.energyHarvesting);

        this.systems.values().forEach((system) -> {
            tag.put(system.getClass().getName(), system.save());
        });

        return tag;
    }

    public void readNbt(NbtCompound tag) {
        this.systemComponents = DefaultedList.ofSize(this.systemComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagSystemComponents"), this.systemComponents);

        this.batteryComponents = DefaultedList.ofSize(this.batteryComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagBatteryComponents"), this.batteryComponents);

        this.upgradeComponents = DefaultedList.ofSize(this.upgradeComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagUpgradeComponents"), this.upgradeComponents);

        if (tag.contains("consoleRoom")) this.consoleRoom = TardisConsoleRooms.getConsoleRoom(tag.getString("consoleRoom"), this.broken);
        if (tag.contains("owner")) this.owner = tag.getUuid("owner");

        if (tag.contains("prevExteriorDimension")) this.prevExteriorDimension = DimensionHelper.getWorldKey(tag.getString("prevExteriorDimension"));
        if (tag.contains("currExteriorDimension")) this.currExteriorDimension = DimensionHelper.getWorldKey(tag.getString("currExteriorDimension"));
        if (tag.contains("destExteriorDimension")) this.destExteriorDimension = DimensionHelper.getWorldKey(tag.getString("destExteriorDimension"));

        if (tag.contains("prevExteriorFacing")) this.prevExteriorFacing = getDirectionByKey(tag, "prevExteriorFacing");
        if (tag.contains("currExteriorFacing")) this.currExteriorFacing = getDirectionByKey(tag, "currExteriorFacing");
        if (tag.contains("destExteriorFacing")) this.destExteriorFacing = getDirectionByKey(tag, "destExteriorFacing");

        if (tag.contains("prevExteriorPosition")) this.prevExteriorPosition = BlockPos.fromLong(tag.getLong("prevExteriorPosition"));
        if (tag.contains("currExteriorPosition")) this.currExteriorPosition = BlockPos.fromLong(tag.getLong("currExteriorPosition"));
        if (tag.contains("destExteriorPosition")) this.destExteriorPosition = BlockPos.fromLong(tag.getLong("destExteriorPosition"));

        this.setFuelStorage(64, 16, 16);
        this.setEnergyStorage(64, 16, 16);

        this.xyzStep = tag.getInt("xyzStep");
        this.fuelStorage.amount = Math.min(tag.getLong("fuelAmount"), this.fuelStorage.getCapacity());
        this.energyStorage.amount = Math.min(tag.getLong("energyAmount"), this.fuelStorage.getCapacity());

        this.broken = tag.getBoolean("broken");
        this.doorsLocked = tag.getBoolean("doorsLocked");
        this.doorsOpened = tag.getBoolean("doorsOpened");
        this.lightEnabled = tag.getBoolean("lightEnabled");
        this.shieldsEnabled = tag.getBoolean("shieldsEnabled");
        this.fuelHarvesting = tag.getBoolean("fuelHarvesting");
        this.energyHarvesting = tag.getBoolean("energyHarvesting");

        this.systems.values().forEach((system) -> {
            if (tag.contains(system.getClass().getName())) {
                system.load(tag.getCompound(system.getClass().getName()));
            }
        });
    }

    public boolean isValid() {
        return this.currExteriorDimension != null && this.currExteriorPosition != null && this.currExteriorFacing != null;
    }

    // /////////////////////////// //
    // Tardis Primary Data methods //
    // /////////////////////////// //

    public ServerWorld getWorld() {
        return this.world;
    }

    public void setWorld(ServerWorld world) {
        this.world = world;
    }

    public TardisConsoleRoomEntry getConsoleRoom() {
        if (this.consoleRoom == null) this.setConsoleRoom(this.isBroken() ? TardisConsoleRooms.DEFAULT_ABANDONED : TardisConsoleRooms.DEFAULT);
        return this.consoleRoom;
    }

    public void setConsoleRoom(TardisConsoleRoomEntry consoleRoom) {
        this.consoleRoom = consoleRoom;
        this.markDirty();
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        this.markDirty();
    }

    // //////////////////////// //
    // Tardis Dimension methods //
    // //////////////////////// //

    public RegistryKey<World> getPreviousExteriorDimension() {
        return this.prevExteriorDimension != null ? this.prevExteriorDimension : this.getCurrentExteriorDimension();
    }

    public RegistryKey<World> getCurrentExteriorDimension() {
        return this.currExteriorDimension;
    }

    public RegistryKey<World> getDestinationExteriorDimension() {
        return this.destExteriorDimension != null ? this.destExteriorDimension : this.getCurrentExteriorDimension();
    }

    public void setDimension(RegistryKey<World> dimension, boolean shouldUpdatePrev) {
        if (shouldUpdatePrev) this.prevExteriorDimension = this.currExteriorDimension;
        this.currExteriorDimension = dimension;
        this.markDirty();
    }

    public void setDestinationDimension(RegistryKey<World> dimension) {
        this.destExteriorDimension = dimension;
        this.markDirty();
    }

    // /////////////////////// //
    // Tardis Position methods //
    // /////////////////////// //

    public BlockPos getPreviousExteriorPosition() {
        return this.prevExteriorPosition != null ? this.prevExteriorPosition.toImmutable() : this.getCurrentExteriorPosition();
    }

    public BlockPos getCurrentExteriorPosition() {
        return this.currExteriorPosition.toImmutable();
    }

    public BlockPos getCurrentExteriorRelativePosition() {
        return this.currExteriorPosition.offset(this.currExteriorFacing).toImmutable();
    }

    public BlockPos getDestinationExteriorPosition() {
        return this.destExteriorPosition != null ? this.destExteriorPosition.toImmutable() : this.getCurrentExteriorPosition();
    }

    public void setPosition(BlockPos blockPos, boolean shouldUpdatePrev) {
        if (shouldUpdatePrev) this.prevExteriorPosition = this.currExteriorPosition;
        this.currExteriorPosition = blockPos.toImmutable();
        this.markDirty();
    }

    public void setDestinationPosition(BlockPos blockPos) {
        this.destExteriorPosition = blockPos.toImmutable();
        this.markDirty();
    }

    // ///////////////////// //
    // Tardis Facing methods //
    // ///////////////////// //

    public Direction getPreviousExteriorFacing() {
        return this.prevExteriorFacing != null ? this.prevExteriorFacing : this.getCurrentExteriorFacing();
    }

    public Direction getCurrentExteriorFacing() {
        return this.currExteriorFacing;
    }

    public Direction getDestinationExteriorFacing() {
        return this.destExteriorFacing != null ? this.destExteriorFacing : this.getCurrentExteriorFacing();
    }

    public void setFacing(Direction direction, boolean shouldUpdatePrev) {
        if (shouldUpdatePrev) this.prevExteriorFacing = this.currExteriorFacing;
        this.currExteriorFacing = direction;
        this.markDirty();
    }

    public void setDestinationFacing(Direction direction) {
        this.destExteriorFacing = direction;
        this.markDirty();
    }

    // ///////////////////////// //
    // Tardis State Data methods //
    // ///////////////////////// //

    public boolean isBroken() {
        return this.broken;
    }

    public boolean setBrokenState(boolean flag) {
        if (this.broken == flag) return false;
        this.broken = flag;

        if (flag) ModSounds.playTardisRepairSound(this.world, this.getEntrancePosition());
        this.markDirty();
        return true;
    }

    public boolean isDoorsLocked() {
        return this.doorsLocked;
    }

    public boolean setDoorsLockState(boolean flag, PlayerEntity player) {
        if (player != null && (this.getOwner() == null || (this.getOwner() != null && !player.getUuid().equals(this.getOwner())))) return false;
        if (this.doorsLocked == flag) return false;
        this.doorsLocked = flag;

        if (flag) ModSounds.playTardisDoorsLockSound(this.world, this.getEntrancePosition());
        else ModSounds.playTardisDoorsUnlockSound(this.world, this.getEntrancePosition());

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.getCurrentExteriorDimension());
        if (flag) ModSounds.playTardisDoorsLockSound(exteriorWorld, this.getCurrentExteriorPosition());
        else ModSounds.playTardisDoorsUnlockSound(exteriorWorld, this.getCurrentExteriorPosition());

        if (flag) this.setDoorsOpenState(false);
        this.markDirty();
        return true;
    }

    public boolean isDoorsOpened() {
        return this.doorsOpened;
    }

    public boolean setDoorsOpenState(boolean flag) {
        if (flag && this.isDoorsLocked()) return false;
        if (flag && !this.getSystem(TardisSystemMaterialization.class).isMaterialized()) return false;

        if (this.doorsOpened == flag) return false;
        this.doorsOpened = flag;

        BlockPos entrancePosition = this.getEntrancePosition();
        BlockState entranceBlockState = this.world.getBlockState(entrancePosition);
        BaseTardisDoorsBlock<?> tardisDoorsBlock = (BaseTardisDoorsBlock<?>) entranceBlockState.getBlock();
        if (flag) ModSounds.playTardisDoorsOpenSound(this.world, entrancePosition, tardisDoorsBlock.isWooden());
        else ModSounds.playTardisDoorsCloseSound(this.world, entrancePosition, tardisDoorsBlock.isWooden());

        this.updateEntrancePortals();
        this.updateDoorsTiles();
        this.updateExterior();
        this.markDirty();
        return true;
    }

    public boolean isLightEnabled() {
        return this.lightEnabled;
    }

    public boolean setLightState(boolean flag) {
        if (this.lightEnabled == flag) return false;
        this.lightEnabled = flag;

        if (flag) ModSounds.playTardisLightOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisLightOffSound(this.world, this.getMainConsolePosition());

        this.updateExterior();
        this.markDirty();
        return true;
    }

    public boolean isShieldsEnabled() {
        return this.shieldsEnabled;
    }

    public boolean setShieldsState(boolean flag) {
        if (this.shieldsEnabled == flag) return false;
        this.shieldsEnabled = flag;

        if (flag) ModSounds.playTardisShieldsOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisShieldsOffSound(this.world, this.getMainConsolePosition());

        this.updateExterior();
        this.markDirty();
        return true;
    }

    public int getXYZStep() {
        return this.xyzStep;
    }

    public void setXYZStep(int value) {
        this.xyzStep = value;
        this.markDirty();
    }

    // //////////////////////////// //
    // Tardis Energy & Fuel methods //
    // //////////////////////////// //

    public boolean isFuelHarvesting() {
        return this.fuelHarvesting;
    }

    public void setFuelHarvesting(boolean flag) {
        this.fuelHarvesting = flag;
        this.markDirty();
    }

    public SimpleEnergyStorage getFuelStorage() {
        return this.fuelStorage;
    }

    public void setFuelStorage(long capacity, long maxInsert, long maxExtract) {
        this.fuelStorage = new SimpleEnergyStorage(capacity, maxInsert, maxExtract) {
            @Override
            protected void onFinalCommit() {
                TardisStateManager.this.markDirty();
            }
        };
    }

    public boolean isEnergyHarvesting() {
        return this.energyHarvesting;
    }

    public void setEnergyHarvesting(boolean flag) {
        this.energyHarvesting = flag;
        this.markDirty();
    }

    public SimpleEnergyStorage getEnergyStorage() {
        return this.energyStorage;
    }

    public void setEnergyStorage(long capacity, long maxInsert, long maxExtract) {
        this.energyStorage = new SimpleEnergyStorage(capacity, maxInsert, maxExtract) {
            @Override
            protected void onFinalCommit() {
                TardisStateManager.this.markDirty();
            }
        };
    }

    // ////////////////////// //
    // Tardis Systems methods //
    // ////////////////////// //

    @SuppressWarnings("unchecked")
    public <T extends ITardisSystem> T getSystem(Class<T> system) {
        return (T) this.systems.getOrDefault(system, null);
    }

    public boolean isSystemEnabled(Class<? extends ITardisSystem> system) {
        for (ItemStack itemStack : this.systemComponents) {
            if (itemStack.getItem() instanceof TardisSystemItem tardisSystemItem) {
                if (tardisSystemItem.getSystemType() == system) return true;
            }
        }

        return false;
    }

    public DefaultedList<ItemStack> getSystemComponents() {
        return this.systemComponents;
    }

    public void setSystemComponents(DefaultedList<ItemStack> systemComponents) {
        this.systemComponents = systemComponents;
        this.markDirty();
    }

    // ///////////////////////// //
    // Tardis Door Tiles methods //
    // ///////////////////////// //

    public List<BaseTardisDoorsBlockEntity> getInteriorDoorTiles() {
        return this.doorTiles;
    }

    public BaseTardisDoorsBlockEntity getMainInteriorDoorTile() {
        int size = this.doorTiles.size();
        return size > 0 ? this.doorTiles.get(size - 1) : null;
    }

    public BlockPos getEntrancePosition() {
        BaseTardisDoorsBlockEntity tardisDoorsBlockEntity = this.getMainInteriorDoorTile();
        return tardisDoorsBlockEntity != null ? tardisDoorsBlockEntity.getPos() : this.getConsoleRoom().getEntrancePosition();
    }

    public Direction getEntranceFacing() {
        BaseTardisDoorsBlockEntity tardisDoorsBlockEntity = this.getMainInteriorDoorTile();
        return tardisDoorsBlockEntity != null ? tardisDoorsBlockEntity.getCachedState().get(BaseTardisDoorsBlock.FACING) : Direction.SOUTH;
    }

    public void updateDoorsTiles() {
        this.doorTiles.forEach((tile) -> {
            this.world.setBlockState(tile.getPos(), tile.getCachedState().with(BaseTardisDoorsBlock.OPEN, this.isDoorsOpened()), 3);
        });
    }

    public void updateEntrancePortals() {
        this.clearEntrancePortals();
        if (!this.isDoorsOpened()) return;

        String worldId = DimensionHelper.getWorldId(world);
        Map.Entry<Portal, Portal> portals = TardisHelper.createTardisPortals(
            this.world,
            this.getEntranceFacing(),
            this.getCurrentExteriorFacing(),
            this.getEntrancePosition().up(),
            this.getCurrentExteriorRelativePosition().up(),
            this.getCurrentExteriorDimension(),
            -0.5 + 0.0275, -0.5, 0,
            1, 2
        );

        this.portalFromTardis = portals.getKey();
        this.portalToTardis = portals.getValue();

        ((IMixinPortal) this.portalFromTardis).markAsTardisEntrance().setTardisId(worldId);
        ((IMixinPortal) this.portalToTardis).markAsTardisEntrance().setTardisId(worldId);
        if (this.portalFromTardis.world != null) this.portalFromTardis.world.spawnEntity(this.portalFromTardis);
        if (this.portalToTardis.world != null) this.portalToTardis.world.spawnEntity(this.portalToTardis);
    }

    public void updateRoomsEntrancesPortals() {
        if (this.isBroken()) return;

        int index = 0;
        String worldId = DimensionHelper.getWorldId(this.getWorld());
        StructurePlacementData placeSettings = new StructurePlacementData();
        List<StructureTemplate.StructureBlockInfo> tacBlockInfos = this.getConsoleRoom().getTemplate(this.getWorld()).getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock());

        this.clearRoomsEntrancesPortals();

        for (StructureTemplate.StructureBlockInfo tacBlockInfo : tacBlockInfos) {
            Direction direction = tacBlockInfo.state.get(TardisArsDestroyerBlock.FACING);
            BlockPos tacBlockPos = this.getConsoleRoom().getCenterPosition().add(tacBlockInfo.pos).offset(direction).toImmutable();
            BlockPos farTacBlockPos = TardisHelper.TARDIS_POS.add(1024, 0, 1024).multiply(++index).withY(TardisHelper.TARDIS_POS.getY()).toImmutable();

            Map.Entry<Portal, Portal> portals = TardisHelper.createTardisPortals(
                this.world,
                direction,
                Direction.SOUTH,
                tacBlockPos.up(),
                farTacBlockPos.up(),
                this.world.getRegistryKey(),
                -0.5, -0.5, -0.5,
                3, 3
            );

            ((IMixinPortal) portals.getKey()).markAsTardisRoomsEntrance().setTardisId(worldId);
            ((IMixinPortal) portals.getValue()).markAsTardisRoomsEntrance().setTardisId(worldId);
            this.world.spawnEntity(portals.getKey());
            this.world.spawnEntity(portals.getValue());
            this.portalsToRooms.add(portals);
        }
    }

    public void validateEntrancePortals() {
        if (!this.isDoorsOpened()) {
            this.clearEntrancePortals();
        }
        else if (this.portalFromTardis == null || this.portalFromTardis.isRemoved() || this.portalToTardis == null || this.portalToTardis.isRemoved()) {
            this.updateEntrancePortals();
        }

        this.validateRoomsEntrancesPortals();
    }

    public void validateRoomsEntrancesPortals() {
        if (this.portalsToRooms.size() == 0) {
            this.updateRoomsEntrancesPortals();
            return;
        }

        for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
            if (portalsToRoom.getKey() == null || portalsToRoom.getValue() == null) {
                this.updateRoomsEntrancesPortals();
                return;
            }

            if (portalsToRoom.getKey().isRemoved() || portalsToRoom.getValue().isRemoved()) {
                this.updateRoomsEntrancesPortals();
                return;
            }
        }
    }

    public void clearEntrancePortals() {
        try {
            if (this.portalFromTardis != null) this.portalFromTardis.discard();
            if (this.portalToTardis != null) this.portalToTardis.discard();
        } catch (Exception ignored) {
        } finally {
            this.portalFromTardis = null;
            this.portalToTardis = null;
        }
    }

    public void clearRoomsEntrancesPortals() {
        for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
            try {
                portalsToRoom.getKey().discard();
                portalsToRoom.getValue().discard();
            } catch (Exception ignored) {
            }
        }

        this.portalsToRooms.clear();
    }

    public boolean checkIsEntrancePortalValid(Portal portal) {
        return (this.portalFromTardis != null && this.portalFromTardis.equals(portal)) || (this.portalToTardis != null && this.portalToTardis.equals(portal));
    }

    public boolean checkIsRoomEntrancePortalValid(Portal portal) {
        for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
            if ((portalsToRoom.getKey().equals(portal)) || (portalsToRoom.getValue().equals(portal))) return true;
        }

        return false;
    }

    // //////////////////////////// //
    // Tardis Console Tiles methods //
    // //////////////////////////// //

    public List<BaseTardisConsoleUnitBlockEntity> getConsoleTiles() {
        return this.consoleTiles;
    }

    public BaseTardisConsoleUnitBlockEntity getMainConsoleTile() {
        int size = this.consoleTiles.size();
        return size > 0 ? this.consoleTiles.get(size - 1) : null;
    }

    public BlockPos getMainConsolePosition() {
        BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity = this.getMainConsoleTile();
        return tardisConsoleUnitBlockEntity != null ? tardisConsoleUnitBlockEntity.getPos() : this.getEntrancePosition();
    }

    public void updateConsoleTiles() {
        this.consoleTiles.forEach((tile) -> {
            if (!this.isBroken()) {
                this.applyDataToControlsStorage(tile.controlsStorage);
                tile.sendControlsUpdatePacket(this.world);
            }

            NbtCompound tardisStateManagerTag = new NbtCompound();
            this.writeNbt(tardisStateManagerTag);
            tile.tardisStateManager.readNbt(tardisStateManagerTag);

            NbtCompound tag = tile.createNbt();
            this.writeNbt(tag);

            PacketHelper.sendToClient(
                TardisConsoleUnitRemoteCallablePackets.class,
                "updateTardisConsoleUnitData",
                this.world.getWorldChunk(tile.getPos()),
                tile.getPos(), tag
            );
        });
    }

    // /////////////////////////// //
    // Tardis Storage Data methods //
    // /////////////////////////// //

    public void applyDataToControlsStorage(TardisConsoleControlsStorage controlsStorage) {
        controlsStorage.values.put(ETardisConsoleUnitControlRole.STARTER, this.getSystem(TardisSystemFlight.class).inProgress());
        controlsStorage.values.put(ETardisConsoleUnitControlRole.MATERIALIZATION, this.getSystem(TardisSystemMaterialization.class).isMaterialized());
        controlsStorage.values.put(ETardisConsoleUnitControlRole.SAFE_DIRECTION, this.getSystem(TardisSystemMaterialization.class).safeDirection.ordinal());
        controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS, this.getSystem(TardisSystemShields.class).inProgress());
        controlsStorage.values.put(ETardisConsoleUnitControlRole.FUEL_HARVESTING, this.isFuelHarvesting());
        controlsStorage.values.put(ETardisConsoleUnitControlRole.ENERGY_HARVESTING, this.isEnergyHarvesting());
        controlsStorage.values.put(ETardisConsoleUnitControlRole.LIGHT, this.isLightEnabled());
        controlsStorage.values.put(ETardisConsoleUnitControlRole.DOORS, this.isDoorsOpened());
        controlsStorage.values.put(ETardisConsoleUnitControlRole.FACING, switch (this.getDestinationExteriorFacing()) {
            default -> 0;
            case EAST -> 1;
            case SOUTH -> 2;
            case WEST -> 3;
        });
    }

    public void applyControlsStorageToData(TardisConsoleControlsStorage controlsStorage) {
        boolean isInFlight = this.getSystem(TardisSystemFlight.class).inProgress();
        boolean isMaterialized = this.getSystem(TardisSystemMaterialization.class).isMaterialized();

        if (this.destExteriorDimension == null) this.destExteriorDimension = this.currExteriorDimension;
        if (this.destExteriorFacing == null) this.destExteriorFacing = this.currExteriorFacing;
        if (this.destExteriorPosition == null) this.destExteriorPosition = this.currExteriorPosition;

        // Flight
        boolean starter = (boolean) controlsStorage.get(ETardisConsoleUnitControlRole.STARTER);
        boolean handbrake = (boolean) controlsStorage.get(ETardisConsoleUnitControlRole.HANDBRAKE);
        if (this.getSystem(TardisSystemFlight.class).isEnabled()) {
            this.getSystem(TardisSystemFlight.class).setFlight(!handbrake && starter);
            isInFlight = this.getSystem(TardisSystemFlight.class).inProgress();
        }
        else {
            controlsStorage.values.put(ETardisConsoleUnitControlRole.STARTER, isInFlight);
            if (isInFlight && !starter) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
            else if (!isInFlight && starter) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
        }

        // Materialization
        boolean materialization = (boolean) controlsStorage.get(ETardisConsoleUnitControlRole.MATERIALIZATION);
        if (this.getSystem(TardisSystemMaterialization.class).isEnabled()) {
            this.getSystem(TardisSystemMaterialization.class).setSafeDirection(Math.abs((int) controlsStorage.get(ETardisConsoleUnitControlRole.SAFE_DIRECTION)));
            this.getSystem(TardisSystemMaterialization.class).setMaterializationState(materialization);
            isMaterialized = this.getSystem(TardisSystemMaterialization.class).isMaterialized();
        }
        else {
            controlsStorage.values.put(ETardisConsoleUnitControlRole.STARTER, isInFlight);
            controlsStorage.values.put(ETardisConsoleUnitControlRole.MATERIALIZATION, isMaterialized);
            if (isMaterialized && !materialization) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
            else if (!isMaterialized && materialization) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
        }

        // Only if Tardis is not in flight (and could be when dematerialized)
        if (!isInFlight) {
            // Facing
            int facing = (int) controlsStorage.get(ETardisConsoleUnitControlRole.FACING);
            this.destExteriorFacing = switch (facing >= 0 ? facing : ETardisConsoleUnitControlRole.FACING.maxIntValue + facing) {
                default -> Direction.NORTH;
                case 1 -> Direction.EAST;
                case 2 -> Direction.SOUTH;
                case 3 -> Direction.WEST;
            };

            // X Set
            int xSet = (int) controlsStorage.get(ETardisConsoleUnitControlRole.XSET);
            if (xSet != 0) this.destExteriorPosition = xSet > 0 ? this.destExteriorPosition.east(this.xyzStep) : this.destExteriorPosition.west(this.xyzStep);

            // Y Set
            int ySet = (int) controlsStorage.get(ETardisConsoleUnitControlRole.YSET);
            if (ySet != 0) this.destExteriorPosition = ySet > 0 ? this.destExteriorPosition.up(this.xyzStep) : this.destExteriorPosition.down(this.xyzStep);

            // Z Set
            int zSet = (int) controlsStorage.get(ETardisConsoleUnitControlRole.ZSET);
            if (zSet != 0) this.destExteriorPosition = zSet > 0 ? this.destExteriorPosition.south(this.xyzStep) : this.destExteriorPosition.north(this.xyzStep);

            // XYZ Step
            int xyzStep = (int) controlsStorage.get(ETardisConsoleUnitControlRole.XYZSTEP);
            if (xyzStep != 0) this.xyzStep = Math.max(1, Math.min(10000, (int) Math.round(this.xyzStep * (xyzStep > 0 ? 10 : 0.1))));

            // Randomizer
            if ((int) controlsStorage.get(ETardisConsoleUnitControlRole.RANDOMIZER) != 0) {
                boolean facingRandom = Math.random() * 10 > 5;

                if (facingRandom) this.destExteriorPosition = this.destExteriorPosition.east((int) Math.round(Math.random() * 10 * this.xyzStep));
                else this.destExteriorPosition = this.destExteriorPosition.west((int) Math.round(Math.random() * 10 * this.xyzStep));

                if (facingRandom) this.destExteriorPosition = this.destExteriorPosition.south((int) Math.round(Math.random() * 10 * this.xyzStep));
                else this.destExteriorPosition = this.destExteriorPosition.north((int) Math.round(Math.random() * 10 * this.xyzStep));
            }

            // Dimension
            int dimPrev = (int) controlsStorage.get(ETardisConsoleUnitControlRole.DIM_PREV);
            int dimNext = (int) controlsStorage.get(ETardisConsoleUnitControlRole.DIM_NEXT);
            if (dimPrev != 0 || dimNext != 0) {
                Iterable<ServerWorld> worlds = MiscHelper.getServer().getWorlds();
                List<RegistryKey<World>> worldKeys = new ArrayList<>();

                worlds.forEach((world) -> {
                    if (TardisHelper.isTardisDimension(world)) return;
                    if (world.getRegistryKey() == this.world.getRegistryKey()) return;
                    if (ModConfig.COMMON.dimensionsBlacklist.get().contains(world.getRegistryKey().getValue().toString())) return;

                    if (world.getRegistryKey() == World.NETHER) {
                        if (!world.getServer().isNetherAllowed()) return;
                    }

                    if (world.getRegistryKey() == World.END) {
                        EnderDragonFight enderDragonFight = world.getEnderDragonFight();
                        boolean enderDragonWasKilled = enderDragonFight != null && enderDragonFight.hasPreviouslyKilled() && !enderDragonFight.toNbt().getBoolean("NeedsStateScanning");
                        if (!enderDragonWasKilled && ModConfig.COMMON.hideTheEndConditionally.get()) return;
                    }

                    worldKeys.add(world.getRegistryKey());
                });

                if (worldKeys.size() > 0) {
                    int index = worldKeys.contains(this.destExteriorDimension) ? worldKeys.indexOf(this.destExteriorDimension) : 0;
                    index = index + (dimPrev != 0 ? -1 : 1);
                    index %= worldKeys.size();
                    index = index < 0 ? worldKeys.size() - 1 : index;

                    this.destExteriorDimension = worldKeys.get(index);
                }
            }

            // Reset to Prev
            int resetToPrev = (int) controlsStorage.get(ETardisConsoleUnitControlRole.RESET_TO_PREV);
            if (resetToPrev != 0) this.destExteriorDimension = this.getPreviousExteriorDimension();
            if (resetToPrev != 0) this.destExteriorFacing = this.getPreviousExteriorFacing();
            if (resetToPrev != 0) this.destExteriorPosition = this.getPreviousExteriorPosition();

            // Reset to Current
            int resetToCurr = (int) controlsStorage.get(ETardisConsoleUnitControlRole.RESET_TO_CURR);
            if (resetToCurr != 0) this.destExteriorDimension = this.getCurrentExteriorDimension();
            if (resetToCurr != 0) this.destExteriorFacing = this.getCurrentExteriorFacing();
            if (resetToCurr != 0) this.destExteriorPosition = this.getCurrentExteriorPosition();
        }

        // Only if Tardis materialized
        if (isMaterialized) {
            // Shields
            boolean shields = (boolean) controlsStorage.get(ETardisConsoleUnitControlRole.SHIELDS);
            if (this.getSystem(TardisSystemShields.class).isEnabled()) {
                this.getSystem(TardisSystemShields.class).setState(shields);
            }
            else {
                controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS, false);
                if (shields) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
            }

            this.setDoorsOpenState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.DOORS));
            this.setLightState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.LIGHT));
            this.setFuelHarvesting((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.FUEL_HARVESTING));
            this.setEnergyHarvesting((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.ENERGY_HARVESTING));
        }

        this.updateConsoleTiles();
        this.markDirty();
    }

    public void tick() {
        this.systems.values().forEach(ITardisSystem::tick);
        this.validateEntrancePortals();

        if (!this.world.isClient) {
            if (this.fuelHarvesting && this.world.getTime() % ModConfig.COMMON.tardisFuelRefillTiming.get() == 0 && !Transaction.isOpen()) {
                Transaction transaction = Transaction.openOuter();
                long fuelInserted = this.fuelStorage.insert(1, transaction);

                if (fuelInserted != 0) {
                    transaction.commit();
                    this.updateConsoleTiles();
                }
                else {
                    transaction.close();
                }
            }
        }
    }

    private static Direction getDirectionByKey(NbtCompound tag, String key) {
        return Direction.byName(tag.getString(key));
    }

    private void addSystem(ITardisSystem system) {
        this.systems.put(system.getClass(), system);
    }

    private void updateExterior() {
        if (!this.isValid()) return;

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.getCurrentExteriorDimension());
        if (exteriorWorld == null) return;

        BlockPos exteriorBlockPos = this.getCurrentExteriorPosition();
        BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);

        if (exteriorBlockState.getBlock() instanceof BaseTardisExteriorBlock tardisExteriorBlock) {
            if (exteriorBlockState.get(BaseTardisExteriorBlock.OPEN) != this.isDoorsOpened()) {
                if (this.isDoorsOpened()) ModSounds.playTardisDoorsOpenSound(exteriorWorld, exteriorBlockPos, tardisExteriorBlock.isWooden());
                else ModSounds.playTardisDoorsCloseSound(exteriorWorld, exteriorBlockPos, tardisExteriorBlock.isWooden());
            }

            if (exteriorBlockState.get(BaseTardisExteriorBlock.LIT) != this.isLightEnabled()) {
                if (this.isLightEnabled()) ModSounds.playTardisLightOnSound(exteriorWorld, exteriorBlockPos);
                else ModSounds.playTardisLightOffSound(exteriorWorld, exteriorBlockPos);
            }

            exteriorBlockState = exteriorBlockState.with(BaseTardisExteriorBlock.OPEN, this.isDoorsOpened());
            exteriorBlockState = exteriorBlockState.with(BaseTardisExteriorBlock.LIT, this.isLightEnabled());
            exteriorWorld.setBlockState(exteriorBlockPos, exteriorBlockState, 3);

            if (exteriorWorld.getBlockState(exteriorBlockPos.up()).getBlock() instanceof BaseTardisExteriorBlock) {
                exteriorBlockState = exteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
                exteriorWorld.setBlockState(exteriorBlockPos.up(), exteriorBlockState, 3);
            }
        }

        PacketHelper.sendToClient(
            TardisExteriorRemoteCallablePackets.class,
            "updateTardisExteriorData",
            world.getWorldChunk(exteriorBlockPos),
            exteriorBlockPos, this.isDoorsOpened(), false
        );
    }
}

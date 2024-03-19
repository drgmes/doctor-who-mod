package net.drgmes.dwm.common.tardis;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.common.tardis.consoleunits.controls.ETardisConsoleUnitControlRole;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.compat.ImmersivePortals;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItem;
import net.drgmes.dwm.network.client.TardisConsoleUnitUpdatePacket;
import net.drgmes.dwm.network.client.TardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.boss.dragon.EnderDragonFight;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

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

    private RegistryKey<World> prevExteriorDimension;
    private RegistryKey<World> currExteriorDimension;
    private RegistryKey<World> destExteriorDimension;

    private BlockPos prevExteriorPosition;
    private BlockPos currExteriorPosition;
    private BlockPos destExteriorPosition;

    private Direction prevExteriorFacing;
    private Direction currExteriorFacing;
    private Direction destExteriorFacing;

    private boolean inited = false;
    private boolean broken = true;
    private boolean doorsLocked = true;
    private boolean doorsOpened = false;
    private boolean lightEnabled = false;
    private boolean shieldsEnabled = false;
    private boolean shieldsOxygenEnabled = false;
    private boolean shieldsFireProofEnabled = false;
    private boolean shieldsMedicalEnabled = false;
    private boolean shieldsMiningEnabled = false;
    private boolean shieldsGravitationEnabled = false;
    private boolean shieldsSpecialEnabled = false;
    private boolean handbrakeLocked = false;
    private boolean fuelHarvesting = false;
    private boolean energyHarvesting = false;

    private int xyzStep = 1;
    private int fuelCapacity = 100;
    private int fuelAmount = 0;
    private int energyCapacity = 1000000;
    private int energyAmount = 0;

    private TardisConsoleRoomEntry consoleRoom;

    public TardisStateManager() {
        this.addSystem(new TardisSystemMaterialization(this));
        this.addSystem(new TardisSystemFlight(this));
        this.addSystem(new TardisSystemShields(this));
    }

    public static PersistentState.Type<TardisStateManager> getPersistentStateType() {
        return new PersistentState.Type<>(TardisStateManager::new, TardisStateManager::createFromNbt, DataFixTypes.SAVED_DATA_MAP_INDEX);
    }

    public static Optional<TardisStateManager> get(ServerWorld world) {
        if (world == null) return Optional.empty();

        TardisStateManager tardis = world.getPersistentStateManager().getOrCreate(
            TardisStateManager.getPersistentStateType(),
            DWM.LOCS.TARDIS.getPath()
        );

        if (tardis != null) tardis.setWorld(world);
        return Optional.ofNullable(tardis);
    }

    public static TardisStateManager createFromNbt(NbtCompound tag) {
        TardisStateManager tardisStateManager = new TardisStateManager();
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

        tag.putBoolean("inited", this.inited);
        tag.putBoolean("broken", this.broken);
        tag.putBoolean("doorsLocked", this.doorsLocked);
        tag.putBoolean("doorsOpened", this.doorsOpened);
        tag.putBoolean("lightEnabled", this.lightEnabled);
        tag.putBoolean("shieldsEnabled", this.shieldsEnabled);
        tag.putBoolean("shieldsOxygenEnabled", this.shieldsOxygenEnabled);
        tag.putBoolean("shieldsFireProofEnabled", this.shieldsFireProofEnabled);
        tag.putBoolean("shieldsMedicalEnabled", this.shieldsMedicalEnabled);
        tag.putBoolean("shieldsMiningEnabled", this.shieldsMiningEnabled);
        tag.putBoolean("shieldsGravitationEnabled", this.shieldsGravitationEnabled);
        tag.putBoolean("shieldsSpecialEnabled", this.shieldsSpecialEnabled);
        tag.putBoolean("handbrakeLocked", this.handbrakeLocked);
        tag.putBoolean("fuelHarvesting", this.fuelHarvesting);
        tag.putBoolean("energyHarvesting", this.energyHarvesting);

        tag.putInt("xyzStep", this.xyzStep);
        tag.putInt("fuelCapacity", this.fuelCapacity);
        tag.putInt("fuelAmount", this.fuelAmount);
        tag.putInt("energyCapacity", this.energyCapacity);
        tag.putInt("energyAmount", this.energyAmount);

        tag.putString("consoleRoom", this.getConsoleRoom().name);

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

        this.inited = tag.getBoolean("inited");
        this.broken = tag.getBoolean("broken");
        this.doorsLocked = tag.getBoolean("doorsLocked");
        this.doorsOpened = tag.getBoolean("doorsOpened");
        this.lightEnabled = tag.getBoolean("lightEnabled");
        this.shieldsEnabled = tag.getBoolean("shieldsEnabled");
        this.shieldsOxygenEnabled = tag.getBoolean("shieldsOxygenEnabled");
        this.shieldsFireProofEnabled = tag.getBoolean("shieldsFireProofEnabled");
        this.shieldsMedicalEnabled = tag.getBoolean("shieldsMedicalEnabled");
        this.shieldsMiningEnabled = tag.getBoolean("shieldsMiningEnabled");
        this.shieldsGravitationEnabled = tag.getBoolean("shieldsGravitationEnabled");
        this.shieldsSpecialEnabled = tag.getBoolean("shieldsSpecialEnabled");
        this.handbrakeLocked = tag.getBoolean("handbrakeLocked");
        this.fuelHarvesting = tag.getBoolean("fuelHarvesting");
        this.energyHarvesting = tag.getBoolean("energyHarvesting");

        this.xyzStep = tag.getInt("xyzStep");
        this.fuelCapacity = tag.getInt("fuelCapacity");
        this.fuelAmount = Math.min(tag.getInt("fuelAmount"), this.fuelCapacity);
        this.energyCapacity = tag.getInt("energyCapacity");
        this.energyAmount = Math.min(tag.getInt("energyAmount"), this.energyCapacity);

        if (tag.contains("consoleRoom")) this.consoleRoom = TardisConsoleRooms.getConsoleRoom(tag.getString("consoleRoom"), this.broken);

        this.systems.values().forEach((system) -> {
            if (tag.contains(system.getClass().getName())) {
                system.load(tag.getCompound(system.getClass().getName()));
            }
        });
    }

    public void init() {
        if (this.inited) return;
        this.getConsoleRoom().place(this);
        this.updateConsoleTiles();
        this.inited = true;
    }

    // /////////////////////////// //
    // Tardis Primary Data methods //
    // /////////////////////////// //

    public String getId() {
        return DimensionHelper.getWorldId(this.getWorld());
    }

    public ServerWorld getWorld() {
        return this.world;
    }

    public void setWorld(ServerWorld world) {
        this.world = world;
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        this.markDirty();
    }

    public boolean checkAccess(PlayerEntity player, boolean deep, boolean owningRequired) {
        boolean hasBaseAccess = player == null || (owningRequired ?
            (this.getOwner() != null && player.getUuid().equals(this.getOwner())) :
            (this.getOwner() == null || player.getUuid().equals(this.getOwner()))
        );

        if (!hasBaseAccess && deep) {
            for (ItemStack itemStack : player.getInventory().main) {
                if (itemStack.getItem() instanceof TardisKeyItem) {
                    NbtCompound tag = itemStack.getOrCreateNbt();
                    if (tag.contains("tardisId") && tag.getString("tardisId").equals(this.getId())) return true;
                }
            }
        }

        return hasBaseAccess;
    }

    // //////////////////////// //
    // Tardis Dimension methods //
    // //////////////////////// //

    public RegistryKey<World> getPreviousExteriorDimension() {
        return this.prevExteriorDimension != null ? this.prevExteriorDimension : this.getCurrentExteriorDimension();
    }

    public RegistryKey<World> getCurrentExteriorDimension() {
        return this.currExteriorDimension != null ? this.currExteriorDimension : World.OVERWORLD;
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
        return this.currExteriorPosition != null ? this.currExteriorPosition.toImmutable() : BlockPos.ORIGIN.toImmutable();
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
        return this.currExteriorFacing != null ? this.currExteriorFacing : Direction.NORTH;
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

        if (!flag) ModSounds.playTardisRepairSound(this.world, this.getMainConsolePosition());
        this.markDirty();
        return true;
    }

    public boolean isDoorsLocked() {
        return this.doorsLocked;
    }

    public boolean setDoorsLockState(boolean flag, PlayerEntity player) {
        if (!this.checkAccess(player, false, true)) return false;
        if (this.doorsLocked == flag) return false;
        this.doorsLocked = flag;

        if (flag) ModSounds.playTardisDoorsLockSound(this.world, this.getEntrancePosition());
        else ModSounds.playTardisDoorsUnlockSound(this.world, this.getEntrancePosition());

        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.getCurrentExteriorDimension(), this.getWorld().getServer());
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
        if (entranceBlockState.getBlock() instanceof BaseTardisDoorsBlock<?> tardisDoorsBlock) {
            if (flag) ModSounds.playTardisDoorsOpenSound(this.world, entrancePosition, tardisDoorsBlock.isWooden());
            else ModSounds.playTardisDoorsCloseSound(this.world, entrancePosition, tardisDoorsBlock.isWooden());
        }

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

        if (!flag) {
            this.shieldsOxygenEnabled = false;
            this.shieldsFireProofEnabled = false;
            this.shieldsMedicalEnabled = false;
            this.shieldsMiningEnabled = false;
            this.shieldsGravitationEnabled = false;
            this.shieldsSpecialEnabled = false;
        }

        this.markDirty();
        return true;
    }

    public boolean isShieldsOxygenEnabled() {
        return this.shieldsOxygenEnabled;
    }

    public boolean setShieldsOxygenState(boolean flag) {
        if (this.shieldsOxygenEnabled == flag) return false;
        this.shieldsOxygenEnabled = flag;

        if (flag) ModSounds.playTardisShieldsOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisShieldsOffSound(this.world, this.getMainConsolePosition());

        this.markDirty();
        return true;
    }

    public boolean isShieldsFireProofEnabled() {
        return this.shieldsFireProofEnabled;
    }

    public boolean setShieldsFireProofState(boolean flag) {
        if (this.shieldsFireProofEnabled == flag) return false;
        this.shieldsFireProofEnabled = flag;

        if (flag) ModSounds.playTardisShieldsOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisShieldsOffSound(this.world, this.getMainConsolePosition());

        this.markDirty();
        return true;
    }

    public boolean isShieldsMedicalEnabled() {
        return this.shieldsMedicalEnabled;
    }

    public boolean setShieldsMedicalState(boolean flag) {
        if (this.shieldsMedicalEnabled == flag) return false;
        this.shieldsMedicalEnabled = flag;

        if (flag) ModSounds.playTardisShieldsOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisShieldsOffSound(this.world, this.getMainConsolePosition());

        this.markDirty();
        return true;
    }

    public boolean isShieldsMiningEnabled() {
        return this.shieldsMiningEnabled;
    }

    public boolean setShieldsMiningState(boolean flag) {
        if (this.shieldsMiningEnabled == flag) return false;
        this.shieldsMiningEnabled = flag;

        if (flag) ModSounds.playTardisShieldsOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisShieldsOffSound(this.world, this.getMainConsolePosition());

        this.markDirty();
        return true;
    }

    public boolean isShieldsGravitationEnabled() {
        return this.shieldsGravitationEnabled;
    }

    public boolean setShieldsGravitationState(boolean flag) {
        if (this.shieldsGravitationEnabled == flag) return false;
        this.shieldsGravitationEnabled = flag;

        if (flag) ModSounds.playTardisShieldsOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisShieldsOffSound(this.world, this.getMainConsolePosition());

        this.markDirty();
        return true;
    }

    public boolean isShieldsSpecialEnabled() {
        return this.shieldsSpecialEnabled;
    }

    public boolean setShieldsSpecialState(boolean flag) {
        if (this.shieldsSpecialEnabled == flag) return false;
        this.shieldsSpecialEnabled = flag;

        if (flag) ModSounds.playTardisShieldsOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisShieldsOffSound(this.world, this.getMainConsolePosition());

        this.markDirty();
        return true;
    }

    public boolean isHandbrakeLocked() {
        return this.handbrakeLocked;
    }

    public boolean setHandbrakeLockState(boolean flag, PlayerEntity player) {
        if (!this.checkAccess(player, true, true)) return false;
        if (this.handbrakeLocked == flag) return false;
        this.handbrakeLocked = flag;

        if (flag) ModSounds.playTardisHandbrakeOnSound(this.world, this.getMainConsolePosition());
        else ModSounds.playTardisHandbrakeOffSound(this.world, this.getMainConsolePosition());

        this.markDirty();
        return true;
    }

    public boolean isFuelHarvesting() {
        return this.fuelHarvesting;
    }

    public void setFuelHarvesting(boolean flag) {
        this.fuelHarvesting = flag;
        this.markDirty();
    }

    public boolean isEnergyHarvesting() {
        return this.energyHarvesting;
    }

    public void setEnergyHarvesting(boolean flag) {
        this.energyHarvesting = flag;
        this.markDirty();
    }

    public int getXYZStep() {
        return this.xyzStep;
    }

    public void setXYZStep(int value) {
        this.xyzStep = Math.min(100000, Math.max(1, value));
        this.markDirty();
    }

    public int getFuelCapacity() {
        return this.fuelCapacity;
    }

    public void setFuelCapacity(int value) {
        this.fuelCapacity = value;
        this.markDirty();
    }

    public int getFuelAmount() {
        return this.fuelAmount;
    }

    public void setFuelAmount(int value) {
        this.fuelAmount = value;
        this.markDirty();
    }

    public int getEnergyCapacity() {
        return this.energyCapacity;
    }

    public void setEnergyCapacity(int value) {
        this.energyCapacity = value;
        this.markDirty();
    }

    public int getEnergyAmount() {
        return this.energyAmount;
    }

    public void setEnergyAmount(int value) {
        this.energyAmount = value;
        this.markDirty();
    }

    // /////////////////// //
    // Tardis Room methods //
    // /////////////////// //

    public TardisConsoleRoomEntry getConsoleRoom() {
        if (this.consoleRoom == null) this.setConsoleRoom(TardisConsoleRooms.getConsoleRoom(null, this.isBroken()));
        return this.consoleRoom;
    }

    public void setConsoleRoom(TardisConsoleRoomEntry consoleRoom) {
        this.consoleRoom = consoleRoom;
        this.markDirty();
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
            this.world.setBlockState(tile.getPos(), tile.getCachedState().with(BaseTardisDoorsBlock.OPEN, this.isDoorsOpened()), Block.NOTIFY_ALL);
        });
    }

    // ////////////////////// //
    // Tardis Portals methods //
    // ////////////////////// //

    public ImmersivePortals.TardisPortalsState getPortalsState() {
        return ImmersivePortals.getOrCreateTardisPortalsState(this);
    }

    public void updateEntrancePortals() {
        if (!ModCompats.immersivePortals()) return;
        this.getPortalsState().clearEntrancePortals();
        if (this.isDoorsOpened()) this.getPortalsState().createEntrancePortals();
    }

    public void updateRoomEntrancePortals() {
        if (this.isBroken()) return;
        if (!ModCompats.immersivePortals()) return;
        this.getPortalsState().clearRoomEntrancePortals();
        this.getPortalsState().createRoomsEntrancesPortals();
    }

    public void validatePortals() {
        if (!ModCompats.immersivePortals()) return;
        if (!this.isDoorsOpened()) this.getPortalsState().clearEntrancePortals();
        if (this.isDoorsOpened() && !this.getPortalsState().isEntrancePortalsValid()) this.updateEntrancePortals();
        if (!this.getPortalsState().isRoomEntrancePortalsValid()) this.updateRoomEntrancePortals();
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
                tile.controlsStorage.applyDataToControlsStorage(this);
                tile.sendControlsUpdatePacket(this.world);
            }

            NbtCompound tardisStateManagerTag = new NbtCompound();
            this.writeNbt(tardisStateManagerTag);
            tile.tardis.readNbt(tardisStateManagerTag);

            NbtCompound tag = tile.createNbt();
            this.writeNbt(tag);

            new TardisConsoleUnitUpdatePacket(tile.getPos(), tag)
                // .sendToChunkListeners(this.world.getWorldChunk(tile.getPos()));
                .sendToLevel(this.world);
        });
    }

    // /////////////////////////// //
    // Tardis Storage Data methods //
    // /////////////////////////// //

    public void applyControlsStorageToData(TardisConsoleControlsStorage controlsStorage, PlayerEntity player) {
        TardisSystemFlight flightSystem = this.getSystem(TardisSystemFlight.class);
        TardisSystemMaterialization materializationSystem = this.getSystem(TardisSystemMaterialization.class);
        TardisSystemShields shieldsSystem = this.getSystem(TardisSystemShields.class);

        boolean isInFlight = flightSystem.inProgress();
        boolean isMaterialized = materializationSystem.isMaterialized();

        if (this.destExteriorDimension == null) this.destExteriorDimension = this.currExteriorDimension;
        if (this.destExteriorFacing == null) this.destExteriorFacing = this.currExteriorFacing;
        if (this.destExteriorPosition == null) this.destExteriorPosition = this.currExteriorPosition;

        // Handbrake
        boolean handbrake = (boolean) controlsStorage.get(ETardisConsoleUnitControlRole.HANDBRAKE);
        if (!this.setHandbrakeLockState(handbrake, player)) {
            controlsStorage.values.put(ETardisConsoleUnitControlRole.HANDBRAKE, this.isHandbrakeLocked());
            if (handbrake != this.isHandbrakeLocked()) ModSounds.playTardisBellSound(this.world, this.getMainConsolePosition());
        }

        // Flight
        boolean starter = (boolean) controlsStorage.get(ETardisConsoleUnitControlRole.STARTER);
        if (flightSystem.isEnabled() && !this.isHandbrakeLocked()) {
            flightSystem.setFlight(starter);
            isInFlight = flightSystem.inProgress();
        }
        else {
            controlsStorage.values.put(ETardisConsoleUnitControlRole.STARTER, isInFlight);
            if (isInFlight && !starter) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
            else if (!isInFlight && starter) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
        }

        // Materialization
        boolean materialization = (boolean) controlsStorage.get(ETardisConsoleUnitControlRole.MATERIALIZATION);
        if (materializationSystem.isEnabled() && !this.isHandbrakeLocked()) {
            materializationSystem.setSafeDirection(Math.abs((int) controlsStorage.get(ETardisConsoleUnitControlRole.SAFE_DIRECTION)));
            materializationSystem.setMaterializationState(materialization);
            isMaterialized = materializationSystem.isMaterialized();
        }
        else {
            controlsStorage.values.put(ETardisConsoleUnitControlRole.STARTER, isInFlight);
            controlsStorage.values.put(ETardisConsoleUnitControlRole.MATERIALIZATION, isMaterialized);
            if (isMaterialized && !materialization) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
            else if (!isMaterialized && materialization) ModSounds.playTardisFailSound(this.world, this.getMainConsolePosition());
        }

        // If Tardis has a fight system
        if (flightSystem.isEnabled()) {
            // XYZ Step
            int xyzStep = (int) controlsStorage.get(ETardisConsoleUnitControlRole.XYZSTEP);
            if (xyzStep != 0) this.setXYZStep((int) Math.round(this.xyzStep * (xyzStep > 0 ? 10 : 0.1)));
        }

        // If Tardis has a fight system and it is not in flight
        if (flightSystem.isEnabled() && !isInFlight) {
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
                Iterable<ServerWorld> worlds = this.getWorld().getServer().getWorlds();
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
                        boolean enderDragonWasKilled = enderDragonFight != null && enderDragonFight.hasPreviouslyKilled() && !enderDragonFight.toData().needsStateScanning();
                        if (!enderDragonWasKilled && ModConfig.COMMON.hideTheEndConditionally.get()) return;
                    }

                    worldKeys.add(world.getRegistryKey());
                });

                if (!worldKeys.isEmpty()) {
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

        // Only if Tardis is not in flight
        if (!isInFlight) {
            this.setFuelHarvesting((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.FUEL_HARVESTING));
            this.setEnergyHarvesting((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.ENERGY_HARVESTING));
        }

        // Only if Tardis materialized
        if (isMaterialized) {
            // Shields
            boolean shields = (boolean) controlsStorage.get(ETardisConsoleUnitControlRole.SHIELDS);
            if (shieldsSystem.isEnabled()) {
                shieldsSystem.setState(shields);

                this.setShieldsOxygenState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.SHIELDS_OXYGEN) && shields);
                this.setShieldsFireProofState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.SHIELDS_FIRE_PROOF) && shields);
                this.setShieldsMedicalState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.SHIELDS_MEDICAL) && shields);
                this.setShieldsMiningState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.SHIELDS_MINING) && shields);
                this.setShieldsGravitationState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.SHIELDS_GRAVITATION) && shields);
                this.setShieldsSpecialState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.SHIELDS_SPECIAL) && shields);
            }
            else {
                controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS, false);
                controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS_OXYGEN, false);
                controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS_FIRE_PROOF, false);
                controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS_MEDICAL, false);
                controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS_MINING, false);
                controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS_GRAVITATION, false);
                controlsStorage.values.put(ETardisConsoleUnitControlRole.SHIELDS_SPECIAL, false);
            }

            // Other
            this.setDoorsOpenState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.DOORS));
            this.setLightState((boolean) controlsStorage.get(ETardisConsoleUnitControlRole.LIGHT));
        }

        this.updateConsoleTiles();
        this.markDirty();
    }

    @SuppressWarnings("UnstableApiUsage")
    public void tick() {
        this.systems.values().forEach(ITardisSystem::tick);

        if (this.world.getTime() % 20 == 0) {
            this.validatePortals();
        }

        if (this.isFuelHarvesting() && this.world.getTime() % ModConfig.COMMON.tardisFuelRefillTiming.get() == 0 && this.fuelAmount < this.fuelCapacity) {
            this.setFuelAmount(this.fuelAmount + 1);
            this.updateConsoleTiles();
        }
    }

    private static Direction getDirectionByKey(NbtCompound tag, String key) {
        return Direction.byName(tag.getString(key));
    }

    private void addSystem(ITardisSystem system) {
        this.systems.put(system.getClass(), system);
    }

    private void updateExterior() {
        ServerWorld exteriorWorld = DimensionHelper.getWorld(this.getCurrentExteriorDimension(), this.getWorld().getServer());
        if (exteriorWorld == null) return;

        BlockPos exteriorBlockPos = this.getCurrentExteriorPosition();
        BlockState exteriorBlockState = exteriorWorld.getBlockState(exteriorBlockPos);

        if (exteriorBlockState.getBlock() instanceof BaseTardisExteriorBlock<?> tardisExteriorBlock) {
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
            exteriorWorld.setBlockState(exteriorBlockPos, exteriorBlockState, Block.NOTIFY_ALL);

            if (exteriorWorld.getBlockState(exteriorBlockPos.up()).getBlock() instanceof BaseTardisExteriorBlock) {
                exteriorBlockState = exteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
                exteriorBlockState = exteriorBlockState.with(BaseTardisExteriorBlock.WATERLOGGED, exteriorWorld.getBlockState(exteriorBlockPos.up()).getFluidState().isIn(FluidTags.WATER));
                exteriorWorld.setBlockState(exteriorBlockPos.up(), exteriorBlockState, Block.NOTIFY_ALL);
            }
        }

        new TardisExteriorUpdatePacket(exteriorBlockPos, this.isDoorsOpened(), this.isLightEnabled(), false)
            // TODO uncomment method when this will work properly
            // .sendToChunkListeners(exteriorWorld.getWorldChunk(exteriorBlockPos));
            .sendToLevel(exteriorWorld);
    }
}

package net.drgmes.dwm.common.tardis;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorEntry;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriors;
import net.drgmes.dwm.common.tardis.systems.*;
import net.drgmes.dwm.compat.immersiveportals.ImmersivePortals;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.items.tardis.systems.TardisSystemItem;
import net.drgmes.dwm.network.client.TardisConsoleUnitUpdatePacket;
import net.drgmes.dwm.setup.ModCompats;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.datafixer.DataFixTypes;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ChunkTicketType;
import net.minecraft.server.world.ServerChunkManager;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class TardisStateManager extends PersistentState {
    private static final ChunkTicketType<ChunkPos> CHUNK_TICKET_TYPE = ChunkTicketType.create("dwm:tardis_loaded_chunks", Comparator.comparingLong(ChunkPos::toLong));

    public static final int SYSTEM_COMPONENTS_CONTAINER_SIZE = 14;
    public static final int BATTERY_COMPONENTS_CONTAINER_SIZE = 0;
    public static final int UPGRADE_COMPONENTS_CONTAINER_SIZE = 0;

    private final Map<Class<? extends TardisBaseSystem>, TardisBaseSystem> systems = new LinkedHashMap<>();
    private final Map<BlockPos, BaseTardisConsoleUnitBlockEntity> consoleTiles = new LinkedHashMap<>();
    private final Map<BlockPos, BaseTardisDoorsBlockEntity> doorsTiles = new LinkedHashMap<>();

    private DefaultedList<ItemStack> systemComponents = DefaultedList.ofSize(SYSTEM_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);
    private DefaultedList<ItemStack> batteryComponents = DefaultedList.ofSize(BATTERY_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);
    private DefaultedList<ItemStack> upgradeComponents = DefaultedList.ofSize(UPGRADE_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);

    private boolean updatedExterior;
    private boolean updatedDoorsTiles;
    private boolean updatedConsoleTiles;

    private ServerWorld world;
    private UUID owner;

    private TardisExteriorEntry exteriorType;
    private TardisConsoleRoomEntry consoleRoom;

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

    public TardisStateManager() {
        this.addSystem(new TardisSystemConsoleRoom(this));
        this.addSystem(new TardisSystemResearch(this));
        this.addSystem(new TardisSystemMaterialization(this));
        this.addSystem(new TardisSystemFlight(this));
        this.addSystem(new TardisSystemShields(this));
    }

    public static PersistentState.Type<TardisStateManager> getPersistentStateType() {
        return new PersistentState.Type<>(TardisStateManager::new, TardisStateManager::createFromNbt, DataFixTypes.LEVEL);
    }

    public static Optional<TardisStateManager> get(ServerWorld world) {
        if (!TardisHelper.isTardisDimension(world)) return Optional.empty();

        TardisStateManager tardis = world.getPersistentStateManager().getOrCreate(
            TardisStateManager.getPersistentStateType(),
            DWM.LOCS.TARDIS.getPath()
        );

        if (tardis != null) tardis.setWorld(world);
        return Optional.ofNullable(tardis);
    }

    public static TardisStateManager createFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        TardisStateManager tardisStateManager = new TardisStateManager();
        tardisStateManager.readNbt(tag, registryLookup);
        return tardisStateManager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        NbtCompound tdTagSystemComponents = new NbtCompound();
        Inventories.writeNbt(tdTagSystemComponents, this.systemComponents, registryLookup);
        tag.put("tdTagSystemComponents", tdTagSystemComponents);

        NbtCompound tdTagBatteryComponents = new NbtCompound();
        Inventories.writeNbt(tdTagBatteryComponents, this.batteryComponents, registryLookup);
        tag.put("tdTagBatteryComponents", tdTagBatteryComponents);

        NbtCompound tdTagUpgradeComponents = new NbtCompound();
        Inventories.writeNbt(tdTagUpgradeComponents, this.upgradeComponents, registryLookup);
        tag.put("tdTagUpgradeComponents", tdTagUpgradeComponents);

        if (this.owner != null) tag.putUuid("owner", this.owner);
        if (this.exteriorType != null) tag.putString("exteriorType", this.exteriorType.name);
        if (this.consoleRoom != null) tag.putString("consoleRoom", this.consoleRoom.name);

        if (this.prevExteriorDimension != null) tag.putString("prevExteriorDimension", this.prevExteriorDimension.getValue().toString());
        if (this.currExteriorDimension != null) tag.putString("currExteriorDimension", this.currExteriorDimension.getValue().toString());
        if (this.destExteriorDimension != null) tag.putString("destExteriorDimension", this.destExteriorDimension.getValue().toString());

        if (this.prevExteriorPosition != null) tag.putLong("prevExteriorPosition", this.prevExteriorPosition.asLong());
        if (this.currExteriorPosition != null) tag.putLong("currExteriorPosition", this.currExteriorPosition.asLong());
        if (this.destExteriorPosition != null) tag.putLong("destExteriorPosition", this.destExteriorPosition.asLong());

        if (this.prevExteriorFacing != null) tag.putString("prevExteriorFacing", this.prevExteriorFacing.getName());
        if (this.currExteriorFacing != null) tag.putString("currExteriorFacing", this.currExteriorFacing.getName());
        if (this.destExteriorFacing != null) tag.putString("destExteriorFacing", this.destExteriorFacing.getName());

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

        this.systems.values().forEach((system) -> {
            tag.put(system.getClass().getSimpleName(), system.writeNbt(new NbtCompound()));
        });

        AtomicInteger i1 = new AtomicInteger();
        NbtCompound doorsTilesTag = new NbtCompound();
        this.doorsTiles.keySet().forEach((pos) -> doorsTilesTag.putLong(String.valueOf(i1.getAndIncrement()), pos.asLong()));
        tag.put("doorsTilesTag", doorsTilesTag);

        AtomicInteger i2 = new AtomicInteger();
        NbtCompound consoleTilesTag = new NbtCompound();
        this.consoleTiles.keySet().forEach((pos) -> consoleTilesTag.putLong(String.valueOf(i2.getAndIncrement()), pos.asLong()));
        tag.put("consoleTilesTag", consoleTilesTag);

        return tag;
    }

    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.systemComponents = DefaultedList.ofSize(this.systemComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagSystemComponents"), this.systemComponents, registryLookup);

        this.batteryComponents = DefaultedList.ofSize(this.batteryComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagBatteryComponents"), this.batteryComponents, registryLookup);

        this.upgradeComponents = DefaultedList.ofSize(this.upgradeComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagUpgradeComponents"), this.upgradeComponents, registryLookup);

        if (tag.contains("owner")) this.owner = tag.getUuid("owner");
        if (tag.contains("exteriorType")) this.exteriorType = TardisExteriors.getExteriorType(tag.getString("exteriorType"));
        if (tag.contains("consoleRoom")) this.consoleRoom = TardisConsoleRooms.getConsoleRoom(tag.getString("consoleRoom"), this.broken);

        if (tag.contains("prevExteriorDimension")) this.prevExteriorDimension = DimensionHelper.getWorldKey(tag.getString("prevExteriorDimension"));
        if (tag.contains("currExteriorDimension")) this.currExteriorDimension = DimensionHelper.getWorldKey(tag.getString("currExteriorDimension"));
        if (tag.contains("destExteriorDimension")) this.destExteriorDimension = DimensionHelper.getWorldKey(tag.getString("destExteriorDimension"));

        if (tag.contains("prevExteriorPosition")) this.prevExteriorPosition = BlockPos.fromLong(tag.getLong("prevExteriorPosition"));
        if (tag.contains("currExteriorPosition")) this.currExteriorPosition = BlockPos.fromLong(tag.getLong("currExteriorPosition"));
        if (tag.contains("destExteriorPosition")) this.destExteriorPosition = BlockPos.fromLong(tag.getLong("destExteriorPosition"));

        if (tag.contains("prevExteriorFacing")) this.prevExteriorFacing = Direction.byName(tag.getString("prevExteriorFacing"));
        if (tag.contains("currExteriorFacing")) this.currExteriorFacing = Direction.byName(tag.getString("currExteriorFacing"));
        if (tag.contains("destExteriorFacing")) this.destExteriorFacing = Direction.byName(tag.getString("destExteriorFacing"));

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

        this.systems.values().forEach((system) -> {
            // TODO remove getName after few releases (for fallback)
            if (tag.contains(system.getClass().getSimpleName())) {
                system.readNbt(tag.getCompound(system.getClass().getSimpleName()));
            } else if (tag.contains(system.getClass().getName())) {
                system.readNbt(tag.getCompound(system.getClass().getName()));
            }
        });

        if (tag.contains("doorsTilesTag")) {
            NbtCompound doorsTilesTag = tag.getCompound("doorsTilesTag");
            doorsTilesTag.getKeys().forEach((key) -> this.doorsTiles.putIfAbsent(BlockPos.fromLong(doorsTilesTag.getLong(key)), null));
        }

        if (tag.contains("consoleTilesTag")) {
            NbtCompound consoleTilesTag = tag.getCompound("consoleTilesTag");
            consoleTilesTag.getKeys().forEach((key) -> this.consoleTiles.putIfAbsent(BlockPos.fromLong(consoleTilesTag.getLong(key)), null));
        }
    }

    public void init() {
        if (this.inited) return;
        this.inited = true;

        this.getConsoleRoom().place(this);
        this.markConsoleTilesUpdated();
    }

    // /////////////////////////// //
    // Tardis Primary Data methods //
    // /////////////////////////// //

    public String getId() {
        return DimensionHelper.getWorldId(this.world);
    }

    public ServerWorld getWorld() {
        return this.world;
    }

    public void setWorld(ServerWorld world) {
        this.unbindChunkLoaders();
        this.world = world;
        this.bindChunkLoaders();
    }

    public ServerWorld getExteriorWorld() {
        return DimensionHelper.getWorld(this.getCurrentExteriorDimension(), this.world.getServer());
    }

    public UUID getOwner() {
        return this.owner;
    }

    public void setOwner(UUID uuid) {
        this.owner = uuid;
        this.markDirty();
    }

    public TardisExteriorEntry getExteriorType() {
        return this.exteriorType;
    }

    public void setExteriorType(TardisExteriorEntry exteriorType) {
        this.exteriorType = exteriorType;
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
                    NbtCompound tag = CommonHelper.getItemStackData(itemStack).copyNbt();
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

        ServerWorld exteriorWorld = this.getExteriorWorld();
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

        this.markDirty();
        this.markExteriorUpdated();
        this.markDoorsTilesUpdated();
        this.updateEntrancePortals();
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

        this.markDirty();
        this.markExteriorUpdated();
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
        // return this.fuelHarvesting;
        // TODO redone fuel system
        return true;
    }

    public void setFuelHarvesting(boolean flag) {
        this.fuelHarvesting = flag;
        this.markDirty();
    }

    public boolean isEnergyHarvesting() {
        // return this.energyHarvesting;
        // TODO redone fuel system
        return true;
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
        // return this.fuelAmount;
        // TODO redone fuel system
        return this.fuelCapacity;
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
        // return this.energyAmount;
        // TODO redone fuel system
        return this.energyCapacity;
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
    public <T extends TardisBaseSystem> T getSystem(Class<T> system) {
        return (T) this.systems.getOrDefault(system, null);
    }

    public boolean isSystemEnabled(Class<? extends TardisBaseSystem> system) {
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

    // //////////////////////////// //
    // Tardis Console Tiles methods //
    // //////////////////////////// //

    public Map<BlockPos, BaseTardisConsoleUnitBlockEntity> getConsoleTiles() {
        return this.consoleTiles;
    }

    public void addConsoleTile(BaseTardisConsoleUnitBlockEntity consoleTile) {
        boolean hasConsoleTile = this.consoleTiles.containsKey(consoleTile.getPos());
        this.consoleTiles.put(consoleTile.getPos(), consoleTile);
        if (!hasConsoleTile) this.markDirty();
    }

    public void removeConsoleTile(BaseTardisConsoleUnitBlockEntity consoleTile) {
        boolean hasConsoleTile = this.consoleTiles.containsKey(consoleTile.getPos());
        if (!hasConsoleTile) return;

        this.consoleTiles.remove(consoleTile.getPos());
        this.markDirty();
    }

    public BaseTardisConsoleUnitBlockEntity getMainConsoleTile() {
        int size = this.consoleTiles.size();
        return size > 0 ? this.consoleTiles.values().stream().toList().get(size - 1) : null;
    }

    public BlockPos getMainConsolePosition() {
        BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity = this.getMainConsoleTile();
        return tardisConsoleUnitBlockEntity != null ? tardisConsoleUnitBlockEntity.getPos() : this.getEntrancePosition();
    }

    public void updateConsoleTiles() {
        this.updatedConsoleTiles = false;

        this.consoleTiles.forEach((id, tile) -> {
            if (tile == null) return;
            tile.controlsStorage.applyDataFromTardis(this);

            NbtCompound tag = new NbtCompound();
            tag.put("controlsState", tile.controlsStorage.writeNbt(new NbtCompound()));
            tag.put("tardisState", this.writeNbt(new NbtCompound(), this.world.getRegistryManager()));
            tile.tardisStateManager.readNbt(tag.getCompound("tardisState"), this.world.getRegistryManager());

            new TardisConsoleUnitUpdatePacket(tile.getPos(), tag)
                .sendToAll(this.world.getServer());
        });
    }

    // ///////////////////////// //
    // Tardis Door Tiles methods //
    // ///////////////////////// //

    public Map<BlockPos, BaseTardisDoorsBlockEntity> getInteriorDoorsTiles() {
        return this.doorsTiles;
    }

    public void addInteriorDoorsTile(BaseTardisDoorsBlockEntity doorsTile) {
        boolean hasDoorTile = this.doorsTiles.containsKey(doorsTile.getPos());
        this.doorsTiles.put(doorsTile.getPos(), doorsTile);
        if (!hasDoorTile) this.markDirty();
    }

    public void removeInteriorDoorsTile(BaseTardisDoorsBlockEntity doorsTile) {
        boolean hasDoorTile = this.doorsTiles.containsKey(doorsTile.getPos());
        if (!hasDoorTile) return;

        this.doorsTiles.remove(doorsTile.getPos());
        this.markDirty();
    }

    public BaseTardisDoorsBlockEntity getMainInteriorDoorsTile() {
        int size = this.doorsTiles.size();
        return size > 0 ? this.doorsTiles.values().stream().toList().get(size - 1) : null;
    }

    public BlockPos getEntrancePosition() {
        BaseTardisDoorsBlockEntity tardisDoorsBlockEntity = this.getMainInteriorDoorsTile();
        return tardisDoorsBlockEntity != null ? tardisDoorsBlockEntity.getPos() : this.getConsoleRoom().getEntrancePosition();
    }

    public Direction getEntranceFacing() {
        BaseTardisDoorsBlockEntity tardisDoorsBlockEntity = this.getMainInteriorDoorsTile();
        return tardisDoorsBlockEntity != null ? tardisDoorsBlockEntity.getCachedState().get(BaseTardisDoorsBlock.FACING) : Direction.SOUTH;
    }

    public void updateDoorsTiles() {
        this.updatedDoorsTiles = false;

        this.doorsTiles.forEach((blockPos, tile) -> {
            BlockState blockState = tile != null ? tile.getCachedState() : this.world.getBlockState(blockPos);
            if (!(blockState.getBlock() instanceof BaseTardisDoorsBlock)) return;
            this.world.setBlockState(blockPos, blockState.with(BaseTardisDoorsBlock.OPEN, this.isDoorsOpened()), Block.NOTIFY_ALL);
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
        if (!ModCompats.immersivePortals()) return;
        this.getPortalsState().clearRoomEntrancePortals();
        this.getPortalsState().createRoomsEntrancesPortals();
    }

    public void validatePortals() {
        if (!ModCompats.immersivePortals()) return;

        if (!this.isDoorsOpened()) this.getPortalsState().clearEntrancePortals();
        else if (!this.getPortalsState().isEntrancePortalsValid()) this.updateEntrancePortals();

        List<ServerPlayerEntity> players = this.world.getPlayers();
        if (!players.isEmpty() && !this.getPortalsState().isRoomEntrancePortalsValid()) this.updateRoomEntrancePortals();
    }

    // /////////////////// //
    // Tardis Data methods //
    // /////////////////// //

    @SuppressWarnings("UnstableApiUsage")
    public void tick() {
        this.systems.values().forEach(TardisBaseSystem::tick);

        if (this.updatedExterior) this.updateExterior();
        if (this.updatedDoorsTiles) this.updateDoorsTiles();
        if (this.updatedConsoleTiles) this.updateConsoleTiles();

        if (this.world.getTime() % 20 == 0) {
            this.validatePortals();
        }

        if (this.isFuelHarvesting() && this.world.getTime() % 40 == 0 && this.fuelAmount < this.fuelCapacity) {
            this.setFuelAmount(this.fuelAmount + 1);
            this.markConsoleTilesUpdated();
        }
    }

    public void markExteriorUpdated() {
        this.updatedExterior = true;
    }

    public void markDoorsTilesUpdated() {
        this.updatedDoorsTiles = true;
    }

    public void markConsoleTilesUpdated() {
        this.updatedConsoleTiles = true;
    }

    private void addSystem(TardisBaseSystem system) {
        this.systems.put(system.getClass(), system);
    }

    private void bindChunkLoaders() {
        if (this.world == null) return;

        ChunkPos pos = new ChunkPos(0, 0);
        ServerChunkManager chunkManager = this.world.getChunkManager();
        chunkManager.addTicket(CHUNK_TICKET_TYPE, pos, 3, pos);
    }

    private void unbindChunkLoaders() {
        if (this.world == null) return;

        ChunkPos pos = new ChunkPos(0, 0);
        ServerChunkManager chunkManager = this.world.getChunkManager();
        chunkManager.removeTicket(CHUNK_TICKET_TYPE, pos, 3, pos);
    }

    private void updateExterior() {
        this.updatedExterior = false;

        ServerWorld exteriorWorld = this.getExteriorWorld();
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

            BlockState exteriorUpBlockState = exteriorWorld.getBlockState(exteriorBlockPos.up());
            if (exteriorUpBlockState.getBlock() instanceof BaseTardisExteriorBlock) {
                exteriorBlockState = exteriorBlockState.with(BaseTardisExteriorBlock.HALF, DoubleBlockHalf.UPPER);
                exteriorBlockState = exteriorBlockState.with(BaseTardisExteriorBlock.WATERLOGGED, exteriorUpBlockState.getFluidState().isIn(FluidTags.WATER));
                exteriorWorld.setBlockState(exteriorBlockPos.up(), exteriorBlockState, Block.NOTIFY_ALL);
            }
        }
    }
}

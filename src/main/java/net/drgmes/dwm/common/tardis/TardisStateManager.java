package net.drgmes.dwm.common.tardis;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.BaseTardisConsoleBlockEntity;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlockEntity;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.common.tardis.consoles.controls.ETardisConsoleControlRole;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.ITardisSystem;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.items.tardissystem.TardisSystemItem;
import net.drgmes.dwm.network.TardisConsoleRemoteCallablePackets;
import net.drgmes.dwm.network.TardisExteriorRemoteCallablePackets;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.types.IMixinPortal;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.PersistentState;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.util.*;

public class TardisStateManager extends PersistentState {
    public static final int SYSTEM_COMPONENTS_CONTAINER_SIZE = 14;
    public static final int BATTERY_COMPONENTS_CONTAINER_SIZE = 0;
    public static final int UPGRADE_COMPONENTS_CONTAINER_SIZE = 0;

    private static final Direction BASE_ENTRANCE_FACING = Direction.SOUTH;
    private static final BlockPos BASE_ENTRANCE_POSITION = TardisHelper.TARDIS_POS.up(7).south(1).east(18).toImmutable();

    private final Map<Class<? extends ITardisSystem>, ITardisSystem> systems = new HashMap<>();
    private final List<BaseTardisDoorsBlockEntity> doorTiles = new ArrayList<>();
    private final List<BaseTardisConsoleBlockEntity> consoleTiles = new ArrayList<>();

    private DefaultedList<ItemStack> systemComponents = DefaultedList.ofSize(SYSTEM_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);
    private DefaultedList<ItemStack> batteryComponents = DefaultedList.ofSize(BATTERY_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);
    private DefaultedList<ItemStack> upgradeComponents = DefaultedList.ofSize(UPGRADE_COMPONENTS_CONTAINER_SIZE, ItemStack.EMPTY);

    private ServerWorld world;
    private UUID owner;

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

    private boolean doorsLocked = false;
    private boolean doorsOpened = false;
    private boolean lightEnabled = false;
    private boolean shieldsEnabled = false;
    private boolean energyArtronHarvesting = false;
    private boolean energyForgeHarvesting = false;

    private int energyArtron = 0;
    private int energyForge = 0;
    private int xyzStep = 1;

    public TardisStateManager(ServerWorld world) {
        this.setWorld(world);

        this.addSystem(new TardisSystemMaterialization(this));
        this.addSystem(new TardisSystemFlight(this));
        this.addSystem(new TardisSystemShields(this));
    }

    public static Optional<TardisStateManager> get(ServerWorld world) {
        if (world == null) return Optional.empty();

        TardisStateManager tardisStateManager = world.getPersistentStateManager().getOrCreate(
            (tag) -> TardisStateManager.createFromNbt(world, tag),
            () -> new TardisStateManager(world),
            DWM.LOCS.TARDIS.getPath()
        );

        if (tardisStateManager != null) tardisStateManager.setWorld(world);
        return Optional.ofNullable(tardisStateManager);
    }

    public static TardisStateManager createFromNbt(ServerWorld world, NbtCompound tag) {
        TardisStateManager tardisStateManager = new TardisStateManager(world);
        tardisStateManager.readNbt(tag);
        return tardisStateManager;
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if (this.owner != null) tag.putUuid("owner", this.owner);

        if (this.prevExteriorDimension != null) tag.putString("prevExteriorDimension", this.prevExteriorDimension.getValue().toString());
        if (this.currExteriorDimension != null) tag.putString("currExteriorDimension", this.currExteriorDimension.getValue().toString());
        if (this.destExteriorDimension != null) tag.putString("destExteriorDimension", this.destExteriorDimension.getValue().toString());

        if (this.prevExteriorFacing != null) tag.putString("prevExteriorFacing", this.prevExteriorFacing.getName());
        if (this.currExteriorFacing != null) tag.putString("currExteriorFacing", this.currExteriorFacing.getName());
        if (this.destExteriorFacing != null) tag.putString("destExteriorFacing", this.destExteriorFacing.getName());

        if (this.prevExteriorPosition != null) {
            tag.putInt("prevExteriorPositionX", this.prevExteriorPosition.getX());
            tag.putInt("prevExteriorPositionY", this.prevExteriorPosition.getY());
            tag.putInt("prevExteriorPositionZ", this.prevExteriorPosition.getZ());
        }

        if (this.currExteriorPosition != null) {
            tag.putInt("currExteriorPositionX", this.currExteriorPosition.getX());
            tag.putInt("currExteriorPositionY", this.currExteriorPosition.getY());
            tag.putInt("currExteriorPositionZ", this.currExteriorPosition.getZ());
        }

        if (this.destExteriorPosition != null) {
            tag.putInt("destExteriorPositionX", this.destExteriorPosition.getX());
            tag.putInt("destExteriorPositionY", this.destExteriorPosition.getY());
            tag.putInt("destExteriorPositionZ", this.destExteriorPosition.getZ());
        }

        tag.putInt("energyArtron", this.energyArtron);
        tag.putInt("energyForge", this.energyForge);
        tag.putInt("xyzStep", this.xyzStep);

        tag.putBoolean("doorsLocked", this.doorsLocked);
        tag.putBoolean("doorsOpened", this.doorsOpened);
        tag.putBoolean("lightEnabled", this.lightEnabled);
        tag.putBoolean("shieldsEnabled", this.shieldsEnabled);
        tag.putBoolean("energyArtronHarvesting", this.energyArtronHarvesting);
        tag.putBoolean("energyForgeHarvesting", this.energyForgeHarvesting);

        NbtCompound tdTagSystemComponents = new NbtCompound();
        Inventories.writeNbt(tdTagSystemComponents, this.systemComponents);
        tag.put("tdTagSystemComponents", tdTagSystemComponents);

        NbtCompound tdTagBatteryComponents = new NbtCompound();
        Inventories.writeNbt(tdTagBatteryComponents, this.batteryComponents);
        tag.put("tdTagBatteryComponents", tdTagBatteryComponents);

        NbtCompound tdTagUpgradeComponents = new NbtCompound();
        Inventories.writeNbt(tdTagUpgradeComponents, this.upgradeComponents);
        tag.put("tdTagUpgradeComponents", tdTagUpgradeComponents);

        this.systems.values().forEach((system) -> {
            tag.put(system.getClass().getName(), system.save());
        });

        return tag;
    }

    public void readNbt(NbtCompound tag) {
        if (tag.contains("owner")) this.owner = tag.getUuid("owner");

        if (tag.contains("prevExteriorDimension")) this.prevExteriorDimension = DimensionHelper.getWorldKey(tag.getString("prevExteriorDimension"));
        if (tag.contains("currExteriorDimension")) this.currExteriorDimension = DimensionHelper.getWorldKey(tag.getString("currExteriorDimension"));
        if (tag.contains("destExteriorDimension")) this.destExteriorDimension = DimensionHelper.getWorldKey(tag.getString("destExteriorDimension"));

        if (tag.contains("prevExteriorPositionX")) this.prevExteriorPosition = getBlockPosByKey(tag, "prevExteriorPosition");
        if (tag.contains("currExteriorPositionX")) this.currExteriorPosition = getBlockPosByKey(tag, "currExteriorPosition");
        if (tag.contains("destExteriorPositionX")) this.destExteriorPosition = getBlockPosByKey(tag, "destExteriorPosition");

        if (tag.contains("prevExteriorFacing")) this.prevExteriorFacing = getDirectionByKey(tag, "prevExteriorFacing");
        if (tag.contains("currExteriorFacing")) this.currExteriorFacing = getDirectionByKey(tag, "currExteriorFacing");
        if (tag.contains("destExteriorFacing")) this.destExteriorFacing = getDirectionByKey(tag, "destExteriorFacing");

        this.energyArtron = tag.getInt("energyArtron");
        this.energyForge = tag.getInt("energyForge");
        this.xyzStep = tag.getInt("xyzStep");

        this.doorsLocked = tag.getBoolean("doorsLocked");
        this.doorsOpened = tag.getBoolean("doorsOpened");
        this.lightEnabled = tag.getBoolean("lightEnabled");
        this.shieldsEnabled = tag.getBoolean("shieldsEnabled");
        this.energyArtronHarvesting = tag.getBoolean("energyArtronHarvesting");
        this.energyForgeHarvesting = tag.getBoolean("energyForgeHarvesting");

        this.systemComponents = DefaultedList.ofSize(this.systemComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagSystemComponents"), this.systemComponents);

        this.batteryComponents = DefaultedList.ofSize(this.batteryComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagBatteryComponents"), this.batteryComponents);

        this.upgradeComponents = DefaultedList.ofSize(this.upgradeComponents.size(), ItemStack.EMPTY);
        Inventories.readNbt(tag.getCompound("tdTagUpgradeComponents"), this.upgradeComponents);

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

    public boolean isDoorsLocked() {
        return this.doorsLocked;
    }

    public boolean setDoorsLockState(boolean flag, PlayerEntity player) {
        if (player != null && this.getOwner() != null && !player.getUuid().equals(this.getOwner())) return false;
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

        if (flag) ModSounds.playTardisDoorsOpenSound(this.world, this.getEntrancePosition());
        else ModSounds.playTardisDoorsCloseSound(this.world, this.getEntrancePosition());

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

    public boolean isEnergyArtronHarvesting() {
        return this.energyArtronHarvesting;
    }

    public void setEnergyArtronHarvesting(boolean flag) {
        this.energyArtronHarvesting = flag;
        this.markDirty();
    }

    public int getEnergyArtron() {
        return this.energyArtron;
    }

    public void setEnergyArtron(int value) {
        this.energyArtron = value;
        this.markDirty();
    }

    public boolean isEnergyForgeHarvesting() {
        return this.energyForgeHarvesting;
    }

    public void setEnergyForgeHarvesting(boolean flag) {
        this.energyForgeHarvesting = flag;
        this.markDirty();
    }

    public int getEnergyForge() {
        return this.energyForge;
    }

    public void setEnergyForge(int value) {
        this.energyForge = value;
        this.markDirty();
    }

    public int getXYZStep() {
        return this.xyzStep;
    }

    public void setXYZStep(int value) {
        this.xyzStep = value;
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
        return tardisDoorsBlockEntity != null ? tardisDoorsBlockEntity.getPos() : BASE_ENTRANCE_POSITION;
    }

    public Direction getEntranceFacing() {
        BaseTardisDoorsBlockEntity tardisDoorsBlockEntity = this.getMainInteriorDoorTile();
        return tardisDoorsBlockEntity != null ? tardisDoorsBlockEntity.getCachedState().get(BaseTardisDoorsBlock.FACING) : BASE_ENTRANCE_FACING;
    }

    public void updateDoorsTiles() {
        this.doorTiles.forEach((tile) -> {
            this.world.setBlockState(tile.getPos(), tile.getCachedState().with(BaseTardisDoorsBlock.OPEN, this.isDoorsOpened()), 3);
        });
    }

    public void updateEntrancePortals() {
        this.clearEntrancePortals();
        if (!this.isDoorsOpened()) return;

        double offset = -0.5;
        Direction originFacing = this.getEntranceFacing();
        Direction destinationFacing = this.getCurrentExteriorFacing();
        Vec3d originPos = Vec3d.ofBottomCenter(this.getEntrancePosition().up()).withBias(originFacing, offset + 0.0275);
        Vec3d destinationPos = Vec3d.ofBottomCenter(this.getCurrentExteriorRelativePosition().up()).withBias(destinationFacing, offset);

        DQuaternion dQuaternion = DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), destinationFacing == Direction.NORTH || destinationFacing == Direction.SOUTH ? 180 : 0);
        dQuaternion = dQuaternion.combine(DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), originFacing.asRotation()));
        dQuaternion = dQuaternion.combine(DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), destinationFacing.asRotation()));

        this.portalFromTardis = Portal.entityType.create(this.world);
        this.portalFromTardis.setOriginPos(originPos);
        this.portalFromTardis.setDestination(destinationPos);
        this.portalFromTardis.setDestinationDimension(this.getCurrentExteriorDimension());
        this.portalFromTardis.setRotationTransformation(dQuaternion.toMcQuaternion());
        this.portalFromTardis.setOrientationAndSize(new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), 1, 2);
        PortalManipulation.rotatePortalBody(this.portalFromTardis, DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), originFacing.asRotation()).toMcQuaternion());

        this.portalToTardis = PortalAPI.createReversePortal(this.portalFromTardis);

        ((IMixinPortal) this.portalFromTardis).setTardisId(DimensionHelper.getWorldId(this.world));
        ((IMixinPortal) this.portalToTardis).setTardisId(DimensionHelper.getWorldId(this.world));
        this.portalFromTardis.world.spawnEntity(this.portalFromTardis);
        this.portalToTardis.world.spawnEntity(this.portalToTardis);
    }

    public void validateEntrancePortals() {
        if (!this.isDoorsOpened()) this.clearEntrancePortals();
        else if (this.portalFromTardis == null || this.portalFromTardis.isRemoved() || this.portalToTardis == null || this.portalToTardis.isRemoved()) {
            this.updateEntrancePortals();
        }
    }

    public void clearEntrancePortals() {
        if (this.portalFromTardis != null) this.portalFromTardis.discard();
        if (this.portalToTardis != null) this.portalToTardis.discard();
    }

    public boolean checkIsPortalValid(Portal portal) {
        return (this.portalFromTardis != null && this.portalFromTardis.equals(portal)) || (this.portalToTardis != null && this.portalToTardis.equals(portal));
    }

    // //////////////////////////// //
    // Tardis Console Tiles methods //
    // //////////////////////////// //

    public List<BaseTardisConsoleBlockEntity> getConsoleTiles() {
        return this.consoleTiles;
    }

    public BaseTardisConsoleBlockEntity getMainConsoleTile() {
        int size = this.consoleTiles.size();
        return size > 0 ? this.consoleTiles.get(size - 1) : null;
    }

    public BlockPos getMainConsolePosition() {
        BaseTardisConsoleBlockEntity tardisConsoleBlockEntity = this.getMainConsoleTile();
        return tardisConsoleBlockEntity != null ? tardisConsoleBlockEntity.getPos() : this.getEntrancePosition();
    }

    public void updateConsoleTiles() {
        this.consoleTiles.forEach((tile) -> {
            this.applyDataToControlsStorage(tile.controlsStorage);
            tile.sendControlsUpdatePacket(this.world);

            NbtCompound tag = new NbtCompound();
            this.writeNbt(tag);
            tile.readNbt(tag);
            tile.markDirty();

            PacketHelper.sendToClient(
                TardisConsoleRemoteCallablePackets.class,
                "updateTardisConsoleData",
                this.world.getWorldChunk(tile.getPos()),
                tile.getPos(), tag
            );
        });
    }

    // /////////////////////////// //
    // Tardis Storage Data methods //
    // /////////////////////////// //

    public void applyDataToControlsStorage(TardisConsoleControlsStorage controlsStorage) {
        controlsStorage.values.put(ETardisConsoleControlRole.STARTER, this.getSystem(TardisSystemFlight.class).inProgress());
        controlsStorage.values.put(ETardisConsoleControlRole.MATERIALIZATION, this.getSystem(TardisSystemMaterialization.class).isMaterialized());
        controlsStorage.values.put(ETardisConsoleControlRole.SAFE_DIRECTION, this.getSystem(TardisSystemMaterialization.class).safeDirection.ordinal());
        controlsStorage.values.put(ETardisConsoleControlRole.SHIELDS, this.getSystem(TardisSystemShields.class).inProgress());
        controlsStorage.values.put(ETardisConsoleControlRole.ENERGY_ARTRON_HARVESTING, this.isEnergyArtronHarvesting());
        controlsStorage.values.put(ETardisConsoleControlRole.ENERGY_FORGE_HARVESTING, this.isEnergyForgeHarvesting());
        controlsStorage.values.put(ETardisConsoleControlRole.LIGHT, this.isLightEnabled());
        controlsStorage.values.put(ETardisConsoleControlRole.DOORS, this.isDoorsOpened());
        controlsStorage.values.put(ETardisConsoleControlRole.FACING, switch (this.getDestinationExteriorFacing()) {
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
        boolean starter = (boolean) controlsStorage.get(ETardisConsoleControlRole.STARTER);
        boolean handbrake = (boolean) controlsStorage.get(ETardisConsoleControlRole.HANDBRAKE);
        if (this.getSystem(TardisSystemFlight.class).isEnabled()) {
            this.getSystem(TardisSystemFlight.class).setFlight(!handbrake && starter);
            isInFlight = this.getSystem(TardisSystemFlight.class).inProgress();
        }
        else {
            controlsStorage.values.put(ETardisConsoleControlRole.STARTER, isInFlight);
            if (isInFlight && !starter) ModSounds.playTardisFailSound(this.getWorld(), this.getMainConsolePosition());
            else if (!isInFlight && starter) ModSounds.playTardisFailSound(this.getWorld(), this.getMainConsolePosition());
        }

        // Materialization
        boolean materialization = (boolean) controlsStorage.get(ETardisConsoleControlRole.MATERIALIZATION);
        if (this.getSystem(TardisSystemMaterialization.class).isEnabled()) {
            this.getSystem(TardisSystemMaterialization.class).setSafeDirection(Math.abs((int) controlsStorage.get(ETardisConsoleControlRole.SAFE_DIRECTION)));
            this.getSystem(TardisSystemMaterialization.class).setMaterializationState(materialization);
            isMaterialized = this.getSystem(TardisSystemMaterialization.class).isMaterialized();
        }
        else {
            controlsStorage.values.put(ETardisConsoleControlRole.STARTER, isInFlight);
            controlsStorage.values.put(ETardisConsoleControlRole.MATERIALIZATION, isMaterialized);
            if (isMaterialized && !materialization) ModSounds.playTardisFailSound(this.getWorld(), this.getMainConsolePosition());
            else if (!isMaterialized && materialization) ModSounds.playTardisFailSound(this.getWorld(), this.getMainConsolePosition());
        }

        // Only if Tardis is not in flight (and could be when dematerialized)
        if (!isInFlight) {
            // Facing
            int facing = (int) controlsStorage.get(ETardisConsoleControlRole.FACING);
            this.destExteriorFacing = switch (facing >= 0 ? facing : ETardisConsoleControlRole.FACING.maxIntValue + facing) {
                default -> Direction.NORTH;
                case 1 -> Direction.EAST;
                case 2 -> Direction.SOUTH;
                case 3 -> Direction.WEST;
            };

            // X Set
            int xSet = (int) controlsStorage.get(ETardisConsoleControlRole.XSET);
            if (xSet != 0) this.destExteriorPosition = xSet > 0 ? this.destExteriorPosition.east(this.xyzStep) : this.destExteriorPosition.west(this.xyzStep);

            // Y Set
            int ySet = (int) controlsStorage.get(ETardisConsoleControlRole.YSET);
            if (ySet != 0) this.destExteriorPosition = ySet > 0 ? this.destExteriorPosition.up(this.xyzStep) : this.destExteriorPosition.down(this.xyzStep);

            // Z Set
            int zSet = (int) controlsStorage.get(ETardisConsoleControlRole.ZSET);
            if (zSet != 0) this.destExteriorPosition = zSet > 0 ? this.destExteriorPosition.south(this.xyzStep) : this.destExteriorPosition.north(this.xyzStep);

            // XYZ Step
            int xyzStep = (int) controlsStorage.get(ETardisConsoleControlRole.XYZSTEP);
            if (xyzStep != 0) this.xyzStep = Math.max(1, Math.min(10000, (int) Math.round(this.xyzStep * (xyzStep > 0 ? 10 : 0.1))));

            // Randomizer
            if ((int) controlsStorage.get(ETardisConsoleControlRole.RANDOMIZER) != 0) {
                boolean facingRandom = Math.random() * 10 > 5;

                if (facingRandom) this.destExteriorPosition = this.destExteriorPosition.east((int) Math.round(Math.random() * 10 * this.xyzStep));
                else this.destExteriorPosition = this.destExteriorPosition.west((int) Math.round(Math.random() * 10 * this.xyzStep));

                if (facingRandom) this.destExteriorPosition = this.destExteriorPosition.south((int) Math.round(Math.random() * 10 * this.xyzStep));
                else this.destExteriorPosition = this.destExteriorPosition.north((int) Math.round(Math.random() * 10 * this.xyzStep));
            }

            // Dimension
            int dimPrev = (int) controlsStorage.get(ETardisConsoleControlRole.DIM_PREV);
            int dimNext = (int) controlsStorage.get(ETardisConsoleControlRole.DIM_NEXT);
            if (dimPrev != 0 || dimNext != 0) {
                Iterable<ServerWorld> worlds = MiscHelper.getServer().getWorlds();
                List<RegistryKey<World>> worldKeys = new ArrayList<>();

                worlds.forEach((world) -> {
                    if (TardisHelper.isTardisDimension(world)) return;
                    if (world.getRegistryKey() == this.world.getRegistryKey()) return;
                    worldKeys.add(world.getRegistryKey());
                });

                int index = worldKeys.contains(this.destExteriorDimension) ? worldKeys.indexOf(this.destExteriorDimension) : 0;
                index = index + (dimPrev != 0 ? -1 : 1);
                index %= worldKeys.size();
                index = index < 0 ? worldKeys.size() - 1 : index;

                this.destExteriorDimension = worldKeys.get(index);
            }

            // Reset to Prev
            int resetToPrev = (int) controlsStorage.get(ETardisConsoleControlRole.RESET_TO_PREV);
            if (resetToPrev != 0) this.destExteriorDimension = this.getPreviousExteriorDimension();
            if (resetToPrev != 0) this.destExteriorFacing = this.getPreviousExteriorFacing();
            if (resetToPrev != 0) this.destExteriorPosition = this.getPreviousExteriorPosition();

            // Reset to Current
            int resetToCurr = (int) controlsStorage.get(ETardisConsoleControlRole.RESET_TO_CURR);
            if (resetToCurr != 0) this.destExteriorDimension = this.getCurrentExteriorDimension();
            if (resetToCurr != 0) this.destExteriorFacing = this.getCurrentExteriorFacing();
            if (resetToCurr != 0) this.destExteriorPosition = this.getCurrentExteriorPosition();
        }

        // Only if Tardis materialized
        if (isMaterialized) {
            // Shields
            boolean shields = (boolean) controlsStorage.get(ETardisConsoleControlRole.SHIELDS);
            if (this.getSystem(TardisSystemShields.class).isEnabled()) {
                this.getSystem(TardisSystemShields.class).setState(shields);
            }
            else {
                controlsStorage.values.put(ETardisConsoleControlRole.SHIELDS, false);
                if (shields) ModSounds.playTardisFailSound(this.getWorld(), this.getMainConsolePosition());
            }

            this.setDoorsOpenState((boolean) controlsStorage.get(ETardisConsoleControlRole.DOORS));
            this.setLightState((boolean) controlsStorage.get(ETardisConsoleControlRole.LIGHT));
            this.setEnergyArtronHarvesting((boolean) controlsStorage.get(ETardisConsoleControlRole.ENERGY_ARTRON_HARVESTING));
            this.setEnergyForgeHarvesting((boolean) controlsStorage.get(ETardisConsoleControlRole.ENERGY_FORGE_HARVESTING));
        }

        this.updateConsoleTiles();
        this.markDirty();
    }

    public void tick() {
        if (!this.isValid()) return;

        this.systems.values().forEach(ITardisSystem::tick);
        this.validateEntrancePortals();
    }

    private static BlockPos getBlockPosByKey(NbtCompound tag, String key) {
        return new BlockPos(tag.getInt(key + "X"), tag.getInt(key + "Y"), tag.getInt(key + "Z"));
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

        if (exteriorBlockState.getBlock() instanceof BaseTardisExteriorBlock) {
            if (exteriorBlockState.get(BaseTardisExteriorBlock.OPEN) != this.isDoorsOpened()) {
                if (this.isDoorsOpened()) ModSounds.playTardisDoorsOpenSound(exteriorWorld, exteriorBlockPos);
                else ModSounds.playTardisDoorsCloseSound(exteriorWorld, exteriorBlockPos);
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

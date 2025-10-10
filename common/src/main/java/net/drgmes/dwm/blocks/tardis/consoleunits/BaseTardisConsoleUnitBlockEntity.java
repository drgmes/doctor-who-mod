package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consoleunits.TardisConsoleUnitEntry;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleUnitControlEntry;
import net.drgmes.dwm.common.tardis.systems.TardisSystemConsoleRoom;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.enums.TardisConsoleUnitControlRole;
import net.drgmes.dwm.enums.TardisConsoleUnitControlType;
import net.drgmes.dwm.enums.TardisConsoleUnitControlValueType;
import net.drgmes.dwm.items.sonicdevices.SonicScrewdriverItem;
import net.drgmes.dwm.network.client.*;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.Inventories;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapBannerMarker;
import net.minecraft.item.map.MapState;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.packet.s2c.play.BlockEntityUpdateS2CPacket;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;

public abstract class BaseTardisConsoleUnitBlockEntity extends BlockEntity {
    public static final int MONITOR_PAGES_LENGTH = 2;

    public final TardisStateManager tardisStateManager = new TardisStateManager();
    public TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
    public ItemStack sonicScrewdriverItemStack = ItemStack.EMPTY;

    public int monitorPage = 0;
    public float tick = 0;

    private final ArrayList<TardisConsoleControlEntity> controls = new ArrayList<>();
    private boolean inited;

    public BaseTardisConsoleUnitBlockEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
    }

    @Override
    public BlockEntityUpdateS2CPacket toUpdatePacket() {
        return BlockEntityUpdateS2CPacket.create(this);
    }

    @Override
    public NbtCompound toInitialChunkDataNbt(RegistryWrapper.WrapperLookup registryLookup) {
        return createNbt(registryLookup);
    }

    @Override
    public void readNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        super.readNbt(tag, registryLookup);

        if (tag.contains("controlsState")) this.controlsStorage.readNbt(tag.getCompound("controlsState"));
        if (tag.contains("tardisState")) this.tardisStateManager.readNbt(tag.getCompound("tardisState"), registryLookup);
        if (tag.contains("monitorPage")) this.monitorPage = tag.getInt("monitorPage") % MONITOR_PAGES_LENGTH;

        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
        if (tag.contains("Items", 9)) {
            Inventories.readNbt(tag, itemStacks, registryLookup);
            this.sonicScrewdriverItemStack = itemStacks.getFirst();
        }
    }

    @Override
    protected void writeNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        if (!this.inited) this.init();
        super.writeNbt(tag, registryLookup);

        tag.put("controlsState", this.controlsStorage.writeNbt(new NbtCompound()));
        tag.put("tardisState", this.tardisStateManager.writeNbt(new NbtCompound(), registryLookup));
        tag.putInt("monitorPage", this.monitorPage % MONITOR_PAGES_LENGTH);

        Inventories.writeNbt(tag, DefaultedList.ofSize(1, this.sonicScrewdriverItemStack), registryLookup);
    }

    @Override
    public void markRemoved() {
        this.removeControls();
        super.markRemoved();
    }

    public TardisConsoleUnitEntry getConsoleUnitType() {
        return ((BaseTardisConsoleUnitBlock<?>) this.getCachedState().getBlock()).consoleUnitType;
    }

    public NbtCompound getSavedTardisTag(PlayerEntity player) {
        return this.tardisStateManager.writeNbt(new NbtCompound(), player.getRegistryManager());
    }

    public void tick() {
        TardisSystemMaterialization materializationSystem = this.tardisStateManager.getSystem(TardisSystemMaterialization.class);
        TardisSystemFlight flightSystem = this.tardisStateManager.getSystem(TardisSystemFlight.class);

        if (flightSystem.inProgress() || materializationSystem.inProgress()) {
            this.tick++;
            this.tick %= 60;
        }

        this.animateControls();
    }

    public void init() {
        if (this.inited) return;
        this.inited = true;

        if (this.getWorld() instanceof ServerWorld) {
            this.createControls();
            this.updateTardisData();
        }
    }

    public void remove() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            this.removeControls();

            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                tardis.removeConsoleTile(this);
            });
        }
    }

    public void useControl(TardisConsoleUnitControlRole controlRole, Hand hand, Entity entity) {
        if (!(this.getWorld() instanceof ServerWorld serverWorld) || !(entity instanceof ServerPlayerEntity player)) return;

        Object initialValue = this.controlsStorage.get(controlRole);
        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(serverWorld);

        switch (controlRole) {
            case MONITOR -> {
                if (hand != Hand.OFF_HAND) return;
                if (tardisHolder.isEmpty() || this.throwNotifyIfLocked(tardisHolder.get(), player)) return;

                this.updateTardisData();

                controlRole.playSound(this.getWorld(), this.getPos());
                this.sendMonitorOpenPacket(player, tardisHolder.get());
            }

            case TELEPATHIC_INTERFACE -> {
                if (hand != Hand.OFF_HAND) return;
                if (tardisHolder.isEmpty() || this.throwNotifyIfLocked(tardisHolder.get(), player)) return;

                TardisStateManager tardis = tardisHolder.get();
                TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
                TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);

                if (!flightSystem.isEnabled()) {
                    player.sendMessage(DWM.TEXTS.FLIGHT_SYSTEM_NOT_INSTALLED, true);
                    return;
                }

                if (flightSystem.inProgress()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_LANDED, true);
                    return;
                }

                if (materializationSystem.inProgress()) {
                    player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_MATERIALIZED, true);
                    return;
                }

                ItemStack mainHandItemStack = player.getMainHandStack();
                ItemStack offHandItemStack = player.getOffHandStack();

                if (mainHandItemStack.getItem() instanceof FilledMapItem || offHandItemStack.getItem() instanceof FilledMapItem) {
                    ItemStack itemStack = mainHandItemStack.getItem() instanceof FilledMapItem ? mainHandItemStack : offHandItemStack;
                    MapState mapData = FilledMapItem.getMapState(itemStack, serverWorld);
                    if (mapData == null) return;

                    if (mapData.dimension == tardis.getWorld().getRegistryKey()) return;

                    BlockPos destExteriorPosition = tardis.getDestinationExteriorPosition();
                    BlockPos blockPos = new BlockPos(mapData.centerX, destExteriorPosition.getY(), mapData.centerZ);

                    if (mapData.getBanners().size() > 1) {
                        this.sendTelepathicInterfaceMapBannersOpenPacket(player, mapData);
                        return;
                    }
                    else if (mapData.getBanners().size() == 1 && mapData.getBanners().toArray()[0] instanceof MapBannerMarker banner) {
                        String color = banner.color().getName().toUpperCase().replace("_", " ");
                        player.sendMessage(DWM.TEXTS.TELEPATHIC_INTERFACE_MAP_BANNER_LOADED.apply(color), true);
                        blockPos = banner.pos();
                    }
                    else {
                        player.sendMessage(DWM.TEXTS.TELEPATHIC_INTERFACE_MAP_COORDS_LOADED, true);
                    }

                    tardis.setDestinationDimension(mapData.dimension);
                    tardis.setDestinationPosition(blockPos);
                    tardis.markConsoleTilesUpdated();
                    return;
                }

                controlRole.playSound(this.getWorld(), this.getPos());
                this.sendTelepathicInterfaceLocationsOpenPacket(player);
            }

            case SONIC_SCREWDRIVER_SLOT -> {
                if (hand != Hand.OFF_HAND) return;
                boolean isChanged = false;

                if (this.sonicScrewdriverItemStack.isEmpty()) {
                    ItemStack mainHandItem = player.getMainHandStack();
                    ItemStack offHandItem = player.getOffHandStack();

                    if (mainHandItem.getItem() instanceof SonicScrewdriverItem) {
                        this.sonicScrewdriverItemStack = mainHandItem;
                        ModSounds.playSonicScrewdriverPutSound(player.getWorld(), player.getBlockPos());
                        player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                        isChanged = true;
                    }
                    else if (offHandItem.getItem() instanceof SonicScrewdriverItem) {
                        this.sonicScrewdriverItemStack = offHandItem;
                        ModSounds.playSonicScrewdriverPutSound(player.getWorld(), player.getBlockPos());
                        player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                        isChanged = true;
                    }
                }
                else if (player.getMainHandStack().isEmpty()) {
                    ModSounds.playSonicScrewdriverPickupSound(player.getWorld(), player.getBlockPos());
                    player.setStackInHand(Hand.MAIN_HAND, this.sonicScrewdriverItemStack);
                    this.sonicScrewdriverItemStack = ItemStack.EMPTY;
                    isChanged = true;
                }
                else if (player.getInventory().insertStack(this.sonicScrewdriverItemStack)) {
                    ModSounds.playSonicScrewdriverPickupSound(player.getWorld(), player.getBlockPos());
                    this.sonicScrewdriverItemStack = ItemStack.EMPTY;
                    isChanged = true;
                }

                if (isChanged) {
                    String currentTardisId = DimensionHelper.getWorldId(player.getWorld());
                    String tardisId = SonicDevice.getTardisId(this.sonicScrewdriverItemStack);

                    if (tardisId == null || tardisId.isEmpty()) {
                        SonicDevice.setTardisId(this.sonicScrewdriverItemStack, serverWorld);
                        tardisId = currentTardisId;
                    }

                    SonicDevice.loadDiscoveredLocations(this.sonicScrewdriverItemStack, player, tardisId, currentTardisId);
                    this.sendSonicScrewdriverSlotUpdatePacket(serverWorld);
                    this.markDirty();
                }
            }

            default -> {
                boolean isUpdated = this.controlsStorage.update(controlRole, hand);

                switch (controlRole) {
                    case STARTER -> {
                        if (!isUpdated && tardisHolder.isPresent() && !tardisHolder.get().isHandbrakeLocked()) {
                            ModSounds.playSound(tardisHolder.get().getWorld(), tardisHolder.get().getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
                            if ((boolean) initialValue) player.sendMessage(DWM.TEXTS.TARDIS_ALREADY_IN_FLIGHT, true);
                            else player.sendMessage(DWM.TEXTS.TARDIS_ALREADY_LANDED, true);
                        }
                    }

                    case MATERIALIZATION -> {
                        if (!isUpdated && tardisHolder.isPresent() && !tardisHolder.get().isHandbrakeLocked()) {
                            ModSounds.playSound(tardisHolder.get().getWorld(), tardisHolder.get().getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
                            if ((boolean) initialValue) player.sendMessage(DWM.TEXTS.TARDIS_ALREADY_MATERIALIZED, true);
                            else player.sendMessage(DWM.TEXTS.TARDIS_ALREADY_DEMATERIALIZED, true);
                        }
                    }

                    case HANDBRAKE -> {
                        if (isUpdated && tardisHolder.isEmpty()) {
                            if ((boolean) this.controlsStorage.get(controlRole)) ModSounds.playTardisHandbrakeOnSound(serverWorld, player.getBlockPos());
                            else ModSounds.playTardisHandbrakeOffSound(serverWorld, player.getBlockPos());
                        }
                    }

                    case MONITOR_PAGE_NEXT -> {
                        if ((int) this.controlsStorage.get(controlRole) != 0) {
                            this.monitorPage = (this.monitorPage + 1) % MONITOR_PAGES_LENGTH;
                            controlRole.playSound(this.getWorld(), this.getPos());
                            this.sendMonitorUpdatePacket(serverWorld);
                            this.markDirty();
                        }
                    }

                    case MONITOR_PAGE_PREV -> {
                        if ((int) this.controlsStorage.get(controlRole) != 0) {
                            this.monitorPage = this.monitorPage < 1 ? MONITOR_PAGES_LENGTH - 1 : this.monitorPage - 1;
                            controlRole.playSound(this.getWorld(), this.getPos());
                            this.sendMonitorUpdatePacket(serverWorld);
                            this.markDirty();
                        }
                    }
                }

                if (tardisHolder.isEmpty() || this.throwNotifyIfLocked(tardisHolder.get(), player)) {
                    this.sendControlsUpdatePacket(serverWorld);
                    return;
                }

                this.controlsStorage.applyDataToTardis(tardisHolder.get(), controlRole, player);
            }
        }
    }

    private void updateTardisData() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) return;

        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(serverWorld);
        if (tardisHolder.isEmpty()) return;

        TardisStateManager tardis = tardisHolder.get();
        tardis.addConsoleTile(this);

        NbtCompound tag = new NbtCompound();
        this.controlsStorage.applyDataFromTardis(tardis);
        tag.put("controlsState", this.controlsStorage.writeNbt(new NbtCompound()));
        tag.put("tardisState", tardis.writeNbt(new NbtCompound(), serverWorld.getRegistryManager()));
        this.tardisStateManager.readNbt(tag.getCompound("tardisState"), serverWorld.getRegistryManager());

        new TardisConsoleUnitUpdatePacket(this.getPos(), tag)
            .sendToAll(serverWorld.getServer());
    }

    private void createControls() {
        if (!(this.getWorld() instanceof ServerWorld serverWorld)) return;

        Map<TardisConsoleUnitControlRole, TardisConsoleUnitControlEntry> controlEntries = this.getConsoleUnitType().controlEntries;
        if (this.controls.size() == controlEntries.size()) return;
        this.removeControls();

        for (TardisConsoleUnitControlEntry controlEntry : controlEntries.values()) {
            this.controls.add(controlEntry.createEntity(this, serverWorld, this.getPos(), this.getCachedState()));
        }
    }

    private void animateControls() {
        boolean isChanged = false;

        for (TardisConsoleUnitControlEntry controlEntry : this.getConsoleUnitType().controlEntries.values()) {
            if (controlEntry.role.type != TardisConsoleUnitControlValueType.ANIMATION && controlEntry.role.type != TardisConsoleUnitControlValueType.ANIMATION_DIRECT) {
                continue;
            }

            int value = (int) this.controlsStorage.get(controlEntry.role);
            int direction = Integer.compare(value, 0);

            this.controlsStorage.values.put(controlEntry.role, value - direction);

            if (value != 0 && value == direction) isChanged = true;
            else if (controlEntry.type == TardisConsoleUnitControlType.ROTATOR && value != 0) isChanged = true;
        }

        if (isChanged) this.markDirty();
    }

    private void removeControls() {
        for (TardisConsoleControlEntity control : this.controls) control.discard();
        this.controls.clear();
    }

    private boolean throwNotifyIfLocked(TardisStateManager tardis, PlayerEntity player) {
        if (tardis.isBroken()) {
            player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
            return true;
        }

        if (tardis.getSystem(TardisSystemConsoleRoom.class).inProgress()) {
            player.sendMessage(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_LOCKED, true);
            return true;
        }

        return false;
    }

    private void sendControlsUpdatePacket(ServerWorld world) {
        new TardisConsoleUnitControlsStatesUpdatePacket(this.getPos(), this.controlsStorage.writeNbt(new NbtCompound()))
            .sendToAll(world.getServer());
    }

    private void sendMonitorUpdatePacket(ServerWorld world) {
        new TardisConsoleUnitMonitorPageUpdatePacket(this.getPos(), this.monitorPage)
            .sendToAll(world.getServer());
    }

    private void sendSonicScrewdriverSlotUpdatePacket(ServerWorld world) {
        new TardisConsoleUnitSonicScrewdriverSlotUpdatePacket(this.getPos(), this.sonicScrewdriverItemStack)
            .sendToAll(world.getServer());
    }

    private void sendMonitorOpenPacket(ServerPlayerEntity player, TardisStateManager tardis) {
        new TardisConsoleUnitMonitorOpenPacket(player, this.getPos(), tardis.getId(), tardis.writeNbt(new NbtCompound(), tardis.getWorld().getRegistryManager()))
            .sendTo(player);
    }

    private void sendTelepathicInterfaceLocationsOpenPacket(ServerPlayerEntity player) {
        new TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(this.getPos(), player.getServerWorld(), DimensionHelper.getWorld(this.tardisStateManager.getDestinationExteriorDimension(), player.server))
            .sendTo(player);
    }

    private void sendTelepathicInterfaceMapBannersOpenPacket(ServerPlayerEntity player, MapState mapData) {
        new TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket(this.getPos(), mapData.writeNbt(new NbtCompound(), player.getRegistryManager()))
            .sendTo(player);
    }
}

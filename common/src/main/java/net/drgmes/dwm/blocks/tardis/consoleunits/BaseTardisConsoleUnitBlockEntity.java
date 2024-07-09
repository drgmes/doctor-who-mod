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
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.enums.TardisConsoleUnitControlFlags;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
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
    public NbtCompound toInitialChunkDataNbt() {
        return createNbt();
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        if (tag.contains("controlsState")) this.controlsStorage.readNbt(tag.getCompound("controlsState"));
        if (tag.contains("tardisState")) this.tardisStateManager.readNbt(tag.getCompound("tardisState"));
        if (tag.contains("monitorPage")) this.monitorPage = tag.getInt("monitorPage") % MONITOR_PAGES_LENGTH;

        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
        if (tag.contains("Items", 9)) {
            Inventories.readNbt(tag, itemStacks);
            this.sonicScrewdriverItemStack = itemStacks.get(0);
        }
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        if (!this.inited) this.init();
        super.writeNbt(tag);

        tag.put("controlsState", this.controlsStorage.writeNbt(new NbtCompound()));
        tag.put("tardisState", this.tardisStateManager.writeNbt(new NbtCompound()));
        tag.putInt("monitorPage", this.monitorPage % MONITOR_PAGES_LENGTH);

        Inventories.writeNbt(tag, DefaultedList.ofSize(1, this.sonicScrewdriverItemStack), true);
    }

    @Override
    public void markRemoved() {
        this.removeControls();
        super.markRemoved();
    }

    public TardisConsoleUnitEntry getConsoleUnitType() {
        return ((BaseTardisConsoleUnitBlock<?>) this.getCachedState().getBlock()).consoleUnitType;
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

        if (this.getWorld() instanceof ServerWorld serverWorld) {
            this.createControls();

            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                tardis.addConsoleTile(this);

                this.controlsStorage.applyData(tardis);
                this.tardisStateManager.readNbt(tardis.writeNbt(new NbtCompound()));
            });
        }
    }

    public void remove() {
        if (this.getWorld() instanceof ServerWorld serverWorld) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                tardis.removeConsoleTile(this);
            });
        }
    }

    public void useControl(TardisConsoleUnitControlRole controlRole, Hand hand, Entity entity) {
        if (!(this.getWorld() instanceof ServerWorld serverWorld) || !(entity instanceof ServerPlayerEntity player)) return;

        Object value = this.controlsStorage.get(controlRole);
        Optional<TardisStateManager> tardisHolder = TardisStateManager.get(serverWorld);

        // Monitor
        if (controlRole == TardisConsoleUnitControlRole.MONITOR && hand == Hand.OFF_HAND) {
            if (this.throwNotifyIfLocked(tardisHolder, player) || tardisHolder.isEmpty()) return;
            this.sendMonitorOpenPacket(player, tardisHolder.get());
            this.playControlSound(controlRole);
            return;
        }

        // Telepathic Interface
        if (controlRole == TardisConsoleUnitControlRole.TELEPATHIC_INTERFACE && hand == Hand.OFF_HAND) {
            if (this.throwNotifyIfLocked(tardisHolder, player) || tardisHolder.isEmpty()) return;

            ItemStack mainHandItemStack = player.getMainHandStack();
            ItemStack offHandItemStack = player.getOffHandStack();

            if (mainHandItemStack.getItem() instanceof FilledMapItem || offHandItemStack.getItem() instanceof FilledMapItem) {
                ItemStack itemStack = mainHandItemStack.getItem() instanceof FilledMapItem ? mainHandItemStack : offHandItemStack;
                MapState mapData = FilledMapItem.getMapState(itemStack, serverWorld);
                if (mapData == null) return;

                if (mapData.dimension == tardisHolder.get().getWorld().getRegistryKey()) return;
                if (tardisHolder.get().getSystem(TardisSystemFlight.class).inProgress()) return;
                if (tardisHolder.get().getSystem(TardisSystemMaterialization.class).inProgress()) return;

                if (!tardisHolder.get().getSystem(TardisSystemFlight.class).isEnabled()) {
                    player.sendMessage(DWM.TEXTS.FLIGHT_SYSTEM_NOT_INSTALLED, true);
                    return;
                }

                BlockPos destExteriorPosition = tardisHolder.get().getDestinationExteriorPosition();
                BlockPos blockPos = new BlockPos(mapData.centerX, destExteriorPosition.getY(), mapData.centerZ);

                if (mapData.getBanners().size() > 1) {
                    this.sendTelepathicInterfaceMapBannersOpenPacket(player, mapData);
                    return;
                }
                else if (mapData.getBanners().size() == 1 && mapData.getBanners().toArray()[0] instanceof MapBannerMarker banner) {
                    String color = banner.getColor().getName().toUpperCase().replace("_", " ");
                    player.sendMessage(DWM.TEXTS.TELEPATHIC_INTERFACE_MAP_BANNER_LOADED.apply(color), true);
                    blockPos = banner.getPos();
                }
                else {
                    player.sendMessage(DWM.TEXTS.TELEPATHIC_INTERFACE_MAP_COORDS_LOADED, true);
                }

                tardisHolder.get().setDestinationDimension(mapData.dimension);
                tardisHolder.get().setDestinationPosition(blockPos);
                tardisHolder.get().markConsoleTilesUpdated();
                return;
            }

            this.sendTelepathicInterfaceLocationsOpenPacket(player);
            this.playControlSound(controlRole);
            return;
        }

        // Sonic Screwdriver Slot
        if (controlRole == TardisConsoleUnitControlRole.SONIC_SCREWDRIVER_SLOT && hand == Hand.OFF_HAND) {
            SonicDevice.setTardisId(this.sonicScrewdriverItemStack, serverWorld);

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
                SonicDevice.setTardisId(this.sonicScrewdriverItemStack, serverWorld);
                this.sendSonicScrewdriverSlotUpdatePacket(serverWorld);
                this.markDirty();
            }

            return;
        }

        if (this.controlsStorage.update(controlRole, hand)) {
            boolean isUpdated = !Objects.equals(value, this.controlsStorage.get(controlRole));

            switch (controlRole) {
                case STARTER -> {
                    if (isUpdated && (tardisHolder.isEmpty() || !tardisHolder.get().isHandbrakeLocked())) {
                        ModSounds.playSound(serverWorld, this.getPos(), ModSounds.TARDIS_CONTROL_3.get(), 1.0F, 1.0F);
                    }

                    if (!isUpdated && tardisHolder.isPresent() && !tardisHolder.get().isHandbrakeLocked()) {
                        ModSounds.playSound(tardisHolder.get().getWorld(), tardisHolder.get().getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
                        if ((boolean) value) player.sendMessage(DWM.TEXTS.TARDIS_ALREADY_IN_FLIGHT, true);
                        else player.sendMessage(DWM.TEXTS.TARDIS_ALREADY_LANDED, true);
                    }
                }

                case MATERIALIZATION -> {
                    if (isUpdated && (tardisHolder.isEmpty() || !tardisHolder.get().isHandbrakeLocked())) {
                        ModSounds.playSound(serverWorld, this.getPos(), ModSounds.TARDIS_CONTROL_2.get(), 1.0F, 1.0F);
                    }

                    if (!isUpdated && tardisHolder.isPresent() && !tardisHolder.get().isHandbrakeLocked()) {
                        ModSounds.playSound(tardisHolder.get().getWorld(), tardisHolder.get().getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
                        if ((boolean) value) player.sendMessage(DWM.TEXTS.TARDIS_ALREADY_MATERIALIZED, true);
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
                        this.sendMonitorUpdatePacket(serverWorld);
                        this.playControlSound(controlRole);
                        this.markDirty();
                    }
                }

                case MONITOR_PAGE_PREV -> {
                    if ((int) this.controlsStorage.get(controlRole) != 0) {
                        this.monitorPage = this.monitorPage < 1 ? MONITOR_PAGES_LENGTH - 1 : this.monitorPage - 1;
                        this.sendMonitorUpdatePacket(serverWorld);
                        this.playControlSound(controlRole);
                        this.markDirty();
                    }
                }
            }

            if (this.throwNotifyIfLocked(tardisHolder, player) || tardisHolder.isEmpty()) {
                this.sendControlsUpdatePacket(serverWorld);
                return;
            }

            tardisHolder.get().applyData(this.controlsStorage, player);
            this.displayNotification(tardisHolder.get(), controlRole, player);
        }
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

    private void playControlSound(TardisConsoleUnitControlRole controlRole) {
        if (controlRole.soundEventSupplier == null || !(this.getWorld() instanceof ServerWorld)) return;
        ModSounds.playSound(this.getWorld(), this.getPos(), controlRole.soundEventSupplier.get(), 1.0F, 1.0F);
    }

    private boolean throwNotifyIfLocked(Optional<TardisStateManager> tardisHolder, PlayerEntity player) {
        if (tardisHolder.isEmpty()) return false;

        if (tardisHolder.get().isBroken()) {
            player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
            return true;
        }

        if (tardisHolder.get().getSystem(TardisSystemConsoleRoom.class).inProgress()) {
            player.sendMessage(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_LOCKED, true);
            return true;
        }

        return false;
    }

    private void displayNotification(TardisStateManager tardis, TardisConsoleUnitControlRole controlRole, PlayerEntity player) {
        Object value = this.controlsStorage.get(controlRole);
        String message = controlRole.message == null ? null : "message.dwm.tardis.control.role." + controlRole.message;

        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        TardisSystemShields shieldsSystem = tardis.getSystem(TardisSystemShields.class);

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.REQUIRED_MATERIALIZING_SYSTEM) && !materializationSystem.isEnabled()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.MATERIALIZATION_SYSTEM_NOT_INSTALLED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.REQUIRED_FLIGHT_SYSTEM) && !flightSystem.isEnabled()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.FLIGHT_SYSTEM_NOT_INSTALLED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.REQUIRED_SHIELDS_SYSTEM) && !shieldsSystem.isEnabled()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.SHIELDS_SYSTEM_NOT_INSTALLED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.MUST_BE_MATERIALIZED) && !materializationSystem.isMaterialized()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_MATERIALIZED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.MUST_BE_LANDED) && flightSystem.inProgress()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_LANDED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.DEPENDS_ON_OWNER) && !tardis.checkAccess(player, true, false)) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.DEPENDS_ON_SHIELDS_ON) && !tardis.isShieldsEnabled()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.SHIELDS_SYSTEM_NOT_ACTIVE, true);
            return;
        }

        if (controlRole.flags.contains(TardisConsoleUnitControlFlags.DEPENDS_ON_HANDBRAKE_OFF) && tardis.isHandbrakeLocked()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_HANDBRAKE_ACTIVATED, true);
            return;
        }

        Text component = switch (controlRole) {
            case DOORS, LIGHT, SHIELDS, SHIELDS_OXYGEN, SHIELDS_FIRE_PROOF, SHIELDS_MEDICAL, SHIELDS_MINING, SHIELDS_GRAVITATION, SHIELDS_SPECIAL, FUEL_HARVESTING, ENERGY_HARVESTING, HANDBRAKE -> Text.translatable(message + ((boolean) value ? ".active" : ".inactive"));
            case DIM_PREV, DIM_NEXT -> Text.translatable(message, "§e" + tardis.getDestinationExteriorDimension().getValue().getPath().replace("_", " ").toUpperCase());
            case FACING -> Text.translatable(message, Text.translatable(message + "." + (tardis.getDestinationExteriorFacing().ordinal() - 2)));
            case XSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getX());
            case YSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getY());
            case ZSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getZ());
            case XYZSTEP -> Text.translatable(message, "§e" + tardis.getXYZStep());
            case VERTICAL_SCANNING -> Text.translatable(message, Text.translatable(message + "." + value));
            case STARTER -> tardis.getFuelAmount() <= 0 && tardis.getEnergyAmount() <= 0 ? DWM.TEXTS.TARDIS_NOT_ENOUGH_FUEL : null;

            default -> message == null ? null : Text.translatable(message, value);
        };

        if (component != null) {
            this.playControlSound(controlRole);
            player.sendMessage(component, true);
        }
    }

    private void sendControlsUpdatePacket(ServerWorld world) {
        new TardisConsoleUnitControlsStatesUpdatePacket(this.getPos(), this.controlsStorage.writeNbt(new NbtCompound()))
            .sendToChunkListeners(world.getWorldChunk(this.getPos()));
    }

    private void sendMonitorUpdatePacket(ServerWorld world) {
        new TardisConsoleUnitMonitorPageUpdatePacket(this.getPos(), this.monitorPage)
            .sendToChunkListeners(world.getWorldChunk(this.getPos()));
    }

    private void sendMonitorOpenPacket(ServerPlayerEntity player, TardisStateManager tardis) {
        new TardisConsoleUnitMonitorOpenPacket(player, this.getPos(), tardis.getId(), this.tardisStateManager.writeNbt(new NbtCompound()))
            .sendTo(player);
    }

    private void sendTelepathicInterfaceLocationsOpenPacket(ServerPlayerEntity player) {
        new TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(this.getPos(), player.getServerWorld(), DimensionHelper.getWorld(this.tardisStateManager.getDestinationExteriorDimension(), player.server))
            .sendTo(player);
    }

    private void sendTelepathicInterfaceMapBannersOpenPacket(ServerPlayerEntity player, MapState mapData) {
        new TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket(this.getPos(), mapData.writeNbt(new NbtCompound()))
            .sendTo(player);
    }

    private void sendSonicScrewdriverSlotUpdatePacket(ServerWorld world) {
        new TardisConsoleUnitSonicScrewdriverSlotUpdatePacket(this.getPos(), this.sonicScrewdriverItemStack)
            .sendToChunkListeners(world.getWorldChunk(this.getPos()));
    }
}

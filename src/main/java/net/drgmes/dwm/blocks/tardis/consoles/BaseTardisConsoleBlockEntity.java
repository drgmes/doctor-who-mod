package net.drgmes.dwm.blocks.tardis.consoles;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoles.screens.TardisConsoleTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.common.tardis.consoles.TardisConsoleTypeEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.*;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.network.TardisConsoleRemoteCallablePackets;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.PacketHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
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
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public abstract class BaseTardisConsoleBlockEntity extends BlockEntity {
    public final TardisStateManager tardisStateManager = new TardisStateManager(null, false);
    public TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
    public ItemStack screwdriverItemStack = ItemStack.EMPTY;
    public TardisConsoleTypeEntry consoleType;

    public float tickInProgress = 0;
    public int monitorPage = 0;

    private final ArrayList<TardisConsoleControlEntity> controls = new ArrayList<>();
    private int timeToInit = -1;

    public BaseTardisConsoleBlockEntity(BlockEntityType<?> type, TardisConsoleTypeEntry consoleType, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);

        this.consoleType = consoleType;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        super.readNbt(tag);

        this.tardisStateManager.readNbt(tag);
        this.controlsStorage.load(tag);
        this.monitorPage = tag.getInt("monitorPage");

        DefaultedList<ItemStack> itemStacks = DefaultedList.ofSize(1, ItemStack.EMPTY);
        if (tag.contains("Items", 9)) {
            Inventories.readNbt(tag, itemStacks);
            this.screwdriverItemStack = itemStacks.get(0);
        }
    }

    @Override
    protected void writeNbt(NbtCompound tag) {
        super.writeNbt(tag);

        this.tardisStateManager.writeNbt(tag);
        this.controlsStorage.save(tag);

        tag.putInt("monitorPage", this.monitorPage);
        Inventories.writeNbt(tag, DefaultedList.ofSize(1, this.screwdriverItemStack), true);
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
    public void markRemoved() {
        if (this.world instanceof ServerWorld serverWorld) {
            TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                if (tardis.isValid()) tardis.getConsoleTiles().remove(this);
            });
        }

        this.removeControls();
        super.markRemoved();
    }

    public void init() {
        this.createControls();

        if (TardisHelper.isTardisDimension(this.world)) {
            if (this.world instanceof ServerWorld serverWorld) {
                TardisStateManager.get(serverWorld).ifPresent((tardis) -> {
                    if (!tardis.isValid()) return;
                    if (tardis.getConsoleTiles().contains(this)) return;

                    tardis.getConsoleTiles().add(this);
                    tardis.updateConsoleTiles();
                });
            }
            else {
                PacketHelper.sendToServer(
                    TardisConsoleRemoteCallablePackets.class,
                    "initTardisConsoleData",
                    this.getPos()
                );
            }
        }
    }

    public void tick() {
        if (this.timeToInit > 0) {
            --this.timeToInit;
            if (this.timeToInit == 0) this.init();
        }
        else if (this.timeToInit < 0) {
            if (this.world instanceof ServerWorld serverWorld) this.tardisStateManager.setWorld(serverWorld);
            this.timeToInit = 10;
        }

        this.animateControls();

        if (this.tardisStateManager.getSystem(TardisSystemFlight.class).inProgress() || this.tardisStateManager.getSystem(TardisSystemMaterialization.class).inProgress()) {
            this.tickInProgress++;
            this.tickInProgress %= 60;
        }
    }

    public void sendMonitorUpdatePacket(ServerWorld world) {
        PacketHelper.sendToClient(
            TardisConsoleRemoteCallablePackets.class,
            "updateTardisConsoleMonitorPage",
            world.getWorldChunk(this.getPos()),
            this.getPos(), this.monitorPage
        );
    }

    public void sendControlsUpdatePacket(ServerWorld world) {
        PacketHelper.sendToClient(
            TardisConsoleRemoteCallablePackets.class,
            "updateTardisConsoleControlsStates",
            world.getWorldChunk(this.getPos()),
            this.getPos(), this.controlsStorage.save(new NbtCompound())
        );
    }

    public void sendScrewdriverSlotUpdatePacket(ServerWorld world) {
        if (this.screwdriverItemStack.isEmpty()) {
            PacketHelper.sendToClient(
                TardisConsoleRemoteCallablePackets.class,
                "clearTardisConsoleScrewdriverSlot",
                world.getWorldChunk(this.getPos()),
                this.getPos()
            );
        }
        else {
            PacketHelper.sendToClient(
                TardisConsoleRemoteCallablePackets.class,
                "updateTardisConsoleScrewdriverSlot",
                world.getWorldChunk(this.getPos()),
                this.getPos(), this.screwdriverItemStack
            );
        }
    }

    public void sendMonitorOpenPacket(ServerPlayerEntity player, TardisStateManager tardis) {
        PacketHelper.sendToClient(
            TardisConsoleRemoteCallablePackets.class,
            "openTardisConsoleMonitor",
            player,
            this.getPos(), DimensionHelper.getWorldId(tardis.getWorld()), tardis.getConsoleRoom().name, createConsoleRoomsList(tardis.getConsoleRoom().name)
        );
    }

    public void sendTelepathicInterfaceLocationsOpenPacket(ServerPlayerEntity player) {
        PacketHelper.sendToClient(
            TardisConsoleRemoteCallablePackets.class,
            "openTardisConsoleTelepathicInterfaceLocations",
            player,
            this.getPos(), createLocationsListFromRegistry(player.getWorld())
        );
    }

    public void sendTelepathicInterfaceMapBannersOpenPacket(ServerPlayerEntity player, MapState mapData) {
        PacketHelper.sendToClient(
            TardisConsoleRemoteCallablePackets.class,
            "openTardisConsoleTelepathicInterfaceMapBanners",
            player,
            this.getPos(), mapData.writeNbt(new NbtCompound())
        );
    }

    public void useControl(TardisConsoleControlEntry control, Hand hand, Entity entity) {
        if (!(this.world instanceof ServerWorld serverWorld) || !(entity instanceof ServerPlayerEntity player)) return;

        Optional<TardisStateManager> tardisHolder = TardisHelper.isTardisDimension(entity.world)
            ? TardisStateManager.get(serverWorld)
            : Optional.empty();

        // Monitor
        if (control.role == ETardisConsoleControlRole.MONITOR && hand == Hand.OFF_HAND) {
            if (tardisHolder.isEmpty() || !tardisHolder.get().isValid()) return;

            if (tardisHolder.get().isBroken()) {
                player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
                return;
            }

            this.sendMonitorOpenPacket(player, tardisHolder.get());
            return;
        }

        // Telepathic Interface
        if (control.role == ETardisConsoleControlRole.TELEPATHIC_INTERFACE && hand == Hand.OFF_HAND) {
            if (tardisHolder.isEmpty() || !tardisHolder.get().isValid()) return;

            if (tardisHolder.get().isBroken()) {
                player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
                return;
            }

            ItemStack mainHandItemStack = player.getMainHandStack();
            ItemStack offHandItemStack = player.getOffHandStack();

            if (mainHandItemStack.getItem() instanceof FilledMapItem || offHandItemStack.getItem() instanceof FilledMapItem) {
                ItemStack itemStack = mainHandItemStack.getItem() instanceof FilledMapItem ? mainHandItemStack : offHandItemStack;
                MapState mapData = FilledMapItem.getOrCreateMapState(itemStack, serverWorld);
                if (mapData == null) return;

                if (mapData.dimension == tardisHolder.get().getWorld().getRegistryKey()) return;
                if (tardisHolder.get().getSystem(TardisSystemFlight.class).inProgress()) return;
                if (tardisHolder.get().getSystem(TardisSystemMaterialization.class).inProgress()) return;

                BlockPos destExteriorPosition = tardisHolder.get().getDestinationExteriorPosition();
                BlockPos blockPos = new BlockPos(mapData.centerX, destExteriorPosition.getY(), mapData.centerZ);

                if (mapData.getBanners().size() > 1) {
                    this.sendTelepathicInterfaceMapBannersOpenPacket(player, mapData);
                    return;
                }
                else if (mapData.getBanners().size() == 1 && mapData.getBanners().toArray()[0] instanceof MapBannerMarker banner) {
                    String color = "§e" + banner.getColor().getName().toUpperCase().replace("_", " ");
                    player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.map.loaded.banner", color), true);
                    blockPos = banner.getPos();
                }
                else {
                    player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.map.loaded"), true);
                }

                tardisHolder.get().setDestinationDimension(mapData.dimension);
                tardisHolder.get().setDestinationPosition(blockPos);
                tardisHolder.get().updateConsoleTiles();
                return;
            }

            this.sendTelepathicInterfaceLocationsOpenPacket(player);
            return;
        }

        // Screwdriver Slot
        if (control.role == ETardisConsoleControlRole.SCREWDRIVER_SLOT && hand == Hand.OFF_HAND) {
            Screwdriver.setTardisId(this.screwdriverItemStack, serverWorld);

            boolean isChanged = false;

            if (this.screwdriverItemStack.isEmpty()) {
                ItemStack mainHandItem = player.getMainHandStack();
                ItemStack offHandItem = player.getOffHandStack();

                if (Screwdriver.checkItemStackIsScrewdriver(mainHandItem)) {
                    this.screwdriverItemStack = mainHandItem;
                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                    isChanged = true;
                }
                else if (Screwdriver.checkItemStackIsScrewdriver(offHandItem)) {
                    this.screwdriverItemStack = offHandItem;
                    player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                    isChanged = true;
                }
            }
            else if (player.getMainHandStack().isEmpty()) {
                player.setStackInHand(Hand.MAIN_HAND, this.screwdriverItemStack);
                this.screwdriverItemStack = ItemStack.EMPTY;
                isChanged = true;
            }
            else if (player.getInventory().insertStack(this.screwdriverItemStack)) {
                this.screwdriverItemStack = ItemStack.EMPTY;
                isChanged = true;
            }

            if (isChanged) {
                Screwdriver.setTardisId(this.screwdriverItemStack, serverWorld);
                this.sendScrewdriverSlotUpdatePacket(serverWorld);
                this.markDirty();
            }

            return;
        }

        if (this.controlsStorage.update(control.role, hand)) {
            int monitorPageLength = 2;

            // Next Screen Page
            int monitorPageNext = (int) controlsStorage.get(ETardisConsoleControlRole.MONITOR_PAGE_NEXT);
            if (monitorPageNext != 0) this.monitorPage = (this.monitorPage + 1) % monitorPageLength;

            // Prev Screen Page
            int monitorPagePrev = (int) controlsStorage.get(ETardisConsoleControlRole.MONITOR_PAGE_PREV);
            if (monitorPagePrev != 0)
                this.monitorPage = this.monitorPage < 1 ? monitorPageLength - 1 : this.monitorPage - 1;

            if (monitorPagePrev != 0 || monitorPageNext != 0) this.sendMonitorUpdatePacket(serverWorld);
            this.markDirty();

            if (tardisHolder.isEmpty() || !tardisHolder.get().isValid()) {
                this.sendControlsUpdatePacket(serverWorld);
                return;
            }

            if (tardisHolder.get().isBroken()) {
                player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
                this.sendControlsUpdatePacket(serverWorld);
                return;
            }

            tardisHolder.get().applyControlsStorageToData(this.controlsStorage);
            this.displayNotification(tardisHolder.get(), this.controlsStorage, control.role, player);
        }
    }

    private static NbtCompound createConsoleRoomsList(String consoleRoomId) {
        List<TardisConsoleRoomEntry> list = TardisConsoleRooms.CONSOLE_ROOMS.values().stream().filter((consoleRoom) -> !consoleRoom.isHidden).toList();

        AtomicInteger i = new AtomicInteger();
        NbtCompound tag = new NbtCompound();
        NbtCompound consoleRoomsTag = new NbtCompound();
        list.forEach((entry) -> consoleRoomsTag.put(String.format("%1$" + 5 + "s", i.incrementAndGet()).replace(' ', '0'), entry.writeNbt(new NbtCompound())));

        tag.put("consoleRooms", consoleRoomsTag);
        tag.putInt("selectedConsoleRoomIndex", Math.max(0, Math.min(list.size(), list.indexOf(TardisConsoleRooms.getConsoleRoom(consoleRoomId)))));

        return tag;
    }

    private static NbtCompound createLocationsListFromRegistry(ServerWorld world) {
        List<Map.Entry<Identifier, TardisConsoleTelepathicInterfaceLocationsScreen.EDataType>> list = new ArrayList<>();
        list.addAll(getLocationsForRegistry(Registry.BIOME_KEY, TardisConsoleTelepathicInterfaceLocationsScreen.EDataType.BIOME, world));
        list.addAll(getLocationsForRegistry(Registry.STRUCTURE_KEY, TardisConsoleTelepathicInterfaceLocationsScreen.EDataType.STRUCTURE, world));

        AtomicInteger i = new AtomicInteger();
        NbtCompound tag = new NbtCompound();
        list.forEach((entry) -> {
            NbtCompound pair = new NbtCompound();
            pair.putString("id", entry.getKey().toString());
            pair.putString("type", entry.getValue().name());
            tag.put(String.format("%1$" + 5 + "s", i.incrementAndGet()).replace(' ', '0'), pair);
        });

        return tag;
    }

    private static <T> List<Map.Entry<Identifier, TardisConsoleTelepathicInterfaceLocationsScreen.EDataType>> getLocationsForRegistry(RegistryKey<Registry<T>> registryKey, TardisConsoleTelepathicInterfaceLocationsScreen.EDataType type, ServerWorld world) {
        Registry<T> registry = world.getRegistryManager().get(registryKey);

        List<Map.Entry<Identifier, TardisConsoleTelepathicInterfaceLocationsScreen.EDataType>> list = new ArrayList<>(
            registry.getKeys().stream()
                .filter((res) -> !res.getValue().equals(ModDimensions.ModDimensionTypes.TARDIS.getValue()))
                .map((res) -> Map.entry(res.getValue(), type)).toList()
        );

        if (list.size() > 0) {
            list.sort(Comparator.comparing(a -> a.getKey().getPath()));
        }

        return list;
    }

    private void createControls() {
        if (this.controls.size() == this.consoleType.controlEntries.size()) return;
        if (!(this.world instanceof ServerWorld serverWorld)) return;
        this.removeControls();

        for (TardisConsoleControlEntry controlEntry : this.consoleType.controlEntries.values()) {
            this.controls.add(controlEntry.createEntity(this, serverWorld, this.getPos()));
        }

        this.markDirty();
    }

    private void animateControls() {
        boolean isChanged = false;
        for (TardisConsoleControlEntry controlEntry : this.consoleType.controlEntries.values()) {
            if (controlEntry.role.type != ETardisConsoleControlRoleType.ANIMATION && controlEntry.role.type != ETardisConsoleControlRoleType.ANIMATION_DIRECT) {
                continue;
            }

            int value = (int) this.controlsStorage.get(controlEntry.role);
            int direction = Integer.compare(value, 0);

            this.controlsStorage.values.put(controlEntry.role, value - direction);

            if (value != 0 && value == direction) isChanged = true;
            else if (controlEntry.type == ETardisConsoleControlEntry.ROTATOR && value != 0) isChanged = true;
        }

        if (isChanged) this.markDirty();
    }

    private void removeControls() {
        for (TardisConsoleControlEntity control : this.controls) control.discard();
        this.controls.clear();
    }

    private void displayNotification(TardisStateManager tardis, TardisConsoleControlsStorage controlsStorage, ETardisConsoleControlRole role, PlayerEntity player) {
        Object value = controlsStorage.get(role);

        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        TardisSystemShields shieldsSystem = tardis.getSystem(TardisSystemShields.class);

        boolean isMaterializationSystemEnabled = materializationSystem.isEnabled();
        boolean isFlightSystemEnabled = flightSystem.isEnabled();
        boolean isShieldsSystemEnabled = shieldsSystem.isEnabled();

        boolean isMaterialized = materializationSystem.isMaterialized();
        boolean isInFlight = flightSystem.inProgress();

        Text component = switch (role) {
            case DOORS, LIGHT, ENERGY_ARTRON_HARVESTING, ENERGY_FORGE_HARVESTING -> !isMaterialized ? null : Text.translatable(role.message + ((boolean) value ? ".active" : ".inactive"));
            case HANDBRAKE -> Text.translatable(role.message + ((boolean) value ? ".active" : ".inactive"));
            case SAFE_DIRECTION -> Text.translatable(role.message, Text.translatable(role.message + "." + value));
            case FACING -> isInFlight ? null : Text.translatable(role.message, Text.translatable(role.message + "." + (tardis.getDestinationExteriorFacing().ordinal() - 2)));
            case XYZSTEP -> isInFlight ? null : Text.translatable(role.message, "§e" + tardis.getXYZStep());
            case XSET -> isInFlight ? null : Text.translatable(role.message, "§e" + tardis.getDestinationExteriorPosition().getX());
            case YSET -> isInFlight ? null : Text.translatable(role.message, "§e" + tardis.getDestinationExteriorPosition().getY());
            case ZSET -> isInFlight ? null : Text.translatable(role.message, "§e" + tardis.getDestinationExteriorPosition().getZ());
            case DIM_PREV, DIM_NEXT -> isInFlight ? null : Text.translatable(role.message, "§e" + tardis.getDestinationExteriorDimension().getValue().getPath().replace("_", " ").toUpperCase());
            case SHIELDS -> !isMaterialized ? null : (isShieldsSystemEnabled ? Text.translatable(role.message + ((boolean) value ? ".active" : ".inactive")) : DWM.TEXTS.SHIELDS_GENERATOR_NOT_INSTALLED);
            case STARTER -> isMaterializationSystemEnabled ? (isFlightSystemEnabled ? null : DWM.TEXTS.DIRECTIONAL_UNIT_NOT_INSTALLED) : DWM.TEXTS.DEMATERIALIZATION_CIRCUIT_NOT_INSTALLED;
            case MATERIALIZATION -> isMaterializationSystemEnabled ? null : DWM.TEXTS.DEMATERIALIZATION_CIRCUIT_NOT_INSTALLED;

            default -> isInFlight || role.message == null ? null : Text.translatable(role.message, value);
        };

        if (component != null) {
            player.sendMessage(component, true);
        }
    }
}

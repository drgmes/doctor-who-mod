package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.screens.TardisConsoleUnitTelepathicInterfaceLocationsScreen;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.common.tardis.consoleunits.TardisConsoleUnitTypeEntry;
import net.drgmes.dwm.common.tardis.consoleunits.controls.*;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.network.client.*;
import net.drgmes.dwm.network.server.TardisConsoleUnitInitPacket;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
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
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;

public abstract class BaseTardisConsoleUnitBlockEntity extends BlockEntity {
    public final TardisStateManager tardisStateManager = new TardisStateManager(null, false);
    public TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
    public ItemStack screwdriverItemStack = ItemStack.EMPTY;
    public TardisConsoleUnitTypeEntry consoleType;

    public float tickInProgress = 0;
    public int monitorPage = 0;

    private final ArrayList<TardisConsoleControlEntity> controls = new ArrayList<>();
    private int timeToInit = -1;

    public BaseTardisConsoleUnitBlockEntity(BlockEntityType<?> type, TardisConsoleUnitTypeEntry consoleType, BlockPos blockPos, BlockState blockState) {
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
                new TardisConsoleUnitInitPacket(this.getPos()).sendToServer();
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
        new TardisConsoleUnitMonitorPageUpdatePacket(this.getPos(), this.monitorPage)
            .sendToChunkListeners(world.getWorldChunk(this.getPos()));
    }

    public void sendControlsUpdatePacket(ServerWorld world) {
        new TardisConsoleUnitControlsStatesUpdatePacket(this.getPos(), this.controlsStorage.save(new NbtCompound()))
            .sendToChunkListeners(world.getWorldChunk(this.getPos()));
    }

    public void sendScrewdriverSlotUpdatePacket(ServerWorld world) {
        new TardisConsoleUnitScrewdriverSlotUpdatePacket(this.getPos(), this.screwdriverItemStack)
            .sendToChunkListeners(world.getWorldChunk(this.getPos()));
    }

    public void sendMonitorOpenPacket(ServerPlayerEntity player, TardisStateManager tardis) {
        new TardisConsoleUnitMonitorOpenPacket(this.getPos(), tardis.getId(), tardis.getConsoleRoom().name, createConsoleRoomsList(tardis.getConsoleRoom().name))
            .sendTo(player);
    }

    public void sendTelepathicInterfaceLocationsOpenPacket(ServerPlayerEntity player) {
        new TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(this.getPos(), createLocationsListFromRegistry(player.getServerWorld(), DimensionHelper.getWorld(this.tardisStateManager.getDestinationExteriorDimension(), player.server)))
            .sendTo(player);
    }

    public void sendTelepathicInterfaceMapBannersOpenPacket(ServerPlayerEntity player, MapState mapData) {
        new TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket(this.getPos(), mapData.writeNbt(new NbtCompound()))
            .sendTo(player);
    }

    public void useControl(TardisConsoleControlEntry control, Hand hand, Entity entity) {
        if (!(this.world instanceof ServerWorld serverWorld) || !(entity instanceof ServerPlayerEntity player)) return;

        boolean isUpdated = false;
        boolean updateResult = false;

        Optional<TardisStateManager> tardisHolder = TardisHelper.isTardisDimension(entity.getWorld())
            ? TardisStateManager.get(serverWorld)
            : Optional.empty();

        // Monitor
        if (control.role == ETardisConsoleUnitControlRole.MONITOR && hand == Hand.OFF_HAND) {
            if (this.throwNotifyIfBroken(tardisHolder, player)) return;
            this.sendMonitorOpenPacket(player, tardisHolder.get());
            return;
        }

        // Telepathic Interface
        if (control.role == ETardisConsoleUnitControlRole.TELEPATHIC_INTERFACE && hand == Hand.OFF_HAND) {
            if (this.throwNotifyIfBroken(tardisHolder, player)) return;

            ItemStack mainHandItemStack = player.getMainHandStack();
            ItemStack offHandItemStack = player.getOffHandStack();

            if (mainHandItemStack.getItem() instanceof FilledMapItem || offHandItemStack.getItem() instanceof FilledMapItem) {
                ItemStack itemStack = mainHandItemStack.getItem() instanceof FilledMapItem ? mainHandItemStack : offHandItemStack;
                MapState mapData = FilledMapItem.getMapState(itemStack, serverWorld);
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

        // Handbrake
        if (control.role == ETardisConsoleUnitControlRole.HANDBRAKE) {
            if (this.throwNotifyIfBroken(tardisHolder, player)) return;

            boolean oldValue = (boolean) this.controlsStorage.get(control.role);
            updateResult = this.controlsStorage.update(control.role, hand);
            isUpdated = true;
            boolean value = (boolean) this.controlsStorage.get(control.role);

            if (value != oldValue && value) ModSounds.playTardisHandbrakeOnSound(serverWorld, this.getPos());
            else if (value != oldValue) ModSounds.playTardisHandbrakeOffSound(serverWorld, this.getPos());
        }

        // Starter
        if (control.role == ETardisConsoleUnitControlRole.STARTER) {
            boolean handbrake = (boolean) this.controlsStorage.get(ETardisConsoleUnitControlRole.HANDBRAKE);

            if (!handbrake && tardisHolder.isPresent() && tardisHolder.get().isValid() && !tardisHolder.get().isBroken()) {
                boolean oldValue = (boolean) this.controlsStorage.get(control.role);
                updateResult = this.controlsStorage.update(control.role, hand);
                isUpdated = true;
                boolean value = (boolean) this.controlsStorage.get(control.role);

                if (value != oldValue) ModSounds.playSound(serverWorld, this.getPos(), ModSounds.TARDIS_CONTROL_3.get(), 1.0F, 1.0F);
            }
        }

        // Materialization
        if (control.role == ETardisConsoleUnitControlRole.MATERIALIZATION) {
            if (tardisHolder.isPresent() && tardisHolder.get().isValid() && !tardisHolder.get().isBroken()) {
                boolean oldValue = (boolean) this.controlsStorage.get(control.role);
                updateResult = this.controlsStorage.update(control.role, hand);
                isUpdated = true;
                boolean value = (boolean) this.controlsStorage.get(control.role);

                if (value != oldValue) ModSounds.playSound(serverWorld, this.getPos(), ModSounds.TARDIS_CONTROL_2.get(), 1.0F, 1.0F);
            }
        }

        // Screwdriver Slot
        if (control.role == ETardisConsoleUnitControlRole.SCREWDRIVER_SLOT && hand == Hand.OFF_HAND) {
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

        if ((isUpdated && updateResult) || (!isUpdated && this.controlsStorage.update(control.role, hand))) {
            if (tardisHolder.isEmpty() || !tardisHolder.get().isValid() || this.throwNotifyIfBroken(tardisHolder, player)) {
                this.sendControlsUpdatePacket(serverWorld);
                return;
            }

            int monitorPageLength = 2;

            // Next Screen Page
            int monitorPageNext = (int) controlsStorage.get(ETardisConsoleUnitControlRole.MONITOR_PAGE_NEXT);
            if (monitorPageNext != 0) this.monitorPage = (this.monitorPage + 1) % monitorPageLength;

            // Prev Screen Page
            int monitorPagePrev = (int) controlsStorage.get(ETardisConsoleUnitControlRole.MONITOR_PAGE_PREV);
            if (monitorPagePrev != 0) this.monitorPage = this.monitorPage < 1 ? monitorPageLength - 1 : this.monitorPage - 1;

            if (monitorPagePrev != 0 || monitorPageNext != 0) {
                this.sendMonitorUpdatePacket(serverWorld);
                this.markDirty();
            }

            tardisHolder.get().applyControlsStorageToData(this.controlsStorage);
            this.displayNotification(tardisHolder.get(), control.role, player);
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

    private static NbtCompound createLocationsListFromRegistry(ServerWorld world, @Nullable ServerWorld destinationWorld) {
        List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> list = new ArrayList<>();

        List<Identifier> biomeIds;
        Registry<Structure> structureRegistry;

        if (destinationWorld != null) {
            Set<RegistryEntry<Biome>> biomeEntries = destinationWorld.getChunkManager().getChunkGenerator().getBiomeSource().getBiomes();
            biomeIds = biomeEntries.stream().filter((b) -> b.getKey().isPresent()).map((b) -> b.getKey().get().getValue()).toList();
            structureRegistry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);
        }
        else {
            biomeIds = null;
            structureRegistry = null;
        }

        list.addAll(getLocationsForRegistry(
            TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.BIOME,
            RegistryKeys.BIOME,
            world,
            (entry) -> (
                !entry.getValue().equals(ModDimensions.ModDimensionTypes.TARDIS.getValue()) && (biomeIds == null || biomeIds.contains(entry.getValue()))
            )
        ));

        list.addAll(getLocationsForRegistry(
            TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType.STRUCTURE,
            RegistryKeys.STRUCTURE,
            world,
            (entry) -> {
                boolean flag = false;

                if (structureRegistry != null) {
                    Structure structure = structureRegistry.get(entry.getValue());
                    if (structure != null) {
                        flag = structure.getValidBiomes().stream().anyMatch((b) -> b.getKey().isPresent() && biomeIds.contains(b.getKey().get().getValue()));
                    }
                }

                return flag;
            }
        ));

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

    private static <T> List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> getLocationsForRegistry(TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType type, RegistryKey<Registry<T>> registryKey, ServerWorld world, Function<RegistryKey<T>, Boolean> entryChecker) {
        Registry<T> registry = world.getRegistryManager().get(registryKey);

        List<Map.Entry<Identifier, TardisConsoleUnitTelepathicInterfaceLocationsScreen.EDataType>> list = new ArrayList<>(
            registry.getKeys().stream().filter(entryChecker::apply).map((res) -> Map.entry(res.getValue(), type)).toList()
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
            if (controlEntry.role.type != ETardisConsoleUnitControlRoleType.ANIMATION && controlEntry.role.type != ETardisConsoleUnitControlRoleType.ANIMATION_DIRECT) {
                continue;
            }

            int value = (int) this.controlsStorage.get(controlEntry.role);
            int direction = Integer.compare(value, 0);

            this.controlsStorage.values.put(controlEntry.role, value - direction);

            if (value != 0 && value == direction) isChanged = true;
            else if (controlEntry.type == ETardisConsoleUnitControlEntry.ROTATOR && value != 0) isChanged = true;
        }

        if (isChanged) this.markDirty();
    }

    private void removeControls() {
        for (TardisConsoleControlEntity control : this.controls) control.discard();
        this.controls.clear();
    }

    private boolean throwNotifyIfBroken(Optional<TardisStateManager> tardisHolder, PlayerEntity player) {
        if (tardisHolder.isEmpty() || !tardisHolder.get().isValid()) return true;

        if (tardisHolder.get().isBroken()) {
            player.sendMessage(DWM.TEXTS.TARDIS_BROKEN, true);
            return true;
        }

        return false;
    }

    private void displayNotification(TardisStateManager tardis, ETardisConsoleUnitControlRole role, PlayerEntity player) {
        Object value = this.controlsStorage.get(role);

        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        TardisSystemShields shieldsSystem = tardis.getSystem(TardisSystemShields.class);

        boolean isMaterializationSystemEnabled = materializationSystem.isEnabled();
        boolean isFlightSystemEnabled = flightSystem.isEnabled();
        boolean isShieldsSystemEnabled = shieldsSystem.isEnabled();

        boolean isMaterialized = materializationSystem.isMaterialized();
        boolean isInFlight = flightSystem.inProgress();

        String message = role.message == null ? null : "message." + DWM.MODID + ".tardis.control.role." + role.message;
        Text component = switch (role) {
            case DOORS, LIGHT, FUEL_HARVESTING, ENERGY_HARVESTING -> !isMaterialized ? null : Text.translatable(message + ((boolean) value ? ".active" : ".inactive"));
            case HANDBRAKE -> Text.translatable(message + ((boolean) value ? ".active" : ".inactive"));
            case SAFE_DIRECTION -> Text.translatable(message, Text.translatable(message + "." + value));
            case FACING -> isInFlight ? null : Text.translatable(message, Text.translatable(message + "." + (tardis.getDestinationExteriorFacing().ordinal() - 2)));
            case XYZSTEP -> isInFlight ? null : Text.translatable(message, "§e" + tardis.getXYZStep());
            case XSET -> isInFlight ? null : Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getX());
            case YSET -> isInFlight ? null : Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getY());
            case ZSET -> isInFlight ? null : Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getZ());
            case DIM_PREV, DIM_NEXT -> isInFlight ? null : Text.translatable(message, "§e" + tardis.getDestinationExteriorDimension().getValue().getPath().replace("_", " ").toUpperCase());
            case SHIELDS -> !isMaterialized ? null : (isShieldsSystemEnabled ? Text.translatable(message + ((boolean) value ? ".active" : ".inactive")) : DWM.TEXTS.SHIELDS_GENERATOR_NOT_INSTALLED);
            case SHIELDS_OXYGEN, SHIELDS_FIRE_PROOF, SHIELDS_MEDICAL, SHIELDS_MINING, SHIELDS_GRAVITATION, SHIELDS_SPECIAL -> !isMaterialized ? null : (isShieldsSystemEnabled && tardis.isShieldsEnabled() ? Text.translatable(message + ((boolean) value ? ".active" : ".inactive")) : DWM.TEXTS.SHIELDS_GENERATOR_NOT_ACTIVE);
            case STARTER -> isMaterializationSystemEnabled ? (isFlightSystemEnabled ? (tardis.getFuelAmount() > 0 || tardis.getEnergyAmount() > 0 ? null : DWM.TEXTS.TARDIS_NOT_ENOUGH_FUEL) : DWM.TEXTS.DIRECTIONAL_UNIT_NOT_INSTALLED) : DWM.TEXTS.DEMATERIALIZATION_CIRCUIT_NOT_INSTALLED;
            case MATERIALIZATION -> isMaterializationSystemEnabled ? null : DWM.TEXTS.DEMATERIALIZATION_CIRCUIT_NOT_INSTALLED;

            default -> isInFlight || message == null ? null : Text.translatable(message, value);
        };

        if (component != null) {
            player.sendMessage(component, true);
        }
    }
}

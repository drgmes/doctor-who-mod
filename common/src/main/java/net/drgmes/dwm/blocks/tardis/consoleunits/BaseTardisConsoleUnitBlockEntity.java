package net.drgmes.dwm.blocks.tardis.consoleunits;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consoleunits.TardisConsoleUnitTypeEntry;
import net.drgmes.dwm.common.tardis.consoleunits.controls.*;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.common.tardis.systems.TardisSystemShields;
import net.drgmes.dwm.entities.tardis.consoleunit.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.network.client.*;
import net.drgmes.dwm.network.server.TardisConsoleUnitInitPacket;
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
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.math.BlockPos;

import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public abstract class BaseTardisConsoleUnitBlockEntity extends BlockEntity {
    public static final int MONITOR_PAGES_LENGTH = 2;

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
        new TardisConsoleUnitMonitorOpenPacket(player, this.getPos(), tardis.getId(), this.tardisStateManager.writeNbt(new NbtCompound()))
            .sendTo(player);
    }

    public void sendTelepathicInterfaceLocationsOpenPacket(ServerPlayerEntity player) {
        new TardisConsoleUnitTelepathicInterfaceLocationsOpenPacket(this.getPos(), player.getServerWorld(), DimensionHelper.getWorld(this.tardisStateManager.getDestinationExteriorDimension(), player.server))
            .sendTo(player);
    }

    public void sendTelepathicInterfaceMapBannersOpenPacket(ServerPlayerEntity player, MapState mapData) {
        new TardisConsoleUnitTelepathicInterfaceMapBannersOpenPacket(this.getPos(), mapData.writeNbt(new NbtCompound()))
            .sendTo(player);
    }

    public void useControl(TardisConsoleControlEntry control, Hand hand, Entity entity) {
        if (!(this.world instanceof ServerWorld serverWorld) || !(entity instanceof ServerPlayerEntity player)) return;
        Object value = this.controlsStorage.get(control.role);

        Optional<TardisStateManager> tardisHolder = TardisHelper.isTardisDimension(entity.getWorld())
            ? TardisStateManager.get(serverWorld)
            : Optional.empty();

        // Monitor
        if (control.role == ETardisConsoleUnitControlRole.MONITOR && hand == Hand.OFF_HAND) {
            if (this.throwNotifyIfBroken(tardisHolder, player) || tardisHolder.isEmpty()) return;
            this.sendMonitorOpenPacket(player, tardisHolder.get());
            this.playControlSound(control.role);
            return;
        }

        // Telepathic Interface
        if (control.role == ETardisConsoleUnitControlRole.TELEPATHIC_INTERFACE && hand == Hand.OFF_HAND) {
            if (this.throwNotifyIfBroken(tardisHolder, player) || tardisHolder.isEmpty()) return;

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
                    player.sendMessage(DWM.TEXTS.DIRECTIONAL_UNIT_NOT_INSTALLED, true);
                    return;
                }

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
            this.playControlSound(control.role);
            return;
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
                    ModSounds.playScrewdriverPutSound(player.getWorld(), player.getBlockPos());
                    player.setStackInHand(Hand.MAIN_HAND, ItemStack.EMPTY);
                    isChanged = true;
                }
                else if (Screwdriver.checkItemStackIsScrewdriver(offHandItem)) {
                    this.screwdriverItemStack = offHandItem;
                    ModSounds.playScrewdriverPutSound(player.getWorld(), player.getBlockPos());
                    player.setStackInHand(Hand.OFF_HAND, ItemStack.EMPTY);
                    isChanged = true;
                }
            }
            else if (player.getMainHandStack().isEmpty()) {
                ModSounds.playScrewdriverPickupSound(player.getWorld(), player.getBlockPos());
                player.setStackInHand(Hand.MAIN_HAND, this.screwdriverItemStack);
                this.screwdriverItemStack = ItemStack.EMPTY;
                isChanged = true;
            }
            else if (player.getInventory().insertStack(this.screwdriverItemStack)) {
                ModSounds.playScrewdriverPickupSound(player.getWorld(), player.getBlockPos());
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
            boolean isUpdated = !Objects.equals(value, this.controlsStorage.get(control.role));

            switch (control.role) {
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
                        if ((boolean) this.controlsStorage.get(control.role)) ModSounds.playTardisHandbrakeOnSound(this.world, player.getBlockPos());
                        else ModSounds.playTardisHandbrakeOffSound(this.world, player.getBlockPos());
                    }
                }

                case MONITOR_PAGE_NEXT -> {
                    if ((int) this.controlsStorage.get(control.role) != 0) {
                        this.monitorPage = (this.monitorPage + 1) % MONITOR_PAGES_LENGTH;
                        this.sendMonitorUpdatePacket(serverWorld);
                        this.playControlSound(control.role);
                        this.markDirty();
                    }
                }

                case MONITOR_PAGE_PREV -> {
                    if ((int) this.controlsStorage.get(control.role) != 0) {
                        this.monitorPage = this.monitorPage < 1 ? MONITOR_PAGES_LENGTH - 1 : this.monitorPage - 1;
                        this.sendMonitorUpdatePacket(serverWorld);
                        this.playControlSound(control.role);
                        this.markDirty();
                    }
                }
            }

            if (tardisHolder.isEmpty() || !tardisHolder.get().isValid() || this.throwNotifyIfBroken(tardisHolder, player)) {
                this.sendControlsUpdatePacket(serverWorld);
                return;
            }

            tardisHolder.get().applyControlsStorageToData(this.controlsStorage, player);
            this.displayNotification(tardisHolder.get(), control.role, player);
        }
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

    private void playControlSound(ETardisConsoleUnitControlRole role) {
        if (this.getWorld() != null && !this.getWorld().isClient && role.soundEventSupplier != null) ModSounds.playSound(this.getWorld(), this.getPos(), role.soundEventSupplier.get(), 1.0F, 1.0F);
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
        String message = role.message == null ? null : "message." + DWM.MODID + ".tardis.control.role." + role.message;

        TardisSystemMaterialization materializationSystem = tardis.getSystem(TardisSystemMaterialization.class);
        TardisSystemFlight flightSystem = tardis.getSystem(TardisSystemFlight.class);
        TardisSystemShields shieldsSystem = tardis.getSystem(TardisSystemShields.class);

        if (role.flags.contains(ETardisConsoleUnitControlRoleFlags.REQUIRED_MATERIALIZING_SYSTEM) && !materializationSystem.isEnabled()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.DEMATERIALIZATION_CIRCUIT_NOT_INSTALLED, true);
            return;
        }

        if (role.flags.contains(ETardisConsoleUnitControlRoleFlags.REQUIRED_FLIGHT_SYSTEM) && !flightSystem.isEnabled()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.DIRECTIONAL_UNIT_NOT_INSTALLED, true);
            return;
        }

        if (role.flags.contains(ETardisConsoleUnitControlRoleFlags.REQUIRED_SHIELDS_SYSTEM) && !shieldsSystem.isEnabled()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.SHIELDS_GENERATOR_NOT_INSTALLED, true);
            return;
        }

        if (role.flags.contains(ETardisConsoleUnitControlRoleFlags.MUST_BE_MATERIALIZED) && !materializationSystem.isMaterialized()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_MATERIALIZED, true);
            return;
        }

        if (role.flags.contains(ETardisConsoleUnitControlRoleFlags.MUST_BE_LANDED) && flightSystem.inProgress()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_MUST_BE_LANDED, true);
            return;
        }

        if (role.flags.contains(ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_OWNER) && !tardis.checkAccess(player, true)) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_NOT_ALLOWED, true);
            return;
        }

        if (role.flags.contains(ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_SHIELDS_ON) && !tardis.isShieldsEnabled()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.SHIELDS_GENERATOR_NOT_ACTIVE, true);
            return;
        }

        if (role.flags.contains(ETardisConsoleUnitControlRoleFlags.DEPENDS_ON_HANDBRAKE_OFF) && tardis.isHandbrakeLocked()) {
            ModSounds.playSound(tardis.getWorld(), tardis.getMainConsolePosition(), SoundEvents.BLOCK_WOOD_PLACE, 1.0F, 1.0F);
            player.sendMessage(DWM.TEXTS.TARDIS_HANDBRAKE_ACTIVATED, true);
            return;
        }

        Text component = switch (role) {
            case DOORS, LIGHT, SHIELDS, SHIELDS_OXYGEN, SHIELDS_FIRE_PROOF, SHIELDS_MEDICAL, SHIELDS_MINING, SHIELDS_GRAVITATION, SHIELDS_SPECIAL, FUEL_HARVESTING, ENERGY_HARVESTING, HANDBRAKE -> Text.translatable(message + ((boolean) value ? ".active" : ".inactive"));
            case DIM_PREV, DIM_NEXT -> Text.translatable(message, "§e" + tardis.getDestinationExteriorDimension().getValue().getPath().replace("_", " ").toUpperCase());
            case FACING -> Text.translatable(message, Text.translatable(message + "." + (tardis.getDestinationExteriorFacing().ordinal() - 2)));
            case XSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getX());
            case YSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getY());
            case ZSET -> Text.translatable(message, "§e" + tardis.getDestinationExteriorPosition().getZ());
            case XYZSTEP -> Text.translatable(message, "§e" + tardis.getXYZStep());
            case SAFE_DIRECTION -> Text.translatable(message, Text.translatable(message + "." + value));
            case STARTER -> tardis.getFuelAmount() <= 0 && tardis.getEnergyAmount() <= 0 ? DWM.TEXTS.TARDIS_NOT_ENOUGH_FUEL : null;

            default -> message == null ? null : Text.translatable(message, value);
        };

        if (component != null) {
            this.playControlSound(role);
            player.sendMessage(component, true);
        }
    }
}

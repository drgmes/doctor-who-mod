package net.drgmes.dwm.blocks.tardis.consoles;

import java.util.ArrayList;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.caps.TardisLevelDataCapability;
import net.drgmes.dwm.common.screwdriver.Screwdriver;
import net.drgmes.dwm.common.tardis.consoles.TardisConsoleType;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoleTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.common.tardis.systems.TardisSystemFlight;
import net.drgmes.dwm.common.tardis.systems.TardisSystemMaterialization;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.drgmes.dwm.network.ClientboundTardisConsoleControlsUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleMonitorUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleScrewdriverSlotUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket;
import net.drgmes.dwm.network.ServerboundTardisConsoleInitPacket;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModPackets;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.SectionPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.saveddata.maps.MapBanner;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class BaseTardisConsoleBlockEntity extends BlockEntity {
    public TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
    public TardisConsoleType consoleType;

    public ItemStack screwdriverItemStack = ItemStack.EMPTY;
    public float tickInProgress = 0;
    public int monitorPage = 0;

    private final LazyOptional<ITardisLevelData> tardisDataHolder;
    private final ITardisLevelData tardisData;

    private ArrayList<TardisConsoleControlEntity> controls = new ArrayList<>();
    private int timeToInit = 0;
    private int monitorPageLength = 2;

    public BaseTardisConsoleBlockEntity(BlockEntityType<?> type, TardisConsoleType consoleType, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);

        this.consoleType = consoleType;
        this.tardisData = new TardisLevelDataCapability(this.level);
        this.tardisDataHolder = LazyOptional.of(() -> this.tardisData);
    }

	@Override
	public AABB getRenderBoundingBox() {
		return new AABB(this.worldPosition).inflate(3, 4, 3);
	}

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return this.saveWithoutMetadata();
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        this.controlsStorage.save(tag);

        tag.putInt("monitorPage", this.monitorPage);
        ContainerHelper.saveAllItems(tag, NonNullList.withSize(1, this.screwdriverItemStack), true);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.controlsStorage.load(tag);

        this.monitorPage = tag.getInt("monitorPage");

        NonNullList<ItemStack> itemStacks = NonNullList.withSize(1, ItemStack.EMPTY);
        if (tag.contains("Items", 9)) {
            ContainerHelper.loadAllItems(tag, itemStacks);
            this.screwdriverItemStack = itemStacks.get(0);
        }
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.timeToInit = 10;
    }

    @Override
    public void setRemoved() {
        this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            if (tardis.isValid()) tardis.getConsoleTiles().remove(this);
        });

        this.removeControls();
        super.setRemoved();
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        return cap == ModCapabilities.TARDIS_DATA ? this.tardisDataHolder.cast() : super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        this.tardisDataHolder.invalidate();
    }

    public void init() {
        this.createControls();

        if (TardisHelper.isTardisDimension(this.level)) {
            if (!this.level.isClientSide) {
                this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                    if (!tardis.isValid()) return;
                    if (tardis.getConsoleTiles().contains(this)) return;

                    tardis.getConsoleTiles().add(this);
                    tardis.updateConsoleTiles();
                });
            }
            else {
                ModPackets.INSTANCE.sendToServer(new ServerboundTardisConsoleInitPacket(this.worldPosition));
            }
        }
    }

    public void tick() {
        if (this.timeToInit > 0) {
            --this.timeToInit;
            if (this.timeToInit == 0) this.init();
        }

        this.animateControls();

        this.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            if (provider.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem && flightSystem.inProgress()) {
                this.tickInProgress++;
                this.tickInProgress %= 60;
            }

            if (provider.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem && materializationSystem.inProgress()) {
                this.tickInProgress++;
                this.tickInProgress %= 60;
            }
        });
    }

    public void unloadAll() {
        this.level.getCapability(ModCapabilities.TARDIS_CHUNK_LOADER).ifPresent((tardis) -> {
            SectionPos pos = SectionPos.of(this.worldPosition);
            int radius = 3;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    tardis.remove(pos.offset(x, 0, z), this.worldPosition);
                }
            }
        });
    }

    public void loadAll() {
        this.level.getCapability(ModCapabilities.TARDIS_CHUNK_LOADER).ifPresent((tardis) -> {
            SectionPos pos = SectionPos.of(this.worldPosition);
            int radius = 3;

            for (int x = -radius; x <= radius; x++) {
                for (int z = -radius; z <= radius; z++) {
                    tardis.add(pos.offset(x, 0, z), this.worldPosition);
                }
            }
        });
    }

    public void sendMonitorUpdatePacket() {
        ClientboundTardisConsoleMonitorUpdatePacket packet = new ClientboundTardisConsoleMonitorUpdatePacket(this.worldPosition, this.monitorPage);
        ModPackets.send(level.getChunkAt(this.worldPosition), packet);
    }

    public void sendControlsUpdatePacket() {
        ClientboundTardisConsoleControlsUpdatePacket packet = new ClientboundTardisConsoleControlsUpdatePacket(this.worldPosition, this.controlsStorage);
        ModPackets.send(level.getChunkAt(this.worldPosition), packet);
    }

    public void sendScrewdriverSlotUpdatePacket() {
        ClientboundTardisConsoleScrewdriverSlotUpdatePacket packet = new ClientboundTardisConsoleScrewdriverSlotUpdatePacket(this.worldPosition, this.screwdriverItemStack);
        ModPackets.send(level.getChunkAt(this.worldPosition), packet);
    }

    public void sendTelepathicInterfaceLocationsOpenPacket(ServerPlayer player) {
        ModPackets.send(player, new ClientboundTardisConsoleTelepathicInterfaceLocationsOpenPacket(this.worldPosition));
    }

    public void sendTelepathicInterfaceMapBannersOpenPacket(ServerPlayer player, MapItemSavedData mapData) {
        ModPackets.send(player, new ClientboundTardisConsoleTelepathicInterfaceMapBannersOpenPacket(this.worldPosition, mapData));
    }

    public void useControl(TardisConsoleControlEntry control, InteractionHand hand, Entity entity) {
        if (!(entity instanceof ServerPlayer player)) return;

        // Monitor
        if (control.role == TardisConsoleControlRoles.MONITOR && hand == InteractionHand.OFF_HAND) {
            System.out.println("Monitor"); // TODO
            return;
        }

        // Telepathic Interface
        if (control.role == TardisConsoleControlRoles.TELEPATHIC_INTERFACE && hand == InteractionHand.OFF_HAND) {
            ItemStack mainHandItemStack = player.getMainHandItem();
            ItemStack offHandItemStack = player.getOffhandItem();

            if (mainHandItemStack.getItem() instanceof MapItem || offHandItemStack.getItem() instanceof MapItem) {
                ItemStack itemStack = mainHandItemStack.getItem() instanceof MapItem ? mainHandItemStack : offHandItemStack;
                MapItemSavedData mapData = MapItem.getSavedData(itemStack, level);
                if (mapData == null) return;

                this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                    if (!tardis.isValid()) return;
                    if (mapData.dimension == tardis.getLevel().dimension()) return;

                    if (tardis.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem && flightSystem.inProgress()) {
                        return;
                    }

                    if (tardis.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem && materializationSystem.inProgress()) {
                        return;
                    }

                    BlockPos destExteriorPosition = tardis.getDestinationExteriorPosition();
                    BlockPos blockPos = new BlockPos(mapData.x, destExteriorPosition.getY(), mapData.z);

                    if (mapData.getBanners().size() > 1) {
                        this.sendTelepathicInterfaceMapBannersOpenPacket(player, mapData);
                        return;
                    }
                    else if (mapData.getBanners().size() == 1 && mapData.getBanners().toArray()[0] instanceof MapBanner banner) {
                        String color = "\u00A7e" + banner.getColor().getName().toUpperCase().replace("_", " ");
                        player.displayClientMessage(Component.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.map.loaded.banner", color), true);
                        blockPos = banner.getPos();
                    }
                    else {
                        player.displayClientMessage(Component.translatable("message." + DWM.MODID + ".tardis.telepathic_interface.map.loaded"), true);
                    }

                    tardis.setDestinationDimension(mapData.dimension);
                    tardis.setDestinationPosition(blockPos);
                    tardis.updateConsoleTiles();
                });

                return;
            }

            this.sendTelepathicInterfaceLocationsOpenPacket(player);
            return;
        }

        // Screwdriver Slot
        if (control.role == TardisConsoleControlRoles.SCREWDRIVER_SLOT && hand == InteractionHand.OFF_HAND) {
            Screwdriver.assingTardisUUID(this.screwdriverItemStack, this.level);

            boolean isChanged = false;

            if (this.screwdriverItemStack.isEmpty()) {
                ItemStack mainHandItem = player.getMainHandItem();
                ItemStack offHandItem = player.getOffhandItem();

                if (mainHandItem.getItem() instanceof ScrewdriverItem) {
                    this.screwdriverItemStack = mainHandItem;
                    player.setItemInHand(InteractionHand.MAIN_HAND, ItemStack.EMPTY);
                    isChanged = true;
                }
                else if (offHandItem.getItem() instanceof ScrewdriverItem) {
                    this.screwdriverItemStack = offHandItem;
                    player.setItemInHand(InteractionHand.OFF_HAND, ItemStack.EMPTY);
                    isChanged = true;
                }
            }
            else if (player.getMainHandItem().isEmpty()) {
                player.setItemInHand(InteractionHand.MAIN_HAND, this.screwdriverItemStack);
                this.screwdriverItemStack = ItemStack.EMPTY;
                isChanged = true;
            }
            else if (player.getInventory().add(this.screwdriverItemStack)) {
                this.screwdriverItemStack = ItemStack.EMPTY;
                isChanged = true;
            }

            if (isChanged) {
                Screwdriver.assingTardisUUID(this.screwdriverItemStack, this.level);
                this.sendScrewdriverSlotUpdatePacket();
                this.setChanged();
            }

            return;
        }

        if (this.controlsStorage.update(control.role, hand)) {
            // Next Screen Page
            int monitorPageNext = (int) controlsStorage.get(TardisConsoleControlRoles.MONITOR_PAGE_NEXT);
            if (monitorPageNext != 0) this.monitorPage = (this.monitorPage + 1) % this.monitorPageLength;

            // Prev Screen Page
            int monitorPagePrev = (int) controlsStorage.get(TardisConsoleControlRoles.MONITOR_PAGE_PREV);
            if (monitorPagePrev != 0) this.monitorPage = this.monitorPage < 1 ? this.monitorPageLength - 1 : this.monitorPage - 1;

            if (monitorPagePrev != 0 || monitorPageNext != 0) this.sendMonitorUpdatePacket();
            this.setChanged();

            if (!TardisHelper.isTardisDimension(this.level)) {
                this.sendControlsUpdatePacket();
                return;
            }

            this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
                if (tardis.isValid()) tardis.applyControlsStorageToData(this.controlsStorage);

                try {
                    this.displayNotification(tardis, this.controlsStorage, control.role, player);
                } catch (Exception e) {
                }
            });
        }
    }

    private void createControls() {
        if (this.controls.size() == this.consoleType.controlEntries.size()) return;
        if (this.level.isClientSide) return;
        this.removeControls();

        for (TardisConsoleControlEntry controlEntry : this.consoleType.controlEntries.values()) {
            this.controls.add(controlEntry.createEntity(this, this.level, this.worldPosition));
        }

        this.setChanged();
    }

    private void animateControls() {
        boolean isChanged = false;
        for (TardisConsoleControlEntry controlEntry : this.consoleType.controlEntries.values()) {
            if (controlEntry.role.type != TardisConsoleControlRoleTypes.ANIMATION && controlEntry.role.type != TardisConsoleControlRoleTypes.ANIMATION_DIRECT) {
                continue;
            }

            int value = (int) this.controlsStorage.get(controlEntry.role);
            int direction = value > 0 ? 1 : (value < 0 ? -1 : 0);

            this.controlsStorage.values.put(controlEntry.role, value - direction);

            if (value != 0 && value == direction) isChanged = true;
            else if (controlEntry.type == TardisConsoleControlEntryTypes.ROTATOR && value != 0) isChanged = true;
        }

        if (isChanged) this.setChanged();
    }

    private void removeControls() {
        for (TardisConsoleControlEntity control : this.controls) control.discard();
        this.controls.clear();
    }

    private void displayNotification(ITardisLevelData provider, TardisConsoleControlsStorage controlsStorage, TardisConsoleControlRoles role, Player player) {
        Object value = controlsStorage.get(role);
        boolean isInFlight = false;
        boolean isMaterialized = false;

        // Flight
        if (provider.getSystem(TardisSystemFlight.class) instanceof TardisSystemFlight flightSystem) {
            isInFlight = flightSystem.inProgress();
        }

        // Materialization
        if (provider.getSystem(TardisSystemMaterialization.class) instanceof TardisSystemMaterialization materializationSystem) {
            isMaterialized = materializationSystem.isMaterialized();
        }

        Component component = switch (role) {
            case DOORS, SHIELDS, LIGHT, ENERGY_ARTRON_HARVESTING, ENERGY_FORGE_HARVESTING
            -> !isMaterialized ? null : Component.translatable(role.message + ((boolean) value ? ".active" : ".inactive"));

            case HANDBRAKE
            -> Component.translatable(role.message + ((boolean) value ? ".active" : ".inactive"));

            case SAFE_DIRECTION
            -> Component.translatable(role.message, Component.translatable(role.message + "." + value));

            case FACING
            -> isInFlight ? null : Component.translatable(role.message, Component.translatable(role.message + "." + (provider.getDestinationExteriorFacing().ordinal() - 2)));

            case XYZSTEP
            -> isInFlight ? null : Component.translatable(role.message, "\u00A7e" + provider.getXYZStep());

            case XSET
            -> isInFlight ? null : Component.translatable(role.message, "\u00A7e" + provider.getDestinationExteriorPosition().getX());

            case YSET
            -> isInFlight ? null : Component.translatable(role.message, "\u00A7e" + provider.getDestinationExteriorPosition().getY());

            case ZSET
            -> isInFlight ? null : Component.translatable(role.message, "\u00A7e" + provider.getDestinationExteriorPosition().getZ());

            case DIM_PREV, DIM_NEXT
            -> isInFlight ? null : Component.translatable(role.message, "\u00A7e" + provider.getDestinationExteriorDimension().location().getPath().replace("_", " ").toUpperCase());

            default
            -> isInFlight || role.message == null ? null : Component.translatable(role.message, value);
        };

        if (component != null) {
            player.displayClientMessage(component, true);
        }
    }
}

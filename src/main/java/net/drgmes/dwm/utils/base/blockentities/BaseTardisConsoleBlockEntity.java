package net.drgmes.dwm.utils.base.blockentities;

import java.util.ArrayList;

import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.caps.TardisLevelCapability;
import net.drgmes.dwm.common.tardis.consoles.TardisConsoleType;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoleTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.network.ClientboundTardisConsoleControlsUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleMonitorUpdatePacket;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class BaseTardisConsoleBlockEntity extends BlockEntity {
    public TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
    public TardisConsoleType consoleType;
    public int monitorPage = 0;

    private final LazyOptional<ITardisLevelData> tardisDataHolder;
    private ITardisLevelData tardisData;

    private ArrayList<TardisConsoleControlEntity> controls = new ArrayList<>();
    private int timeToSpawnControls = 0;
    private int monitorPageLength = 2;

    public BaseTardisConsoleBlockEntity(BlockEntityType<?> type, TardisConsoleType consoleType, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);

        this.consoleType = consoleType;
        this.tardisData = new TardisLevelCapability(this.level);
        this.tardisDataHolder = LazyOptional.of(() -> this.tardisData);
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
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.controlsStorage.load(tag);

        this.monitorPage = tag.getInt("monitorPage");
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.timeToSpawnControls = 10;
    }

    @Override
    public void setRemoved() {
        this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
            if (levelProvider.isValid()) levelProvider.getConsoleTiles().remove(this);
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
        if (!this.level.isClientSide && this.checkTileIsInATardis()) {
            this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
                if (!levelProvider.isValid()) return;

                levelProvider.getConsoleTiles().add(this);
                levelProvider.updateConsoleTiles();
            });
        }
    }

    public void tick() {
        if (!level.isClientSide) {
            if (this.timeToSpawnControls > 0) {
                --this.timeToSpawnControls;

                if (this.timeToSpawnControls == 0) {
                    this.createControls();
                    this.init();
                }
            }

            this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
                if (levelProvider.getMainConsoleTile() == this) levelProvider.tick();
            });
        }

        this.animateControls();
    }

    public void sendMonitorUpdatePacket() {
        ClientboundTardisConsoleMonitorUpdatePacket packet = new ClientboundTardisConsoleMonitorUpdatePacket(this.worldPosition, this.monitorPage);
        ModPackets.send(level.getChunkAt(this.worldPosition), packet);
    }

    public void sendControlsUpdatePacket() {
        ClientboundTardisConsoleControlsUpdatePacket packet = new ClientboundTardisConsoleControlsUpdatePacket(this.worldPosition, this.controlsStorage);
        ModPackets.send(level.getChunkAt(this.worldPosition), packet);
    }

    public void useControl(TardisConsoleControlEntry control, InteractionHand hand) {
        // Monitor
        if (hand == InteractionHand.OFF_HAND && control.role == TardisConsoleControlRoles.MONITOR) {
            System.out.println("Monitor"); // TODO
            return;
        }

        // Telepatic Interface
        if (hand == InteractionHand.OFF_HAND && control.role == TardisConsoleControlRoles.TELEPATIC_INTERFACE) {
            System.out.println("Telepatic Interface"); // TODO
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

            if (!this.checkTileIsInATardis()) {
                this.sendControlsUpdatePacket();
                return;
            }

            this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
                if (levelProvider.isValid()) levelProvider.applyControlsStorageToData(this.controlsStorage);
            });
        }
    }

    private void createControls() {
        if (this.controls.size() == this.consoleType.controlEntries.size()) return;
        this.removeControls();

        for (TardisConsoleControlEntry controlEntry : this.consoleType.controlEntries.values()) {
            this.controls.add(controlEntry.createEntity(this, this.level, this.worldPosition));
        }

        this.setChanged();
    }

    private void animateControls() {
        boolean isChanged = false;
        for (TardisConsoleControlEntry controlEntry : this.consoleType.controlEntries.values()) {
            if (controlEntry.role.type != TardisConsoleControlRoleTypes.ANIMATION && controlEntry.role.type != TardisConsoleControlRoleTypes.ANIMATION_DIRECT) continue;

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

    private boolean checkTileIsInATardis() {
        return this.level != null && this.level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS);
    }
}

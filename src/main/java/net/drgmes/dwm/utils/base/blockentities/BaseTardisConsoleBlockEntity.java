package net.drgmes.dwm.utils.base.blockentities;

import java.util.ArrayList;

import net.drgmes.dwm.caps.ITardisLevelData;
import net.drgmes.dwm.caps.TardisLevelCapability;
import net.drgmes.dwm.common.tardis.consoles.TardisConsoleType;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoleTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.network.ClientboundTardisConsoleControlsUpdatePacket;
import net.drgmes.dwm.network.ClientboundTardisConsoleWorldDataUpdatePacket;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

public abstract class BaseTardisConsoleBlockEntity extends BlockEntity {
    public TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
    public final TardisConsoleType consoleType;

    final private LazyOptional<ITardisLevelData> tardisDataHolder;
    private ITardisLevelData tardisData;

    private ArrayList<TardisConsoleControlEntity> controls = new ArrayList<>();
    private int timeToSpawnControls = 0;

    public BaseTardisConsoleBlockEntity(BlockEntityType<?> type, TardisConsoleType consoleType, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);

        this.consoleType = consoleType;
        this.tardisData = new TardisLevelCapability();
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
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        this.controlsStorage.load(tag);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        this.timeToSpawnControls = 10;

        if (!this.level.isClientSide && this.checkTileIsInATardis()) {
            this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
                if (!levelProvider.isValid()) return;

                this.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                    CompoundTag tag = levelProvider.serializeNBT();
                    provider.deserializeNBT(tag);
                    this.sendWorldDataUpdatePacket(tag);
                });
            });
        }
    }

    @Override
    public void setRemoved() {
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

    public void tick(Level level, BlockPos blockPos, BlockState blockState) {
        if (this.timeToSpawnControls > 0) {
            --this.timeToSpawnControls;
            if (this.timeToSpawnControls == 0) this.createControls();
        }

        this.animateControls();
    }

    public void useControl(TardisConsoleControlEntry control, InteractionHand hand) {
        if (this.controlsStorage.update(control.role, hand)) {
            this.sendControlsUpdatePacket();
            this.setChanged();

            if (!this.checkTileIsInATardis()) return;

            this.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((levelProvider) -> {
                if (!levelProvider.isValid()) return;
                levelProvider.applyControlsStorage(this.controlsStorage);

                this.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                    CompoundTag tag = levelProvider.serializeNBT();
                    provider.deserializeNBT(tag);
                    this.sendWorldDataUpdatePacket(tag);
                    this.setChanged();
                });
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
            if (value == direction || controlEntry.type == TardisConsoleControlEntryTypes.ROTATOR) isChanged = true;
        }

        if (isChanged) {
            this.sendControlsUpdatePacket();
            this.setChanged();
        }
    }

    private void removeControls() {
        for (TardisConsoleControlEntity control : this.controls) control.discard();
        this.controls.clear();
    }

    private boolean checkTileIsInATardis() {
        return this.level != null && this.level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS);
    }

    private void sendControlsUpdatePacket() {
        ClientboundTardisConsoleControlsUpdatePacket packet = new ClientboundTardisConsoleControlsUpdatePacket(this.worldPosition, this.controlsStorage);
        ModPackets.send(level.getChunkAt(this.worldPosition), packet);
    }

    private void sendWorldDataUpdatePacket(CompoundTag tag) {
        this.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            ClientboundTardisConsoleWorldDataUpdatePacket packet = new ClientboundTardisConsoleWorldDataUpdatePacket(this.worldPosition, tag);
            ModPackets.send(level.getChunkAt(this.worldPosition), packet);
        });
    }
}

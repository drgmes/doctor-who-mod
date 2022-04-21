package net.drgmes.dwm.utils.base.blockentities;

import java.util.ArrayList;

import net.drgmes.dwm.common.tardis.TardisConsoleType;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntry;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlEntryTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoleTypes;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.entities.tardis.consoles.controls.TardisConsoleControlEntity;
import net.drgmes.dwm.network.ClientboundTardisConsoleUpdatePacket;
import net.drgmes.dwm.setup.ModPackets;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BaseTardisConsoleBlockEntity extends BlockEntity {
    public TardisConsoleControlsStorage controlsStorage = new TardisConsoleControlsStorage();
    public final TardisConsoleType consoleType;

    private ArrayList<TardisConsoleControlEntity> controls = new ArrayList<>();
    private int timeToSpawnControls = 0;

    public BaseTardisConsoleBlockEntity(BlockEntityType<?> type, TardisConsoleType consoleType, BlockPos blockPos, BlockState blockState) {
        super(type, blockPos, blockState);
        this.consoleType = consoleType;
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
    }

    @Override
    public void setRemoved() {
        this.removeControls();
        super.setRemoved();
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
            this.sendUpdatePacket();
        }
    }

    private void createControls() {
        Level level = this.getLevel();
        BlockPos blockPos = this.getBlockPos();

        if (this.controls.size() == this.consoleType.controlEntries.size()) return;
        this.removeControls();

        for (TardisConsoleControlEntry controlEntry : this.consoleType.controlEntries.values()) {
            this.controls.add(controlEntry.createEntity(this, level, blockPos));
        }

        this.setChanged();
    }

    private void animateControls() {
        boolean isChanged = false;
        for (TardisConsoleControlEntry controlEntry : this.consoleType.controlEntries.values()) {
            if (controlEntry.role.type != TardisConsoleControlRoleTypes.ANIMATION) continue;

            int value = (int) this.controlsStorage.get(controlEntry.role);
            if (value > 0) this.controlsStorage.values.put(controlEntry.role, value - 1);
            if (value == 1 || controlEntry.type == TardisConsoleControlEntryTypes.ROTATOR) isChanged = true;
        }

        if (isChanged) this.sendUpdatePacket();
    }

    private void removeControls() {
        for (TardisConsoleControlEntity control : this.controls) control.discard();
        this.controls.clear();
    }

    private void sendUpdatePacket() {
        BlockPos blockPos = this.getBlockPos();
        ClientboundTardisConsoleUpdatePacket packet = new ClientboundTardisConsoleUpdatePacket(blockPos, this.controlsStorage);
        ModPackets.send(level.getChunkAt(blockPos), packet);
        this.setChanged();
    }
}

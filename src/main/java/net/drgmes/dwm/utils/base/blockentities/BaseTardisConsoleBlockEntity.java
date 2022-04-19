package net.drgmes.dwm.utils.base.blockentities;

import java.util.ArrayList;

import net.drgmes.dwm.common.tardis.TardisConsoleType;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControl;
import net.drgmes.dwm.common.tardis.consoles.controls.TardisConsoleControlRoles;
import net.drgmes.dwm.entities.tardis.consolecontrol.TardisConsoleControlEntity;
import net.drgmes.dwm.network.ClientboundEnergyTardisConsoleUpdatePacket;
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
    private ArrayList<TardisConsoleControlEntity> controls = new ArrayList<TardisConsoleControlEntity>();
    private TardisConsoleType consoleType;
    private int timeToSpawnControls = 0;

    public boolean controlDoor = false;
    public boolean controlShields = false;
    public boolean controlHandbrake = false;
    public boolean controlStarter = false;
    public boolean controlRandomizer = false;
    public int controlFacing = 0;

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

        tag.putBoolean("controlDoor", this.controlDoor);
        tag.putBoolean("controlShields", this.controlShields);
        tag.putBoolean("controlHandbrake", this.controlHandbrake);
        tag.putBoolean("controlStarter", this.controlStarter);
        tag.putBoolean("controlRandomizer", this.controlRandomizer);
        tag.putInt("controlFacing", this.controlFacing);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);

        this.controlDoor = tag.getBoolean("controlDoor");
        this.controlShields = tag.getBoolean("controlShields");
        this.controlHandbrake = tag.getBoolean("controlHandbrake");
        this.controlStarter = tag.getBoolean("controlStarter");
        this.controlRandomizer = tag.getBoolean("controlRandomizer");
        this.controlFacing = tag.getInt("controlFacing");
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

            if (this.timeToSpawnControls == 0) {
                this.createControls();
            }
        }
    }

    private void createControls() {
        Level level = this.getLevel();
        BlockPos blockPos = this.getBlockPos();

        if (this.controls.size() == this.consoleType.controls.size()) return;
        this.removeControls();

        for (TardisConsoleControl control : this.consoleType.controls) {
            this.controls.add(control.createEntity(this, level, blockPos));
        }

        this.setChanged();
    }

    public void removeControls() {
        for (TardisConsoleControlEntity control : this.controls) control.discard();
        this.controls.clear();
    }

    public void useControl(TardisConsoleControl control, InteractionHand hand) {
        BlockPos blockPos = this.getBlockPos();
        boolean isChanged = false;

        if (control.role == TardisConsoleControlRoles.DOORS) {
            this.controlDoor = !this.controlDoor;
            isChanged = true;
        }
        else if (control.role == TardisConsoleControlRoles.SHIELDS) {
            this.controlShields = !this.controlShields;
            isChanged = true;
        }
        else if (control.role == TardisConsoleControlRoles.HANDBRAKE) {
            this.controlHandbrake = !this.controlHandbrake;
            isChanged = true;
        }
        else if (control.role == TardisConsoleControlRoles.STARTER) {
            this.controlStarter = !this.controlStarter;
            isChanged = true;
        }
        else if (control.role == TardisConsoleControlRoles.FACING) {
            this.controlFacing = (this.controlFacing + (hand == InteractionHand.MAIN_HAND ? 1 : -1)) % 4;
            isChanged = true;
        }

        if (isChanged) {
            ClientboundEnergyTardisConsoleUpdatePacket packet = new ClientboundEnergyTardisConsoleUpdatePacket(
                blockPos,
                this.controlDoor,
                this.controlShields,
                this.controlHandbrake,
                this.controlStarter,
                this.controlRandomizer,
                this.controlFacing
            );

            ModPackets.send(level.getChunkAt(blockPos), packet);
            this.setChanged();
        }
    }
}

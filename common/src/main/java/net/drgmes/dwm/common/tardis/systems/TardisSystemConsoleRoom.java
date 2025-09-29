package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.enums.TardisExteriorAction;
import net.drgmes.dwm.network.client.TardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.UUID;

public class TardisSystemConsoleRoom extends TardisBaseSystem {
    private enum EStep {
        NONE,
        INITED,
        PROCESSING,
    }

    private enum EResult {
        NONE,
        SUCCESS,
        FAIL,
    }

    private EStep step = EStep.NONE;
    private EResult result = EResult.NONE;

    private UUID initiatorId;
    private String consoleRoomId;

    private int tick = -1;
    private int soundTick = -1;

    public TardisSystemConsoleRoom(TardisStateManager tardis) {
        super(tardis);
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public boolean inProgress() {
        return this.step != EStep.NONE;
    }

    @Override
    public void readNbt(NbtCompound tag) {
        if (tag.contains("step")) this.step = EStep.valueOf(tag.getString("step"));
        if (tag.contains("result")) this.result = EResult.valueOf(tag.getString("result"));
        if (tag.contains("initiatorId")) this.initiatorId = tag.getUuid("initiatorId");
        if (tag.contains("consoleRoomId")) this.consoleRoomId = tag.getString("consoleRoomId");
        if (tag.contains("tick")) this.tick = tag.getInt("tick");
        if (tag.contains("soundTick")) this.soundTick = tag.getInt("soundTick");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if (this.initiatorId != null) tag.putUuid("initiatorId", this.initiatorId);
        if (this.consoleRoomId != null) tag.putString("consoleRoomId", this.consoleRoomId);

        tag.putString("step", this.step.name());
        tag.putString("result", this.result.name());
        tag.putInt("tick", this.tick);
        tag.putInt("soundTick", this.soundTick);

        return tag;
    }

    @Override
    public void tick() {
        if (!this.isEnabled() || !this.inProgress()) return;
        if (this.tick > 0) this.tick -= 1;

        switch (this.step) {
            case INITED -> {
                this.playBellSound();

                List<ServerPlayerEntity> players = this.tardis.getWorld().getPlayers();
                if (this.tick == 0) this.tick = DWM.TIMINGS.RECONSTRUCTION_NOTIFICATION;

                if (players.isEmpty()) {
                    this.step = EStep.PROCESSING;
                    this.tick = Math.max(DWM.TIMINGS.RECONSTRUCTION_DURATION, 120);
                    this.soundTick = -1;

                    this.tardis.setDoorsOpenState(false);
                    this.tardis.setDoorsLockState(true, null);
                    this.sendExteriorUpdatePacket(TardisExteriorAction.PULSE);
                    this.notify(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_STARTED, this.initiatorId);
                }
                else if (this.tick == DWM.TIMINGS.RECONSTRUCTION_NOTIFICATION) {
                    players.forEach((player) -> {
                        player.sendMessage(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_PREPARE, true);
                    });
                }
            }

            case PROCESSING -> {
                if (this.tick % 20 == 0) {
                    this.notify(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_TIMER.apply((float) this.tick / 20), this.initiatorId);
                }

                if (this.tick == 100) {
                    this.tardis.getConsoleRoom().remove(this.tardis);
                }
                else if (this.tick == 20) {
                    TardisConsoleRoomEntry consoleRoom = TardisConsoleRooms.CONSOLE_ROOMS.get(this.consoleRoomId);

                    if (consoleRoom.place(tardis)) {
                        this.result = EResult.SUCCESS;
                        this.tardis.setConsoleRoom(consoleRoom);
                        this.tardis.updateRoomEntrancePortals();
                    }
                    else {
                        this.result = EResult.FAIL;
                    }
                }
                else if (this.tick == 0) {
                    switch (this.result) {
                        case SUCCESS -> this.notify(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_FINISHED, this.initiatorId);
                        case FAIL -> this.notify(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_FAILED, this.initiatorId);
                    }

                    this.reset();
                    this.sendExteriorUpdatePacket(TardisExteriorAction.NORMALIZE);
                    this.tardis.setDoorsLockState(false, null);
                    this.tardis.markConsoleTilesUpdated();
                }
            }
        }
    }

    public boolean init(String consoleRoomId, UUID initiatorId) {
        if (!this.isEnabled() || this.inProgress() || consoleRoomId == null) return false;
        if (!TardisConsoleRooms.CONSOLE_ROOMS.containsKey(consoleRoomId)) return false;

        this.step = EStep.INITED;
        this.result = EResult.NONE;
        this.initiatorId = initiatorId;
        this.consoleRoomId = consoleRoomId;
        this.tick = 0;
        this.soundTick = 0;
        return true;
    }

    public void reset() {
        this.step = EStep.NONE;
        this.result = EResult.NONE;
        this.initiatorId = null;
        this.consoleRoomId = null;
        this.tick = -1;
        this.soundTick = -1;
    }

    private void playBellSound() {
        if (this.soundTick > 0) {
            this.soundTick -= 1;
        }
        else if (this.soundTick == 0) {
            this.soundTick = DWM.TIMINGS.RECONSTRUCTION_LOOP;
            ModSounds.playTardisBellSound(this.tardis.getWorld(), this.tardis.getMainConsolePosition());
        }
    }

    private void sendExteriorUpdatePacket(TardisExteriorAction exteriorAction) {
        BlockPos exteriorBlockPos = this.tardis.getCurrentExteriorPosition();
        ServerWorld exteriorWorld = this.tardis.getExteriorWorld();
        if (exteriorWorld == null) return;

        if (exteriorWorld.getBlockEntity(exteriorBlockPos) instanceof BaseTardisExteriorBlockEntity tardisExteriorBlockEntity) {
            switch (exteriorAction) {
                case NORMALIZE -> tardisExteriorBlockEntity.normalize();
                case PULSE -> tardisExteriorBlockEntity.pulse();
            }
        }

        new TardisExteriorUpdatePacket(exteriorBlockPos, exteriorAction)
            .sendToAll(exteriorWorld.getServer());
    }
}

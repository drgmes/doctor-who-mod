package net.drgmes.dwm.common.tardis.systems;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRoomEntry;
import net.drgmes.dwm.common.tardis.consolerooms.TardisConsoleRooms;
import net.drgmes.dwm.enums.TardisExteriorAction;
import net.drgmes.dwm.network.client.TardisExteriorUpdatePacket;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.math.BlockPos;

import java.util.List;
import java.util.UUID;

public class TardisSystemConsoleRoom implements ITardisSystem {
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

    private final TardisStateManager tardis;

    private UUID initiatorId;
    private String consoleRoomId;
    private EStep step = EStep.NONE;
    private EResult result = EResult.NONE;

    private float tick = -1;
    private float soundTick = -1;

    public TardisSystemConsoleRoom(TardisStateManager tardis) {
        this.tardis = tardis;
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
        if (tag.contains("initiatorId")) this.initiatorId = tag.getUuid("initiatorId");
        if (tag.contains("consoleRoomId")) this.consoleRoomId = tag.getString("consoleRoomId");
        if (tag.contains("step")) this.step = EStep.valueOf(tag.getString("step"));
        if (tag.contains("result")) this.result = EResult.valueOf(tag.getString("result"));
        if (tag.contains("tick")) this.tick = tag.getFloat("tick");
        if (tag.contains("soundTick")) this.soundTick = tag.getFloat("soundTick");
    }

    @Override
    public NbtCompound writeNbt(NbtCompound tag) {
        if (this.initiatorId != null) tag.putUuid("initiatorId", this.initiatorId);
        if (this.consoleRoomId != null) tag.putString("consoleRoomId", this.consoleRoomId);
        tag.putString("step", this.step.name());
        tag.putString("result", this.result.name());
        tag.putFloat("tick", this.tick);
        tag.putFloat("soundTick", this.soundTick);
        return tag;
    }

    @Override
    public void tick() {
        if (!this.inProgress()) return;
        if (this.tick > 0) this.tick -= 1;

        this.playSound();

        switch (this.step) {
            case INITED -> {
                List<ServerPlayerEntity> players = this.tardis.getWorld().getPlayers();
                if (this.tick == 0) this.tick = DWM.TIMINGS.RECONSTRUCTION_NOTIFICATION;

                if (players.isEmpty()) {
                    this.step = EStep.PROCESSING;
                    this.tick = Math.max(DWM.TIMINGS.RECONSTRUCTION_DURATION, 120);

                    this.tardis.setDoorsOpenState(false);
                    this.tardis.setDoorsLockState(true, null);
                    this.sendExteriorUpdatePacket(TardisExteriorAction.PULSE);
                    this.notify(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_STARTED);
                }
                else if (this.tick == DWM.TIMINGS.RECONSTRUCTION_NOTIFICATION) {
                    players.forEach((player) -> {
                        player.sendMessage(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_PREPARE, true);
                    });
                }
            }

            case PROCESSING -> {
                if (this.tick % 20 == 0) {
                    this.notify(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_TIMER.apply(this.tick / 20));
                }

                if (this.tick == 100) {
                    this.tardis.getConsoleRoom().remove(this.tardis);
                }
                else if (this.tick == 20) {
                    TardisConsoleRoomEntry consoleRoom = TardisConsoleRooms.CONSOLE_ROOMS.get(this.consoleRoomId);

                    if (consoleRoom.place(tardis)) {
                        this.result = EResult.SUCCESS;
                        this.tardis.setConsoleRoom(consoleRoom);
                        this.tardis.markConsoleTilesUpdated();
                        this.tardis.updateRoomEntrancePortals();
                    }
                    else {
                        this.result = EResult.FAIL;
                    }
                }
                else if (this.tick == 0) {
                    switch (this.result) {
                        case SUCCESS -> this.notify(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_FINISHED);
                        case FAIL -> this.notify(DWM.TEXTS.ARS_CONSOLE_ROOM_REBUILD_FAILED);
                    }

                    this.reset();
                    this.sendExteriorUpdatePacket(TardisExteriorAction.NORMALIZE);
                    this.tardis.setDoorsLockState(false, null);
                }
            }
        }
    }

    public boolean init(String consoleRoomId, PlayerEntity initiator) {
        if (!this.isEnabled() || this.inProgress() || consoleRoomId == null) return false;
        if (!TardisConsoleRooms.CONSOLE_ROOMS.containsKey(consoleRoomId)) return false;

        this.step = EStep.INITED;
        this.result = EResult.NONE;
        this.tick = 0;
        this.soundTick = 0;
        this.consoleRoomId = consoleRoomId;
        this.initiatorId = initiator.getUuid();
        return true;
    }

    public void reset() {
        this.step = EStep.NONE;
        this.result = EResult.NONE;
        this.tick = -1;
        this.soundTick = -1;
        this.consoleRoomId = null;
        this.initiatorId = null;
    }

    private void notify(Text message) {
        if (this.initiatorId == null) return;

        PlayerEntity initiator = this.tardis.getWorld().getServer().getPlayerManager().getPlayer(this.initiatorId);
        if (initiator != null) initiator.sendMessage(message, true);
    }

    private void playSound() {
        if (this.soundTick < 0) return;
        if (this.soundTick > 0) this.soundTick -= 1;
        else {
            this.soundTick = DWM.TIMINGS.RECONSTRUCTION_LOOP;
            ModSounds.playTardisBellSound(tardis.getWorld(), tardis.getMainConsolePosition());
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
            .sendToChunkListeners(exteriorWorld.getWorldChunk(exteriorBlockPos));
    }
}

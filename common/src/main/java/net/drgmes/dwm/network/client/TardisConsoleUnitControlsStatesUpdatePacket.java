package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.common.tardis.consoleunits.controls.TardisConsoleControlsStorage;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitControlsStatesUpdatePacket extends BaseS2CMessage {
    private final BlockPos blockPos;
    private final NbtCompound tag;

    public TardisConsoleUnitControlsStatesUpdatePacket(BlockPos blockPos, NbtCompound tag) {
        this.blockPos = blockPos;
        this.tag = tag;
    }

    public static TardisConsoleUnitControlsStatesUpdatePacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitControlsStatesUpdatePacket(buf.readBlockPos(), buf.readNbt());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_CONTROLS_STATES_UPDATE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
        buf.writeNbt(this.tag);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        if (mc.world.getBlockEntity(this.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            tardisConsoleUnitBlockEntity.controlsStorage.load(this.tag);
        }
    }
}

package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.setup.ModNetwork;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.util.math.BlockPos;

public class TardisConsoleUnitSoundPacket extends BaseC2SMessage {
    private final BlockPos blockPos;

    public TardisConsoleUnitSoundPacket(BlockPos blockPos) {
        this.blockPos = blockPos;
    }

    public static TardisConsoleUnitSoundPacket create(PacketByteBuf buf) {
        return new TardisConsoleUnitSoundPacket(buf.readBlockPos());
    }

    @Override
    public MessageType getType() {
        return ModNetwork.TARDIS_CONSOLE_UNIT_SOUND;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeBlockPos(this.blockPos);
    }

    @Override
    public void handle(NetworkManager.PacketContext context) {
        PlayerEntity player = context.getPlayer();

        if (player.world.getBlockEntity(this.blockPos) instanceof BaseTardisConsoleUnitBlockEntity tardisConsoleUnitBlockEntity) {
            ModSounds.playTardisConsoleCrackSound(player.world, blockPos);
        }
    }
}

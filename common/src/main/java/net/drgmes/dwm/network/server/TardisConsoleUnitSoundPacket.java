package net.drgmes.dwm.network.server;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.consoleunits.BaseTardisConsoleUnitBlockEntity;
import net.drgmes.dwm.network.IPacket;
import net.drgmes.dwm.setup.ModSounds;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;

public record TardisConsoleUnitSoundPacket(
    BlockPos blockPos
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("tardis_console_unit_sound");
    public static final CustomPayload.Id<TardisConsoleUnitSoundPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, TardisConsoleUnitSoundPacket> PACKET_CODEC = PacketCodec.tuple(
        BlockPos.PACKET_CODEC, TardisConsoleUnitSoundPacket::blockPos,
        TardisConsoleUnitSoundPacket::new
    );

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    public static void handle(TardisConsoleUnitSoundPacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            PlayerEntity player = context.getPlayer();

            if (player.getWorld().getBlockEntity(payload.blockPos) instanceof BaseTardisConsoleUnitBlockEntity) {
                ModSounds.playTardisConsoleCrackSound(player.getWorld(), payload.blockPos);
            }
        });
    }
}

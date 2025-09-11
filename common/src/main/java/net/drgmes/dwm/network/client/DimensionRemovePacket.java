package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.network.IPacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

import java.util.Set;

public record DimensionRemovePacket(
    RegistryKey<World> worldKey
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("dimension_remove");
    public static final CustomPayload.Id<DimensionRemovePacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, DimensionRemovePacket> PACKET_CODEC = PacketCodec.tuple(
        RegistryKey.createPacketCodec(RegistryKeys.WORLD), DimensionRemovePacket::worldKey,
        DimensionRemovePacket::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }

    @Environment(EnvType.CLIENT)
    public static void handle(DimensionRemovePacket payload, NetworkManager.PacketContext context) {
        context.queue(() -> {
            final MinecraftClient mc = MinecraftClient.getInstance();
            if (mc.player == null || mc.getNetworkHandler() == null) return;

            Set<RegistryKey<World>> worlds = mc.getNetworkHandler().getWorldKeys();
            if (worlds == null || !worlds.contains(payload.worldKey)) return;

            worlds.remove(payload.worldKey);
        });
    }
}

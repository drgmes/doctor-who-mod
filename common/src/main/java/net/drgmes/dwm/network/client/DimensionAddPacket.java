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

public record DimensionAddPacket(
    RegistryKey<World> worldKey
) implements IPacket {
    public static final Identifier ID = DWM.getIdentifier("dimension_add");
    public static final CustomPayload.Id<DimensionAddPacket> PACKET_ID = new CustomPayload.Id<>(ID);

    public static final PacketCodec<PacketByteBuf, DimensionAddPacket> PACKET_CODEC = PacketCodec.tuple(
        RegistryKey.createPacketCodec(RegistryKeys.WORLD), DimensionAddPacket::worldKey,
        DimensionAddPacket::new
    );

    @Environment(EnvType.CLIENT)
    public static void handle(DimensionAddPacket payload, NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        Set<RegistryKey<World>> worlds = mc.getNetworkHandler().getWorldKeys();
        if (worlds == null || worlds.contains(payload.worldKey)) return;

        worlds.add(payload.worldKey);
    }

    @Override
    public Id<? extends CustomPayload> getId() {
        return PACKET_ID;
    }
}

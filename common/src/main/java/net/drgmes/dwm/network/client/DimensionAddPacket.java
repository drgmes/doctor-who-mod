package net.drgmes.dwm.network.client;

import dev.architectury.networking.NetworkManager;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageType;
import net.drgmes.dwm.setup.ModNetwork;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.World;

import java.util.Set;

public class DimensionAddPacket extends BaseS2CMessage {
    private final RegistryKey<World> worldKey;

    public DimensionAddPacket(RegistryKey<World> worldKey) {
        this.worldKey = worldKey;
    }

    public static DimensionAddPacket create(PacketByteBuf buf) {
        return new DimensionAddPacket(buf.readRegistryKey(RegistryKeys.WORLD));
    }

    @Override
    public MessageType getType() {
        return ModNetwork.DIMENSION_ADD;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeRegistryKey(this.worldKey);
    }

    @Override
    @Environment(EnvType.CLIENT)
    public void handle(NetworkManager.PacketContext context) {
        final MinecraftClient mc = MinecraftClient.getInstance();
        if (mc.player == null || mc.getNetworkHandler() == null) return;

        Set<RegistryKey<World>> worlds = mc.getNetworkHandler().getWorldKeys();
        if (worlds == null) return;

        worlds.add(this.worldKey);
    }
}

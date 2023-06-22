package net.drgmes.dwm.utils.helpers;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.compat.ImmersivePortalsAPI;
import net.drgmes.dwm.setup.ModCompats;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

import java.util.function.Consumer;
import java.util.function.Function;

public class DimensionHelper {
    public static String getWorldId(World world) {
        return world.getRegistryKey().getValue().getPath();
    }

    public static RegistryKey<World> getWorldKey(Identifier id) {
        return RegistryKey.of(RegistryKeys.WORLD, id);
    }

    public static RegistryKey<World> getWorldKey(String id) {
        return getWorldKey(new Identifier(id));
    }

    public static ServerWorld getWorld(RegistryKey<World> worldKey, MinecraftServer server) {
        return server.getWorldRegistryKeys().contains(worldKey) ? server.getWorld(worldKey) : null;
    }

    public static ServerWorld getWorld(Identifier id, MinecraftServer server) {
        return getWorld(getWorldKey(id), server);
    }

    public static ServerWorld getWorld(String id, MinecraftServer server) {
        return getWorld(getWorldKey(id), server);
    }

    public static ServerWorld getModWorld(String id, MinecraftServer server) {
        return getWorld(DWM.getIdentifier(id), server);
    }

    public static ServerWorld getOrCreateWorld(String id, MinecraftServer server, Consumer<ServerWorld> initialConsumer, Function<MinecraftServer, DimensionOptions> dimensionFactory) {
        if (ModCompats.immersivePortalsAPI()) return ImmersivePortalsAPI.getOrCreateWorld(id, server, initialConsumer, dimensionFactory);
        else return customGetOrCreateWorld(id, server, initialConsumer, dimensionFactory);
    }

    public static void removeWorld(String id, MinecraftServer server) {
        if (ModCompats.immersivePortalsAPI()) ImmersivePortalsAPI.removeWorld(id, server);
        else customRemoveWorld(id, server);
    }

    @ExpectPlatform
    public static ServerWorld customGetOrCreateWorld(String id, MinecraftServer server, Consumer<ServerWorld> initialConsumer, Function<MinecraftServer, DimensionOptions> dimensionFactory) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void customRemoveWorld(String id, MinecraftServer server) {
        throw new AssertionError();
    }
}

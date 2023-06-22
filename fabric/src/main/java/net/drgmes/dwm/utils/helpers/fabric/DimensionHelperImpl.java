package net.drgmes.dwm.utils.helpers.fabric;

import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.dimension.DimensionOptions;

import java.util.function.Consumer;
import java.util.function.Function;

public class DimensionHelperImpl {
    public static ServerWorld customGetOrCreateWorld(String id, MinecraftServer server, Consumer<ServerWorld> initialConsumer, Function<MinecraftServer, DimensionOptions> dimensionFactory) {
        throw new AssertionError();
    }

    public static void customRemoveWorld(String id, MinecraftServer server) {
        throw new AssertionError();
    }
}

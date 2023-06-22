package net.drgmes.dwm.compat;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import qouteall.q_misc_util.api.DimensionAPI;

import java.util.function.Consumer;
import java.util.function.Function;

public class ImmersivePortalsAPI {
    public static ServerWorld getOrCreateWorld(String id, MinecraftServer server, Consumer<ServerWorld> initialConsumer, Function<MinecraftServer, DimensionOptions> dimensionFactory) {
        Identifier worldIdentifier = DWM.getIdentifier(id);
        RegistryKey<World> worldKey = DimensionHelper.getWorldKey(worldIdentifier);
        ServerWorld world = server.getWorldRegistryKeys().contains(worldKey) ? server.getWorld(worldKey) : null;
        if (world != null) return world;

        DimensionAPI.addDimensionDynamically(worldIdentifier, dimensionFactory.apply(server));
        DimensionAPI.saveDimensionConfiguration(worldKey);

        world = DimensionHelper.getWorld(worldKey, server);
        if (initialConsumer != null) initialConsumer.accept(world);
        return world;
    }

    public static void removeWorld(String id, MinecraftServer server) {
        Identifier worldIdentifier = DWM.getIdentifier(id);
        RegistryKey<World> worldKey = DimensionHelper.getWorldKey(worldIdentifier);
        ServerWorld world = server.getWorldRegistryKeys().contains(worldKey) ? server.getWorld(worldKey) : null;
        if (world == null) return;

        DimensionAPI.removeDimensionDynamically(world);
        DimensionAPI.deleteDimensionConfiguration(worldKey);
    }
}

package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.api.DimensionAPI;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class DimensionHelper {
    public static String getWorldId(World world) {
        return world.getRegistryKey().getValue().getPath();
    }

    public static RegistryKey<World> getWorldKey(Identifier id) {
        return RegistryKey.of(Registry.WORLD_KEY, id);
    }

    public static RegistryKey<World> getWorldKey(String id) {
        return getWorldKey(new Identifier(id));
    }

    public static ServerWorld getWorld(RegistryKey<World> worldKey) {
        MinecraftServer server = MiscHelper.getServer();
        return server != null && server.isOnThread() && server.getWorldRegistryKeys().contains(worldKey) ? server.getWorld(worldKey) : null;
    }

    public static ServerWorld getWorld(Identifier id) {
        return getWorld(getWorldKey(id));
    }

    public static ServerWorld getWorld(String id) {
        return getWorld(getWorldKey(id));
    }

    public static ServerWorld getModWorld(String id) {
        return getWorld(DWM.getIdentifier(id));
    }

    public static ServerWorld getOrCreateWorld(String id, Consumer<ServerWorld> initialConsumer, Supplier<DimensionOptions> dimensionFactory) {
        MinecraftServer server = MiscHelper.getServer();
        if (server == null || !server.isOnThread()) return null;

        Identifier worldIdentifier = DWM.getIdentifier(id);
        RegistryKey<World> worldKey = getWorldKey(worldIdentifier);
        ServerWorld world = server.getWorldRegistryKeys().contains(worldKey) ? server.getWorld(worldKey) : null;
        if (world != null) return world;

        DimensionAPI.addDimensionDynamically(worldIdentifier, dimensionFactory.get());
        DimensionAPI.saveDimensionConfiguration(worldKey);

        world = getWorld(worldKey);
        initialConsumer.accept(world);
        return world;
    }
}

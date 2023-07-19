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

import java.util.function.Function;

public class ImmersivePortalsAPI {
    public static ServerWorld createWorld(String id, MinecraftServer server, Function<MinecraftServer, DimensionOptions> dimensionFactory) {
        Identifier worldIdentifier = DWM.getIdentifier(id);
        RegistryKey<World> worldKey = DimensionHelper.getWorldKey(worldIdentifier);

        DimensionAPI.addDimensionDynamically(worldIdentifier, dimensionFactory.apply(server));
        DimensionAPI.saveDimensionConfiguration(worldKey);

        return DimensionHelper.getWorld(worldKey, server);
    }

    public static void removeWorld(String id, MinecraftServer server) {
        ServerWorld world = DimensionHelper.getModWorld(id, server);
        if (world == null) return;

        DimensionAPI.removeDimensionDynamically(world);
        DimensionAPI.deleteDimensionConfiguration(DimensionHelper.getWorldKey(DWM.getIdentifier(id)));
    }
}

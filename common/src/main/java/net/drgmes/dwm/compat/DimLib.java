package net.drgmes.dwm.compat;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import qouteall.dimlib.api.DimensionAPI;

import java.util.function.Function;

public class DimLib {
    public static void suppressExperimentalWarning() {
        DimensionAPI.suppressExperimentalWarningForNamespace(DWM.MODID);
    }

    public static ServerWorld createWorld(String id, MinecraftServer server, Function<MinecraftServer, DimensionOptions> dimensionFactory) {
        Identifier worldIdentifier = DWM.getIdentifier(id);
        RegistryKey<World> worldKey = DimensionHelper.getWorldKey(worldIdentifier);

        DimensionAPI.addDimension(server, worldIdentifier, dimensionFactory.apply(server));
        ModDimensions.addWorldToRegistry(server, worldKey);
        DWM.LOGGER.info("Dimension \"%s\" created by DimLib".formatted(id));

        return DimensionHelper.getWorld(worldKey, server);
    }

    public static void removeWorld(String id, MinecraftServer server) {
        Identifier worldIdentifier = DWM.getIdentifier(id);
        RegistryKey<World> worldKey = DimensionHelper.getWorldKey(worldIdentifier);

        ServerWorld world = DimensionHelper.getWorld(worldKey, server);
        if (world == null) return;

        DimensionAPI.removeDimensionDynamically(world);
        ModDimensions.removeWorldFromRegistry(server, worldKey);
        DWM.LOGGER.info("Dimension \"%s\" removed by DimLib".formatted(id));
    }
}

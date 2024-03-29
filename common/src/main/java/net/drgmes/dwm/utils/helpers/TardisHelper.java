package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;

public class TardisHelper {
    public static final BlockPos TARDIS_POS = new BlockPos(0, 128, 0).toImmutable();

    public static BlockPos getTardisFarPos(int index) {
        return TARDIS_POS.add(DWM.COMMON.TARDIS_ROOMS_OFFSET, 0, DWM.COMMON.TARDIS_ROOMS_OFFSET).multiply(index).withY(TARDIS_POS.getY()).toImmutable();
    }

    public static boolean isTardisDimension(World world) {
        return world != null && world.getDimensionKey().equals(ModDimensionTypes.TARDIS);
    }

    public static ServerWorld getOrCreateTardisWorld(String id, RegistryKey<World> dimension, BlockPos blockPos, Direction direction, MinecraftServer server) {
        ServerWorld tardisWorld = DimensionHelper.getOrCreateWorld(id, server, TardisHelper::tardisDimensionBuilder);

        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            tardis.setDimension(dimension, false);
            tardis.setFacing(direction, false);
            tardis.setPosition(blockPos, false);
        });

        return tardisWorld;
    }

    public static ServerWorld getOrCreateTardisWorld(BaseTardisExteriorBlockEntity tile) {
        if (tile.getWorld() == null || tile.getWorld().isClient) return null;

        return TardisHelper.getOrCreateTardisWorld(
            tile.getTardisId(),
            tile.getWorld().getRegistryKey(),
            tile.getPos(),
            tile.getCachedState().get(BaseTardisExteriorBlock.FACING),
            tile.getWorld().getServer()
        );
    }

    public static DimensionOptions tardisDimensionBuilder(MinecraftServer server) {
        return new DimensionOptions(
            server.getRegistryManager().get(RegistryKeys.DIMENSION_TYPE).getEntry(ModDimensionTypes.TARDIS).orElseThrow(),
            new TardisChunkGenerator(server)
        );
    }
}

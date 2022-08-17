package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import qouteall.q_misc_util.MiscHelper;

import java.util.function.Consumer;

public class TardisHelper {
    public static final BlockPos TARDIS_POS = new BlockPos(0, 128, 0).toImmutable();

    public static boolean isTardisDimension(World world) {
        return world != null && world.getDimensionKey().equals(ModDimensionTypes.TARDIS);
    }

    public static ServerWorld getOrCreateTardisWorld(String id, RegistryKey<World> dimension, BlockPos blockPos, Direction direction, String consoleRoom) {
        ServerWorld tardisWorld = DimensionHelper.getOrCreateWorld(id, TardisHelper.getTardisSetupConsumer(consoleRoom), TardisHelper::tardisDimensionBuilder);
        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            tardis.setDimension(dimension, false);
            tardis.setFacing(direction, false);
            tardis.setPosition(blockPos, false);
        });

        return tardisWorld;
    }

    public static ServerWorld getOrCreateTardisWorld(BaseTardisExteriorBlockEntity tile) {
        return tile.getWorld() == null || tile.getWorld().isClient ? null : TardisHelper.getOrCreateTardisWorld(
            tile.getTardisId(),
            tile.getWorld().getRegistryKey(),
            tile.getPos(),
            tile.getCachedState().get(BaseTardisExteriorBlock.FACING),
            "toyota_natured"
        );
    }

    private static DimensionOptions tardisDimensionBuilder() {
        MinecraftServer server = MiscHelper.getServer();
        if (server == null || !server.isOnThread()) return null;

        return new DimensionOptions(
            server.getRegistryManager().getManaged(Registry.DIMENSION_TYPE_KEY).getOrCreateEntry(ModDimensionTypes.TARDIS),
            new TardisChunkGenerator(server)
        );
    }

    private static Consumer<ServerWorld> getTardisSetupConsumer(String consoleRoom) {
        return (tardis) -> {
            if (consoleRoom != null) {
                StructureTemplate template = tardis.getStructureTemplateManager().getTemplateOrBlank(DWM.getIdentifier("console_rooms/" + consoleRoom));
                if (template != null) template.place(tardis, TardisHelper.TARDIS_POS, new BlockPos(BlockPos.ZERO), new StructurePlacementData().setIgnoreEntities(false), tardis.random, 3);
            }
        };
    }
}

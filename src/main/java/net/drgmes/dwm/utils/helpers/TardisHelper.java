package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.setup.ModEvents;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.util.ITeleporter;

import java.util.function.Consumer;
import java.util.function.Function;

public class TardisHelper {
    public static final BlockPos TARDIS_POS = new BlockPos(0, 128, 0).immutable();

    public static void teleportToTardis(Entity entity, ServerLevel destination) {
        if (destination == null || entity.level.dimension() == destination.dimension() || !DimensionHelper.isTardisDimension(destination))
            return;

        destination.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            entity.setYRot(tardis.getEntraceFacing().toYRot());
            entity.changeDimension(destination, new TardisTeleporter(tardis.getEntracePosition().relative(tardis.getEntraceFacing())));
        });
    }

    public static void teleportFromTardis(Entity entity, MinecraftServer server) {
        if (server == null || !DimensionHelper.isTardisDimension(entity.level)) return;

        entity.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            if (!tardis.isValid()) return;

            ServerLevel level = server.getLevel(tardis.getCurrentExteriorDimension());
            if (level != null) {
                entity.setYRot(tardis.getCurrentExteriorFacing().toYRot());
                entity.changeDimension(level, new TardisTeleporter(tardis.getCurrentExteriorRelativePosition()));
            }
        });
    }

    public static ServerLevel getOrCreateTardisLevel(Level level, String id, ResourceKey<Level> dimension, BlockPos blockPos, Direction direction, String room) {
        MinecraftServer server = level.getServer();
        if (server == null) return null;

        ResourceKey<Level> levelKey = DimensionHelper.getModLevelKey(id);
        ServerLevel tardisLevel = DimensionHelper.getOrCreateLevel(server, id, TardisHelper.getTardisConsoleRoomBuilder(room), TardisHelper::tardisDimensionBuilder);

        tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((tardis) -> {
            tardis.setDimension(dimension, false);
            tardis.setFacing(direction, false);
            tardis.setPosition(blockPos, false);
        });

        if (server.getLevel(levelKey) != null) return tardisLevel;

        ModDimensions.TARDISES.add(id);
        if (ModEvents.DATA != null) ModEvents.DATA.setDirty();

        return tardisLevel;
    }

    public static ServerLevel getOrCreateTardisLevel(Level level, BaseTardisExteriorBlockEntity tile) {
        return TardisHelper.getOrCreateTardisLevel(
            level,
            tile.getTardisLevelUUID(),
            level.dimension(),
            tile.getBlockPos(),
            tile.getBlockState().getValue(BaseTardisExteriorBlock.FACING),
            tile.tardisConsoleRoom
        );
    }

    public static void registerOldTardises(MinecraftServer server) {
        for (String id : ModDimensions.TARDISES) {
            if (server.getLevel(DimensionHelper.getModLevelKey(id)) != null) continue;
            DimensionHelper.getOrCreateLevelStatic(server, id, TardisHelper::tardisDimensionBuilder);
        }
    }

    private static LevelStem tardisDimensionBuilder(MinecraftServer server, ResourceKey<LevelStem> dimensionKey) {
        return new LevelStem(
            server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getHolderOrThrow(ModDimensionTypes.TARDIS),
            new TardisChunkGenerator(server)
        );
    }

    private static Consumer<ServerLevel> getTardisConsoleRoomBuilder(String room) {
        return (level) -> {
            if (room == null) return;

            StructureTemplate template = level.getStructureManager().getOrCreate(new ResourceLocation(DWM.MODID, "rooms/consoles/" + room));
            if (template != null)
                template.placeInWorld(level, TardisHelper.TARDIS_POS, BlockPos.ZERO, new StructurePlaceSettings().setIgnoreEntities(false), level.random, 3);
        };
    }

    public static class TardisTeleporter implements ITeleporter {
        private BlockPos pos = BlockPos.ZERO;

        public TardisTeleporter(BlockPos pos) {
            this.pos = pos;
        }

        @Override
        public Entity placeEntity(Entity entity, ServerLevel level, ServerLevel destination, float yaw, Function<Boolean, Entity> repositionEntity) {
            entity = repositionEntity.apply(false);
            entity.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            return entity;
        }

        @Override
        public boolean playTeleportSound(ServerPlayer player, ServerLevel level, ServerLevel destination) {
            return false;
        }
    }
}

package net.drgmes.dwm.utils.helpers;

import java.util.function.Consumer;
import java.util.function.Function;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.tardisexteriorpolicebox.TardisExteriorPoliceBoxBlockEntity;
import net.drgmes.dwm.common.boti.BotiBlocksStorage;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.setup.ModEvents;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.util.ITeleporter;

public class TardisHelper {
    public static final BlockPos TARDIS_POS = new BlockPos(0, 128, 0).immutable();

    public static boolean isTardisDimension(Level level) {
        return level != null && level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS);
    }

    public static void teleportToTardis(Entity entity, ServerLevel destination) {
        if (destination == null || entity.level.dimension() == destination.dimension() || !TardisHelper.isTardisDimension(destination)) return;

        destination.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            entity.setYRot(provider.getEntraceFacing().toYRot());
            entity.changeDimension(destination, new TardisTeleporter(provider.getEntracePosition().relative(provider.getEntraceFacing())));
        });
    }

    public static void teleportFromTardis(Entity entity, MinecraftServer server) {
        if (server == null || !TardisHelper.isTardisDimension(entity.level)) return;

        entity.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            if (!provider.isValid()) return;

            ServerLevel level = server.getLevel(provider.getCurrentExteriorDimension());
            if (level != null) {
                entity.setYRot(provider.getCurrentExteriorFacing().toYRot());
                entity.changeDimension(level, new TardisTeleporter(provider.getCurrentExteriorRelativePosition()));
            }
        });
    }

    public static ServerLevel getOrCreateTardisLevel(TardisExteriorPoliceBoxBlockEntity tile, Level level) {
        MinecraftServer server = level.getServer();
        if (server == null) return null;

        String id = tile.getTardisLevelUUID();
        ResourceKey<Level> levelKey = DimensionHelper.getModLevelKey(id);
        ServerLevel tardisLevel = DimensionHelper.getOrCreateLevel(server, id, TardisHelper.getTardisConsoleRoomBuilder(tile), TardisHelper::tardisDimensionBuilder);

        tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            try {
                provider.setDimension(level.dimension(), false);
                provider.setFacing(tile.getBlockState().getValue(TardisExteriorPoliceBoxBlock.FACING), false);
                provider.setPosition(tile.getBlockPos(), false);
            } catch(Exception e) {
                System.out.println(e);
            }
        });

        if (server.getLevel(levelKey) != null) return tardisLevel;

        ModDimensions.TARDISES.add(id);
        if (ModEvents.DATA != null) ModEvents.DATA.setDirty();

        return tardisLevel;
    }

    public static void registerOldTardises(MinecraftServer server) {
        for (String id : ModDimensions.TARDISES) {
            if (server.getLevel(DimensionHelper.getModLevelKey(id)) != null) continue;
            DimensionHelper.getOrCreateLevelStatic(server, id, TardisHelper::tardisDimensionBuilder);
        }
    }

    public static void saveBlocksForBoti(Level level, BlockPos blockPos, String tardisLevelUUID) {
        if (!level.isClientSide) return;

        BotiBlocksStorage storage = BotiBlocksStorage.getStorage(tardisLevelUUID);
        storage.gatherBlocks(level, blockPos, level.getBlockState(blockPos).getValue(BlockStateProperties.HORIZONTAL_FACING));
    }

    public static void saveBlocksForBoti(Entity entity, String tardisLevelUUID) {
        TardisHelper.saveBlocksForBoti(entity.level, entity.blockPosition(), tardisLevelUUID);
    }

    private static LevelStem tardisDimensionBuilder(MinecraftServer server, ResourceKey<LevelStem> dimensionKey) {
        return new LevelStem(
            server.registryAccess().registryOrThrow(Registry.DIMENSION_TYPE_REGISTRY).getHolderOrThrow(ModDimensionTypes.TARDIS),
            new TardisChunkGenerator(server)
        );
    }

    private static Consumer<ServerLevel> getTardisConsoleRoomBuilder(TardisExteriorPoliceBoxBlockEntity tile) {
        final String room = tile.tardisConsoleRoom;

        return (level) -> {
            if (room == null) return;

            StructureTemplate template = level.getStructureManager().getOrCreate(new ResourceLocation(DWM.MODID, "rooms/consoles/" + room));
            if (template != null) template.placeInWorld(level, TardisHelper.TARDIS_POS, BlockPos.ZERO, new StructurePlaceSettings().setIgnoreEntities(false), level.random, 3);
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
    }
}

package net.drgmes.dwm.utils.helpers;

import java.util.function.Function;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.util.ITeleporter;

public class TardisHelper {
    public static final BlockPos TARDIS_POS = new BlockPos(0, 128, 0).immutable();

    public static void teleportToTardis(Entity entity, ServerLevel destination) {
        if (destination == null || entity.level.dimension() == destination.dimension() || !destination.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) return;

        destination.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            entity.setYRot(provider.getEntraceFacing().toYRot());
            entity.changeDimension(destination, new TardisTeleporter(provider.getEntracePosition().relative(provider.getEntraceFacing())));
        });
    }

    public static void teleportFromTardis(Entity entity, MinecraftServer server) {
        if (server == null || !entity.level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) return;

        entity.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            if (!provider.isValid()) return;

            ServerLevel level = server.getLevel(provider.getCurrentExteriorDimension());
            if (level != null) {
                entity.setYRot(provider.getCurrentExteriorFacing().toYRot());
                entity.changeDimension(level, new TardisTeleporter(provider.getCurrentExteriorRelativePosition()));
            }
        });
    }

    public static ServerLevel setupTardis(TardisExteriorBlockEntity tile, Level level) {
        MinecraftServer server = level.getServer();
        if (server == null) return null;

        ServerLevel tardisLevel = ModDimensions.getTardisDimension(tile.getTardisDimUUID(), server, (lvl) -> {
            if (tile.tardisConsoleRoom != null) {
                StructureTemplate template = lvl.getStructureManager().getOrCreate(new ResourceLocation(DWM.MODID, "rooms/consoles/" + tile.tardisConsoleRoom));
                if (template != null) template.placeInWorld(lvl, TardisHelper.TARDIS_POS, BlockPos.ZERO, new StructurePlaceSettings().setIgnoreEntities(false), lvl.random, 3);
            }
        });

        tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            try {
                provider.setDimension(level.dimension(), false);
                provider.setFacing(tile.getBlockState().getValue(TardisExteriorBlock.FACING), false);
                provider.setPosition(tile.getBlockPos(), false);
            } catch(Exception e) {
                System.out.println(e);
            }
        });

        return tardisLevel;
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

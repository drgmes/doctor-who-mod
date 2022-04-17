package net.drgmes.dwm.utils.helpers;

import java.util.function.Function;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlock;
import net.drgmes.dwm.blocks.tardisexterior.TardisExteriorBlockEntity;
import net.drgmes.dwm.setup.ModCapabilities;
import net.drgmes.dwm.setup.ModDimensions;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.util.ITeleporter;

public class TardisHelper {
    public static final BlockPos TARDIS_POS = new BlockPos(0, 128, 0).immutable();
    public static final BlockPos TARDIS_SPAWN_POS = TARDIS_POS.above(1).south(2).east(3).immutable();

    public static void teleportToTardis(Entity entity, ServerLevel destination) {
        if (destination == null || entity.level.dimension() == destination.dimension() || !destination.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) return;

        entity.setYRot(Direction.SOUTH.toYRot());
        entity.changeDimension(destination, new TardisTeleporter(TARDIS_SPAWN_POS));
    }

    public static void teleportFromTardis(Entity entity, MinecraftServer server) {
        if (server == null || !entity.level.dimensionTypeRegistration().is(ModDimensionTypes.TARDIS)) return;

        entity.level.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
            ServerLevel level = server.getLevel(provider.getExteriorDim());
            if (level != null) {
                entity.setYRot(provider.getExteriorFacing().toYRot());
                entity.changeDimension(level, new TardisTeleporter(provider.getExteriorRelativePosition()));
            }
        });
    }

    public static ServerLevel setupTardis(Level level, BlockPos blockPos, String consoleRoomName) {
        MinecraftServer server = level.getServer();
        if (server == null) return null;

        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof TardisExteriorBlockEntity tardisExteriorBlockEntity) {
            ServerLevel tardisLevel = ModDimensions.getTardisDimension(tardisExteriorBlockEntity.getTardisDimUUID(), server, (lvl) -> {
                if (consoleRoomName != null) {
                    StructureTemplate template = lvl.getStructureManager().getOrCreate(new ResourceLocation(DWM.MODID, "rooms/consoles/" + consoleRoomName));
                    if (template != null) template.placeInWorld(lvl, TardisHelper.TARDIS_POS, BlockPos.ZERO, new StructurePlaceSettings().setIgnoreEntities(false), lvl.random, 3);
                }
            });
    
            tardisLevel.getCapability(ModCapabilities.TARDIS_DATA).ifPresent((provider) -> {
                provider.setExteriorDim(level.dimension());
                provider.setExteriorPosition(blockPos);
                provider.setExteriorFacing(level.getBlockState(blockPos).getValue(TardisExteriorBlock.FACING));
            });
    
            return tardisLevel;
        }

        return null;
    }

    private static class TardisTeleporter implements ITeleporter {
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

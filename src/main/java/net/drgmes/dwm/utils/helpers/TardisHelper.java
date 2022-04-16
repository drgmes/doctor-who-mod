package net.drgmes.dwm.utils.helpers;

import java.util.function.Function;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModDimensions;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraftforge.common.util.ITeleporter;

public class TardisHelper {
    public static final BlockPos TARDIS_POS = new BlockPos(0, 128, 0).immutable();
    public static final BlockPos TARDIS_SPAWN_POS = TARDIS_POS.above(5).offset(8, 0, 8).immutable();

    public static void teleport(Entity entity, ServerLevel level) {
        if (level != null && entity.level.dimension() != level.dimension()) {
            entity.changeDimension(level, new TardisTeleporter());
        }
    }

    public static ServerLevel setupVoidTardis(MinecraftServer server, String uuid) {
        return ModDimensions.getTardisDimension(server, uuid);
    }

    public static ServerLevel setupTardis(MinecraftServer server, String uuid, String consoleRoomName) {
        return ModDimensions.getTardisDimension(server, uuid, (level) -> {
            StructureTemplate template = level.getStructureManager().getOrCreate(new ResourceLocation(DWM.MODID, "rooms/consoles/" + consoleRoomName));
            if (template != null) template.placeInWorld(level, TardisHelper.TARDIS_POS, BlockPos.ZERO, new StructurePlaceSettings().setIgnoreEntities(false), level.random, 3);
        });
    }

    private static class TardisTeleporter implements ITeleporter {
        private BlockPos pos = TARDIS_POS.above(1).south(1).east(3);

        @Override
        public Entity placeEntity(Entity entity, ServerLevel level, ServerLevel destination, float yaw, Function<Boolean, Entity> repositionEntity) {
            entity = repositionEntity.apply(false);
            entity.teleportTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5);
            return entity;
        }
    }
}

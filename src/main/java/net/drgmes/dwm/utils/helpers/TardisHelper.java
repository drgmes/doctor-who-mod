package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlock;
import net.drgmes.dwm.blocks.tardis.exteriors.BaseTardisExteriorBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModDimensions.ModDimensionTypes;
import net.drgmes.dwm.world.generator.TardisChunkGenerator;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionOptions;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.MiscHelper;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.util.Map;
import java.util.function.Consumer;

public class TardisHelper {
    public static final BlockPos TARDIS_POS = new BlockPos(0, 128, 0).toImmutable();

    public static boolean isTardisDimension(World world) {
        return world != null && world.getDimensionKey().equals(ModDimensionTypes.TARDIS);
    }

    public static Map.Entry<Portal, Portal> createTardisPortals(ServerWorld world, Direction originFacing, Direction destinationFacing, BlockPos originBlockPos, BlockPos destinationBlockPos, RegistryKey<World> destinationWorldKey, double originFacingOffset, double destinationFacingOffset, double blockOffset, int width, int height) {
        Vec3d originPos = Vec3d.ofCenter(originBlockPos, blockOffset).withBias(originFacing, originFacingOffset);
        Vec3d destinationPos = Vec3d.ofCenter(destinationBlockPos, blockOffset).withBias(destinationFacing, destinationFacingOffset);

        DQuaternion dQuaternion = DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), destinationFacing == Direction.NORTH || destinationFacing == Direction.SOUTH ? 180 : 0);
        dQuaternion = dQuaternion.combine(DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), originFacing.asRotation()));
        dQuaternion = dQuaternion.combine(DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), destinationFacing.asRotation()));

        Portal portal = Portal.entityType.create(world);
        portal.setOriginPos(originPos);
        portal.setDestination(destinationPos);
        portal.setDestinationDimension(destinationWorldKey);
        portal.setRotationTransformation(dQuaternion.toMcQuaternion());
        portal.setOrientationAndSize(new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), width, height);
        PortalManipulation.rotatePortalBody(portal, DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), originFacing.asRotation()).toMcQuaternion());

        Portal portalReversed = PortalAPI.createReversePortal(portal);
        return Map.entry(portal, portalReversed);
    }

    public static ServerWorld getOrCreateTardisWorld(String id, RegistryKey<World> dimension, BlockPos blockPos, Direction direction, Consumer<ServerWorld> tardisSetup) {
        ServerWorld tardisWorld = DimensionHelper.getOrCreateWorld(id, tardisSetup, TardisHelper::tardisDimensionBuilder);
        TardisStateManager.get(tardisWorld).ifPresent((tardis) -> {
            tardis.setDimension(dimension, false);
            tardis.setFacing(direction, false);
            tardis.setPosition(blockPos, false);
        });

        return tardisWorld;
    }

    public static ServerWorld getOrCreateTardisWorld(BaseTardisExteriorBlockEntity tile, boolean mustBeBroken) {
        return tile.getWorld() == null || tile.getWorld().isClient ? null : TardisHelper.getOrCreateTardisWorld(
            tile.getTardisId(),
            tile.getWorld().getRegistryKey(),
            tile.getPos(),
            tile.getCachedState().get(BaseTardisExteriorBlock.FACING),
            (tardisWorld) -> {
                TardisStateManager.get(tardisWorld, mustBeBroken).ifPresent((tardis) -> {
                    tardis.getConsoleRoom().place(tardis);
                    tardis.updateConsoleTiles();
                });
            }
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
}

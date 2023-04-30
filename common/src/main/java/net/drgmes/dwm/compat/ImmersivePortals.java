package net.drgmes.dwm.compat;

import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlock;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.types.IMixinPortal;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ImmersivePortals {
    private static final Map<String, TardisPortalsState> tardisPortalsStates = new HashMap<>();

    public static TardisPortalsState getOrCreateTardisPortalsState(TardisStateManager tardis) {
        String tardisId = tardis.getId();
        if (!tardisPortalsStates.containsKey(tardisId)) tardisPortalsStates.put(tardisId, new TardisPortalsState(tardis));
        return tardisPortalsStates.get(tardisId);
    }

    public static void removeTardisPortalsState(String tardisId) {
        if (!tardisPortalsStates.containsKey(tardisId)) return;
        tardisPortalsStates.remove(tardisId);
    }

    public static void clearTardisPortalsState() {
        tardisPortalsStates.clear();
    }

    public static Map.Entry<Portal, Portal> createPortals(ServerWorld world, Direction originFacing, Direction destinationFacing, BlockPos originBlockPos, BlockPos destinationBlockPos, RegistryKey<World> destinationWorldKey, double originFacingOffset, double destinationFacingOffset, double blockOffset, int width, int height) {
        Vec3d originPos = Vec3d.ofCenter(originBlockPos, blockOffset).offset(originFacing, originFacingOffset);
        Vec3d destinationPos = Vec3d.ofCenter(destinationBlockPos, blockOffset).offset(destinationFacing, destinationFacingOffset);

        DQuaternion dQuaternion = DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), destinationFacing == Direction.NORTH || destinationFacing == Direction.SOUTH ? 180 : 0);
        dQuaternion = dQuaternion.combine(DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), originFacing.asRotation()));
        dQuaternion = dQuaternion.combine(DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), destinationFacing.asRotation()));

        Portal portal = Portal.entityType.create(world);
        portal.setOriginPos(originPos);
        portal.setRotation(dQuaternion);
        portal.setDestination(destinationPos);
        portal.setDestinationDimension(destinationWorldKey);
        portal.setOrientationAndSize(new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), width, height);
        PortalManipulation.rotatePortalBody(portal, DQuaternion.fromMcQuaternion(DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), originFacing.asRotation()).toMcQuaternion()));

        Portal portalReversed = PortalAPI.createReversePortal(portal);
        return Map.entry(portal, portalReversed);
    }

    public static class TardisPortalsState {
        private final TardisStateManager tardis;
        private final List<Map.Entry<Portal, Portal>> portalsToRooms = new ArrayList<>();

        private Portal portalFromTardis;
        private Portal portalToTardis;

        public TardisPortalsState(TardisStateManager tardis) {
            this.tardis = tardis;
        }

        public void createEntrancePortals() {
            if (this.tardis.getWorld() == null) return;

            Map.Entry<Portal, Portal> portals = createPortals(
                this.tardis.getWorld(),
                this.tardis.getEntranceFacing(),
                this.tardis.getCurrentExteriorFacing(),
                this.tardis.getEntrancePosition().up(),
                this.tardis.getCurrentExteriorRelativePosition().up(),
                this.tardis.getCurrentExteriorDimension(),
                -0.5 + 0.0275, -0.5, 0,
                1, 2
            );

            this.portalFromTardis = portals.getKey();
            this.portalToTardis = portals.getValue();

            String worldId = this.tardis.getId();
            ((IMixinPortal) this.portalFromTardis).markAsTardisEntrance().setTardisId(worldId);
            ((IMixinPortal) this.portalToTardis).markAsTardisEntrance().setTardisId(worldId);

            if (this.portalFromTardis.getWorld() != null) this.portalFromTardis.getWorld().spawnEntity(this.portalFromTardis);
            if (this.portalToTardis.getWorld() != null) this.portalToTardis.getWorld().spawnEntity(this.portalToTardis);
        }

        public void createRoomsEntrancesPortals() {
            if (this.tardis.getWorld() == null) return;

            int index = 0;
            String worldId = this.tardis.getId();
            StructurePlacementData placeSettings = new StructurePlacementData();
            List<StructureTemplate.StructureBlockInfo> tacBlockInfos = this.tardis.getConsoleRoom().getTemplate(this.tardis.getWorld()).getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock());

            for (StructureTemplate.StructureBlockInfo tacBlockInfo : tacBlockInfos) {
                Direction direction = tacBlockInfo.state.get(TardisArsDestroyerBlock.FACING);
                BlockPos tacBlockPos = this.tardis.getConsoleRoom().getCenterPosition().add(tacBlockInfo.pos).offset(direction).toImmutable();
                BlockPos farTacBlockPos = TardisHelper.TARDIS_POS.add(1024, 0, 1024).multiply(++index).withY(TardisHelper.TARDIS_POS.getY()).toImmutable();

                Map.Entry<Portal, Portal> portals = createPortals(
                    this.tardis.getWorld(),
                    direction,
                    Direction.SOUTH,
                    tacBlockPos.up(),
                    farTacBlockPos.up(),
                    this.tardis.getWorld().getRegistryKey(),
                    -0.5, -0.5, -0.5,
                    3, 3
                );

                ((IMixinPortal) portals.getKey()).markAsTardisRoomsEntrance().setTardisId(worldId);
                ((IMixinPortal) portals.getValue()).markAsTardisRoomsEntrance().setTardisId(worldId);

                this.tardis.getWorld().spawnEntity(portals.getKey());
                this.tardis.getWorld().spawnEntity(portals.getValue());
                this.portalsToRooms.add(portals);
            }
        }

        public void clearEntrancePortals() {
            try {
                if (this.portalFromTardis != null) this.portalFromTardis.discard();
                if (this.portalToTardis != null) this.portalToTardis.discard();
            } catch (Exception ignored) {
            } finally {
                this.portalFromTardis = null;
                this.portalToTardis = null;
            }
        }

        public void clearRoomEntrancePortals() {
            for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
                try {
                    portalsToRoom.getKey().discard();
                    portalsToRoom.getValue().discard();
                } catch (Exception ignored) {
                }
            }

            this.portalsToRooms.clear();
        }

        public boolean isEntrancePortalFromTardisValid() {
            return this.portalFromTardis != null && !this.portalFromTardis.isRemoved() && this.portalFromTardis.isPortalValid();
        }

        public boolean isEntrancePortalToTardisValid() {
            return this.portalToTardis != null && !this.portalToTardis.isRemoved() && this.portalToTardis.isPortalValid();
        }

        public boolean isEntrancePortalsValid() {
            return this.isEntrancePortalFromTardisValid() && this.isEntrancePortalToTardisValid();
        }

        public boolean isRoomEntrancePortalsValid() {
            if (this.portalsToRooms.size() == 0) return false;

            for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
                if (portalsToRoom.getKey() == null || portalsToRoom.getValue() == null) return false;
                if (portalsToRoom.getKey().isRemoved() || portalsToRoom.getValue().isRemoved()) return false;
                if (!portalsToRoom.getKey().isPortalValid() || !portalsToRoom.getValue().isPortalValid()) return false;
            }

            return true;
        }

        public boolean checkIsEntrancePortalEquals(Portal portal) {
            return (this.portalFromTardis != null && this.portalFromTardis.equals(portal)) || (this.portalToTardis != null && this.portalToTardis.equals(portal));
        }

        public boolean checkIsRoomEntrancePortalEquals(Portal portal) {
            for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
                if ((portalsToRoom.getKey().equals(portal)) || (portalsToRoom.getValue().equals(portal))) return true;
            }

            return false;
        }
    }
}

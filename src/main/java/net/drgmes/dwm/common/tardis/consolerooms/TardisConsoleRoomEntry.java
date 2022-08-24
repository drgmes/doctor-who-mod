package net.drgmes.dwm.common.tardis.consolerooms;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlock;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.types.IMixinPortal;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.drgmes.dwm.utils.helpers.DimensionHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.Registry;
import qouteall.imm_ptl.core.api.PortalAPI;
import qouteall.imm_ptl.core.portal.Portal;
import qouteall.imm_ptl.core.portal.PortalManipulation;
import qouteall.q_misc_util.my_util.DQuaternion;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class TardisConsoleRoomEntry {
    public final Text title;
    public final String name;
    public final BlockPos center;
    public final BlockPos entrance;
    public final String decoratorBlock;
    public final String imageUrl;
    public final boolean isHidden;

    private List<Map.Entry<Portal, Portal>> portalsToRooms = new ArrayList<>();

    protected TardisConsoleRoomEntry(String name, String title, BlockPos center, BlockPos entrance, String decoratorBlock, String imageUrl, boolean isHidden) {
        this.title = Text.translatable(title);
        this.name = name;
        this.center = center.toImmutable();
        this.entrance = entrance.toImmutable();
        this.decoratorBlock = decoratorBlock;
        this.imageUrl = imageUrl;
        this.isHidden = isHidden;
    }

    public static TardisConsoleRoomEntry create(String name, String title, BlockPos center, BlockPos entrance, String decoratorBlock, String imageUrl, boolean isHidden) {
        TardisConsoleRoomEntry consoleRoom = new TardisConsoleRoomEntry(name, title, center, entrance, decoratorBlock, imageUrl, isHidden);
        TardisConsoleRooms.CONSOLE_ROOMS.put(name, consoleRoom);
        return consoleRoom;
    }

    public BlockPos getCenterPosition() {
        return TardisHelper.TARDIS_POS.subtract(this.center).toImmutable();
    }

    public BlockPos getEntrancePosition() {
        return this.getCenterPosition().add(this.entrance).toImmutable();
    }

    public Block getDecoratorBlock() {
        return Registry.BLOCK.get(new Identifier(this.decoratorBlock));
    }

    public StructureTemplate getTemplate(ServerWorld world) {
        return world.getStructureTemplateManager().getTemplateOrBlank(DWM.getIdentifier("console_rooms/" + this.name));
    }

    public boolean place(TardisStateManager tardis) {
        ServerWorld world = tardis.getWorld();
        StructurePlacementData placeSettings = new StructurePlacementData();

        TardisConsoleRoomEntry oldConsoleRoom = tardis.getConsoleRoom();
        StructureTemplate oldConsoleRoomTemplate = oldConsoleRoom.getTemplate(world);
        StructureTemplate newConsoleRoomTemplate = this.getTemplate(world);

        this.buildRoomsEntrances(world, oldConsoleRoomTemplate.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock()), true);
        this.clearArea(world, oldConsoleRoomTemplate.calculateBoundingBox(placeSettings, oldConsoleRoom.getCenterPosition()));
        this.clearArea(world, newConsoleRoomTemplate.calculateBoundingBox(placeSettings, this.getCenterPosition()));

        if (newConsoleRoomTemplate.place(world, this.getCenterPosition(), BlockPos.ORIGIN, placeSettings, world.random, 3)) {
            this.buildRoomsEntrances(world, newConsoleRoomTemplate.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock()), false);
            this.updateRoomsEntrancesPortals(tardis);
            return true;
        }

        return false;
    }

    public void updateRoomsEntrancesPortals(TardisStateManager tardis) {
        int index = 0;
        ServerWorld world = tardis.getWorld();
        String worldId = DimensionHelper.getWorldId(world);
        StructurePlacementData placeSettings = new StructurePlacementData();
        List<StructureTemplate.StructureBlockInfo> tacBlockInfos = this.getTemplate(world).getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock());

        this.clearRoomsEntrancesPortals();

        for (StructureTemplate.StructureBlockInfo tacBlockInfo : tacBlockInfos) {
            BlockPos farTacBlockPos = TardisHelper.TARDIS_POS.add(1024, 0, 1024).multiply(++index).withY(TardisHelper.TARDIS_POS.getY()).toImmutable();
            BlockPos tacBlockPos = this.getCenterPosition().add(tacBlockInfo.pos).toImmutable();
            Direction direction = tacBlockInfo.state.get(TardisArsDestroyerBlock.FACING);

            double offset = -0.5;
            Vec3d originPos = Vec3d.ofCenter(tacBlockPos.up().offset(direction), offset).withBias(direction, offset);
            Vec3d destinationPos = Vec3d.ofCenter(farTacBlockPos.up(), offset).withBias(Direction.SOUTH, offset);

            DQuaternion dQuaternion = DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), 180);
            dQuaternion = dQuaternion.combine(DQuaternion.rotationByDegrees(new Vec3d(0, 1, 0), direction.asRotation()));

            Portal portalToRoom = Portal.entityType.create(world);
            portalToRoom.setOriginPos(originPos);
            portalToRoom.setDestination(destinationPos);
            portalToRoom.setDestinationDimension(world.getRegistryKey());
            portalToRoom.setRotationTransformation(dQuaternion.toMcQuaternion());
            portalToRoom.setOrientationAndSize(new Vec3d(1, 0, 0), new Vec3d(0, 1, 0), 3, 3);
            PortalManipulation.rotatePortalBody(portalToRoom, DQuaternion.rotationByDegrees(new Vec3d(0, -1, 0), direction.asRotation()).toMcQuaternion());

            Portal portalFromRoom = PortalAPI.createReversePortal(portalToRoom);

            ((IMixinPortal) portalToRoom).markAsTardisRoomsEntrance().setTardisId(worldId);
            ((IMixinPortal) portalFromRoom).markAsTardisRoomsEntrance().setTardisId(worldId);
            world.spawnEntity(portalToRoom);
            world.spawnEntity(portalFromRoom);

            this.portalsToRooms.add(Map.entry(portalToRoom, portalFromRoom));
        }
    }

    public void validateRoomsEntrancesPortals(TardisStateManager tardis) {
        if (this.portalsToRooms.size() == 0) {
            this.updateRoomsEntrancesPortals(tardis);
            return;
        }

        for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
            if (portalsToRoom.getKey() == null || portalsToRoom.getValue() == null) {
                this.updateRoomsEntrancesPortals(tardis);
                return;
            }

            if (portalsToRoom.getKey().isRemoved() || portalsToRoom.getValue().isRemoved()) {
                this.updateRoomsEntrancesPortals(tardis);
                return;
            }
        }
    }

    public void clearRoomsEntrancesPortals() {
        for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
            try {
                portalsToRoom.getKey().discard();
                portalsToRoom.getValue().discard();
            } catch (Exception ignored) {
            }
        }

        this.portalsToRooms.clear();
    }

    public boolean checkIsPortalValid(Portal portal) {
        for (Map.Entry<Portal, Portal> portalsToRoom : this.portalsToRooms) {
            if ((portalsToRoom.getKey().equals(portal)) || (portalsToRoom.getValue().equals(portal))) return true;
        }

        return false;
    }

    private void buildRoomsEntrances(ServerWorld world, List<StructureTemplate.StructureBlockInfo> tacBlockInfos, boolean doClear) {
        int index = 0;

        for (StructureTemplate.StructureBlockInfo tacBlockInfo : tacBlockInfos) {
            BlockPos farTacBlockPos = TardisHelper.TARDIS_POS.add(1024, 0, 1024).multiply(++index).withY(TardisHelper.TARDIS_POS.getY()).toImmutable();
            BlockPos tacBlockPos = this.getCenterPosition().add(tacBlockInfo.pos).toImmutable();
            Direction direction = tacBlockInfo.state.get(TardisArsDestroyerBlock.FACING);
            BlockRotation rotation = CommonHelper.getBlockRotation(direction.getOpposite());

            this.fillWall(world, tacBlockPos, rotation, Blocks.AIR.getDefaultState());

            BlockPos upBlockPos = new BlockPos(BlockPos.ZERO.up(2));
            BlockPos downBlockPos = new BlockPos(BlockPos.ZERO.down(2));
            BlockPos upWestBlockPos = new BlockPos(BlockPos.ZERO.up(2).west());
            BlockPos upEastBlockPos = new BlockPos(BlockPos.ZERO.up(2).east());
            BlockPos downWestBlockPos = new BlockPos(BlockPos.ZERO.down(2).west());
            BlockPos downEastBlockPos = new BlockPos(BlockPos.ZERO.down(2).east());
            BlockPos westBlockPos = new BlockPos(BlockPos.ZERO.west(2));
            BlockPos eastBlockPos = new BlockPos(BlockPos.ZERO.east(2));
            BlockPos westUpBlockPos = new BlockPos(BlockPos.ZERO.west(2).up());
            BlockPos westDownBlockPos = new BlockPos(BlockPos.ZERO.west(2).down());
            BlockPos eastUpBlockPos = new BlockPos(BlockPos.ZERO.east(2).up());
            BlockPos eastDownBlockPos = new BlockPos(BlockPos.ZERO.east(2).down());

            if (doClear) {
                this.fillWall(world, farTacBlockPos, BlockRotation.NONE, Blocks.AIR.getDefaultState());
                world.setBlockState(farTacBlockPos, Blocks.AIR.getDefaultState(), 3);

                world.setBlockState(farTacBlockPos.add(upBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(downBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(upWestBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(upEastBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(downWestBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(downEastBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(westBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(eastBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(westUpBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(westDownBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(eastUpBlockPos), Blocks.AIR.getDefaultState(), 3);
                world.setBlockState(farTacBlockPos.add(eastDownBlockPos), Blocks.AIR.getDefaultState(), 3);
            }
            else {
                if (world.isAir(farTacBlockPos.down(2).south())) {
                    this.fillWall(world, farTacBlockPos, BlockRotation.NONE, this.getDecoratorBlock().getDefaultState());
                    world.setBlockState(farTacBlockPos, ModBlocks.TARDIS_ARS_CREATOR.getBlock().getDefaultState(), 3);
                }

                world.setBlockState(farTacBlockPos.add(upBlockPos), world.getBlockState(tacBlockPos.add(upBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(downBlockPos), world.getBlockState(tacBlockPos.add(downBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(upWestBlockPos), world.getBlockState(tacBlockPos.add(upWestBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(upEastBlockPos), world.getBlockState(tacBlockPos.add(upEastBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(downWestBlockPos), world.getBlockState(tacBlockPos.add(downWestBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(downEastBlockPos), world.getBlockState(tacBlockPos.add(downEastBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(westBlockPos), world.getBlockState(tacBlockPos.add(westBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(eastBlockPos), world.getBlockState(tacBlockPos.add(eastBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(westUpBlockPos), world.getBlockState(tacBlockPos.add(westUpBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(westDownBlockPos), world.getBlockState(tacBlockPos.add(westDownBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(eastUpBlockPos), world.getBlockState(tacBlockPos.add(eastUpBlockPos.rotate(rotation))), 3);
                world.setBlockState(farTacBlockPos.add(eastDownBlockPos), world.getBlockState(tacBlockPos.add(eastDownBlockPos.rotate(rotation))), 3);
            }
        }
    }

    private void clearArea(ServerWorld world, BlockBox aabb) {
        for (double x = aabb.getMinX(); x <= aabb.getMaxX(); x++) {
            for (double y = aabb.getMinY(); y <= aabb.getMaxY(); y++) {
                for (double z = aabb.getMinZ(); z <= aabb.getMaxZ(); z++) {
                    world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 3);
                }
            }
        }
    }

    private void fillWall(ServerWorld world, BlockPos blockPos, BlockRotation rotation, BlockState blockState) {
        world.setBlockState(blockPos, blockState, 3);
        world.setBlockState(blockPos.add(BlockPos.ZERO.up()), blockState, 3);
        world.setBlockState(blockPos.add(BlockPos.ZERO.down()), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.up().west()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.up().east()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.down().west()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.down().east()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.west()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.east()).rotate(rotation)), blockState, 3);
    }
}

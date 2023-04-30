package net.drgmes.dwm.common.tardis.consolerooms;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlock;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.compat.ModCompats;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModConfig;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

public class TardisConsoleRoomEntry {
    public final String name;
    public final String title;
    public final BlockPos center;
    public final BlockPos entrance;

    public String decoratorBlock;
    public String imageUrl = "";
    public String repairTo = "";
    public boolean isHidden = false;

    protected TardisConsoleRoomEntry(String name, String title, BlockPos center, BlockPos entrance) {
        this.name = name;
        this.title = title;
        this.center = center.toImmutable();
        this.entrance = entrance.toImmutable();
        this.decoratorBlock = Registries.BLOCK.getId(Blocks.CHISELED_QUARTZ_BLOCK).toString();
    }

    public static TardisConsoleRoomEntry create(String name, String title, BlockPos center, BlockPos entrance) {
        TardisConsoleRoomEntry consoleRoom = new TardisConsoleRoomEntry(name, title, center, entrance);
        TardisConsoleRooms.CONSOLE_ROOMS.put(name, consoleRoom);
        return consoleRoom;
    }

    public static TardisConsoleRoomEntry create(NbtCompound tag, boolean saveToRegistry) {
        String name = tag.getString("name");
        String title = tag.getString("title");
        BlockPos center = BlockPos.fromLong(tag.getLong("center"));
        BlockPos entrance = BlockPos.fromLong(tag.getLong("entrance"));

        TardisConsoleRoomEntry consoleRoom = new TardisConsoleRoomEntry(name, title, center, entrance);
        if (saveToRegistry) TardisConsoleRooms.CONSOLE_ROOMS.put(name, consoleRoom);
        consoleRoom.setDecoratorBlock(tag.getString("decoratorBlock"));
        consoleRoom.setImageUrl(tag.getString("imageUrl"));
        consoleRoom.setRepairTo(tag.getString("repairTo"));
        consoleRoom.setHidden(tag.getBoolean("isHidden"));
        return consoleRoom;
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putBoolean("isHidden", this.isHidden);

        tag.putString("name", this.name);
        tag.putString("title", this.title);
        tag.putString("decoratorBlock", this.decoratorBlock);
        tag.putString("imageUrl", this.imageUrl);
        tag.putString("repairTo", this.repairTo);

        tag.putLong("center", this.center.asLong());
        tag.putLong("entrance", this.entrance.asLong());

        return tag;
    }

    public StructureTemplate getTemplate(ServerWorld world) {
        return world.getStructureTemplateManager().getTemplateOrBlank(DWM.getIdentifier("console_rooms/" + this.name));
    }

    public Text getTitle() {
        return Text.translatable(this.title);
    }

    public BlockPos getCenterPosition() {
        return TardisHelper.TARDIS_POS.subtract(this.center).toImmutable();
    }

    public BlockPos getEntrancePosition() {
        return this.getCenterPosition().add(this.entrance).toImmutable();
    }

    public Block getDecoratorBlock() {
        return Registries.BLOCK.get(new Identifier(this.decoratorBlock));
    }

    public TardisConsoleRoomEntry setDecoratorBlock(String decoratorBlock) {
        this.decoratorBlock = decoratorBlock;
        return this;
    }

    public TardisConsoleRoomEntry setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
        return this;
    }

    public TardisConsoleRoomEntry setRepairTo(String repairTo) {
        this.repairTo = repairTo;
        return this;
    }

    public TardisConsoleRoomEntry setHidden(boolean isHidden) {
        this.isHidden = isHidden;
        return this;
    }

    public boolean place(TardisStateManager tardis) {
        ServerWorld world = tardis.getWorld();
        StructurePlacementData placeSettings = new StructurePlacementData();

        TardisConsoleRoomEntry oldConsoleRoom = tardis.getConsoleRoom();
        StructureTemplate oldConsoleRoomTemplate = oldConsoleRoom.getTemplate(world);
        StructureTemplate newConsoleRoomTemplate = this.getTemplate(world);

        this.buildRoomsEntrances(tardis, oldConsoleRoomTemplate.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock()), true);
        WorldHelper.clearArea(world, oldConsoleRoomTemplate.calculateBoundingBox(placeSettings, oldConsoleRoom.getCenterPosition()));
        WorldHelper.clearArea(world, newConsoleRoomTemplate.calculateBoundingBox(placeSettings, this.getCenterPosition()));

        if (newConsoleRoomTemplate.place(world, this.getCenterPosition(), BlockPos.ORIGIN, placeSettings, world.random, 3)) {
            return this.updateRoomsEntrances(tardis);
        }

        return false;
    }

    public boolean updateRoomsEntrances(TardisStateManager tardis) {
        StructurePlacementData placeSettings = new StructurePlacementData();
        this.buildRoomsEntrances(tardis, this.getTemplate(tardis.getWorld()).getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock()), false);
        return true;
    }

    private void buildRoomsEntrances(TardisStateManager tardis, List<StructureTemplate.StructureBlockInfo> tacBlockInfos, boolean doClear) {
        if (!ModConfig.COMMON.extendedRoomsGeneration.get() || !ModCompats.immersivePortals()) return;
        if (tardis.isBroken()) return;

        int index = 0;
        ServerWorld world = tardis.getWorld();

        for (StructureTemplate.StructureBlockInfo tacBlockInfo : tacBlockInfos) {
            BlockPos farTacBlockPos = TardisHelper.TARDIS_POS.add(1024, 0, 1024).multiply(++index).withY(TardisHelper.TARDIS_POS.getY()).toImmutable();
            BlockPos tacBlockPos = this.getCenterPosition().add(tacBlockInfo.pos).toImmutable();
            Direction direction = tacBlockInfo.state.get(TardisArsDestroyerBlock.FACING);
            BlockRotation rotation = WorldHelper.getBlockRotation(direction.getOpposite());

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
                world.removeBlock(farTacBlockPos, false);

                world.removeBlock(farTacBlockPos.add(upBlockPos), false);
                world.removeBlock(farTacBlockPos.add(downBlockPos), false);
                world.removeBlock(farTacBlockPos.add(upWestBlockPos), false);
                world.removeBlock(farTacBlockPos.add(upEastBlockPos), false);
                world.removeBlock(farTacBlockPos.add(downWestBlockPos), false);
                world.removeBlock(farTacBlockPos.add(downEastBlockPos), false);
                world.removeBlock(farTacBlockPos.add(westBlockPos), false);
                world.removeBlock(farTacBlockPos.add(eastBlockPos), false);
                world.removeBlock(farTacBlockPos.add(westUpBlockPos), false);
                world.removeBlock(farTacBlockPos.add(westDownBlockPos), false);
                world.removeBlock(farTacBlockPos.add(eastUpBlockPos), false);
                world.removeBlock(farTacBlockPos.add(eastDownBlockPos), false);
            }
            else {
                if (world.isAir(farTacBlockPos.down(2).south())) {
                    this.fillWall(world, farTacBlockPos, BlockRotation.NONE, this.getDecoratorBlock().getDefaultState());
                    world.setBlockState(farTacBlockPos, ModBlocks.TARDIS_ARS_CREATOR.getBlock().getDefaultState(), Block.NOTIFY_ALL);
                }

                world.setBlockState(farTacBlockPos.add(upBlockPos), world.getBlockState(tacBlockPos.add(upBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(downBlockPos), world.getBlockState(tacBlockPos.add(downBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(upWestBlockPos), world.getBlockState(tacBlockPos.add(upWestBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(upEastBlockPos), world.getBlockState(tacBlockPos.add(upEastBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(downWestBlockPos), world.getBlockState(tacBlockPos.add(downWestBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(downEastBlockPos), world.getBlockState(tacBlockPos.add(downEastBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(westBlockPos), world.getBlockState(tacBlockPos.add(westBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(eastBlockPos), world.getBlockState(tacBlockPos.add(eastBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(westUpBlockPos), world.getBlockState(tacBlockPos.add(westUpBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(westDownBlockPos), world.getBlockState(tacBlockPos.add(westDownBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(eastUpBlockPos), world.getBlockState(tacBlockPos.add(eastUpBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
                world.setBlockState(farTacBlockPos.add(eastDownBlockPos), world.getBlockState(tacBlockPos.add(eastDownBlockPos.rotate(rotation))), Block.NOTIFY_ALL);
            }
        }
    }

    private void fillWall(ServerWorld world, BlockPos blockPos, BlockRotation rotation, BlockState blockState) {
        world.setBlockState(blockPos, blockState, Block.NOTIFY_ALL);
        world.setBlockState(blockPos.add(BlockPos.ZERO.up()), blockState, Block.NOTIFY_ALL);
        world.setBlockState(blockPos.add(BlockPos.ZERO.down()), blockState, Block.NOTIFY_ALL);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.up().west()).rotate(rotation)), blockState, Block.NOTIFY_ALL);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.up().east()).rotate(rotation)), blockState, Block.NOTIFY_ALL);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.down().west()).rotate(rotation)), blockState, Block.NOTIFY_ALL);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.down().east()).rotate(rotation)), blockState, Block.NOTIFY_ALL);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.west()).rotate(rotation)), blockState, Block.NOTIFY_ALL);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.east()).rotate(rotation)), blockState, Block.NOTIFY_ALL);
    }
}

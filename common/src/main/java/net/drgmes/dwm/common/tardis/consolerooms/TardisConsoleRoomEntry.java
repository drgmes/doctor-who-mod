package net.drgmes.dwm.common.tardis.consolerooms;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.TardisTeleporterBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.items.screwdriver.ScrewdriverItem;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;

import java.util.List;
import java.util.function.BiConsumer;

public class TardisConsoleRoomEntry {
    public final String name;
    public final String title;
    public final BlockPos center;
    public final BlockPos entrance;
    public final int spawnChance;

    public String imageUrl = "";
    public String repairTo = "";
    public boolean isHidden = false;

    private String teleporterRoom;
    private String decoratorBlock;

    protected TardisConsoleRoomEntry(String name, String title, BlockPos center, BlockPos entrance, int spawnChance) {
        this.name = name;
        this.title = title;
        this.spawnChance = spawnChance;
        this.center = center.toImmutable();
        this.entrance = entrance.toImmutable();
    }

    public static TardisConsoleRoomEntry create(String name, String title, BlockPos center, BlockPos entrance, int spawnChance) {
        TardisConsoleRoomEntry consoleRoom = new TardisConsoleRoomEntry(name, title, center, entrance, spawnChance);
        TardisConsoleRooms.CONSOLE_ROOMS.put(name, consoleRoom);
        return consoleRoom;
    }

    public static TardisConsoleRoomEntry create(NbtCompound tag, boolean saveToRegistry) {
        String name = tag.getString("name");
        String title = tag.getString("title");
        BlockPos center = BlockPos.fromLong(tag.getLong("center"));
        BlockPos entrance = BlockPos.fromLong(tag.getLong("entrance"));
        int spawnChance = tag.getInt("spawnChance");

        TardisConsoleRoomEntry consoleRoom = new TardisConsoleRoomEntry(name, title, center, entrance, spawnChance);
        if (saveToRegistry) TardisConsoleRooms.CONSOLE_ROOMS.put(name, consoleRoom);
        consoleRoom.setTeleporterRoom(tag.getString("teleporterRoom"));
        consoleRoom.setDecoratorBlock(tag.getString("decoratorBlock"));
        consoleRoom.setImageUrl(tag.getString("imageUrl"));
        consoleRoom.setRepairTo(tag.getString("repairTo"));
        consoleRoom.setHidden(tag.getBoolean("isHidden"));
        return consoleRoom;
    }

    public NbtCompound writeNbt(NbtCompound tag) {
        tag.putBoolean("isHidden", this.isHidden);
        tag.putInt("spawnChance", this.spawnChance);

        tag.putString("name", this.name);
        tag.putString("title", this.title);
        tag.putString("teleporterRoom", this.teleporterRoom);
        tag.putString("decoratorBlock", this.decoratorBlock);
        tag.putString("imageUrl", this.imageUrl);
        tag.putString("repairTo", this.repairTo);

        tag.putLong("center", this.center.asLong());
        tag.putLong("entrance", this.entrance.asLong());

        return tag;
    }

    public StructureTemplate getTemplate(ServerWorld world) {
        return world.getStructureTemplateManager().getTemplateOrBlank(DWM.getIdentifier("tardis/console_rooms/" + this.name));
    }

    public StructureTemplate getTeleporterRoomTemplate(ServerWorld world) {
        return world.getStructureTemplateManager().getTemplateOrBlank(DWM.getIdentifier("tardis/teleporter_rooms/" + this.teleporterRoom));
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

    public String getTeleporterRoom() {
        return this.teleporterRoom;
    }

    public TardisConsoleRoomEntry setTeleporterRoom(String teleporterRoom) {
        this.teleporterRoom = teleporterRoom;
        return this;
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
        ServerWorld tardisWorld = tardis.getWorld();
        TardisConsoleRoomEntry oldConsoleRoom = tardis.getConsoleRoom();
        StructurePlacementData placeSettings = new StructurePlacementData();

        StructureTemplate newConsoleRoomTemplate = this.getTemplate(tardisWorld);
        StructureTemplate oldConsoleRoomTemplate = oldConsoleRoom.getTemplate(tardisWorld);
        StructureTemplate newTeleporterRoomTemplate = this.getTeleporterRoomTemplate(tardisWorld);
        StructureTemplate oldTeleporterRoomTemplate = oldConsoleRoom.getTeleporterRoomTemplate(tardisWorld);
        List<StructureTemplate.StructureBlockInfo> newConsoleRoomTacBlockInfos = newConsoleRoomTemplate.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock());
        List<StructureTemplate.StructureBlockInfo> oldConsoleRoomTacBlockInfos = oldConsoleRoomTemplate.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock());

        WorldHelper.clearArea(tardisWorld, oldConsoleRoomTemplate.calculateBoundingBox(placeSettings, oldConsoleRoom.getCenterPosition()));
        this.clearTeleporterRooms(tardisWorld, oldConsoleRoom.getCenterPosition(), oldTeleporterRoomTemplate, oldConsoleRoomTacBlockInfos);
        this.clearGroundItems(tardisWorld, oldConsoleRoomTemplate, oldConsoleRoom.getCenterPosition(), placeSettings);

        if (newConsoleRoomTemplate.place(tardisWorld, this.getCenterPosition(), BlockPos.ORIGIN, placeSettings, tardisWorld.random, Block.NOTIFY_ALL)) {
            this.clearGroundItems(tardisWorld, newTeleporterRoomTemplate, this.getCenterPosition(), placeSettings);
            this.placeTeleporterRooms(tardisWorld, newTeleporterRoomTemplate, newConsoleRoomTacBlockInfos);
            this.updateRoomsEntrances(tardisWorld, newTeleporterRoomTemplate, newConsoleRoomTacBlockInfos);
            return true;
        }

        return false;
    }

    private void placeTeleporterRooms(ServerWorld world, StructureTemplate teleporterRoomTemplate, List<StructureTemplate.StructureBlockInfo> tacBlockInfos) {
        this.processTeleporterRooms(this.getCenterPosition(), teleporterRoomTemplate, tacBlockInfos, (placeSettings, blockPos) -> {
            teleporterRoomTemplate.place(world, blockPos, BlockPos.ORIGIN, placeSettings, world.random, Block.NOTIFY_ALL);
        });
    }

    private void clearTeleporterRooms(ServerWorld world, BlockPos centerPosition, StructureTemplate teleporterRoomTemplate, List<StructureTemplate.StructureBlockInfo> tacBlockInfos) {
        this.processTeleporterRooms(centerPosition, teleporterRoomTemplate, tacBlockInfos, (placeSettings, blockPos) -> {
            WorldHelper.clearArea(world, teleporterRoomTemplate.calculateBoundingBox(placeSettings, blockPos));
        });
    }

    private void processTeleporterRooms(BlockPos centerPosition, StructureTemplate teleporterRoomTemplate, List<StructureTemplate.StructureBlockInfo> tacBlockInfos, BiConsumer<StructurePlacementData, BlockPos> executor) {
        int index = -1;
        StructurePlacementData placeSettings = new StructurePlacementData();
        BlockPos templateOffset = new BlockPos((int) Math.floor((double) teleporterRoomTemplate.getSize().getX() / 2), 2, 0).toImmutable();

        for (StructureTemplate.StructureBlockInfo tacBlockInfo : tacBlockInfos) {
            index++;

            Direction direction = tacBlockInfo.state().get(TardisArsCreatorBlock.FACING).getOpposite();
            BlockRotation rotation = WorldHelper.getBlockRotation(direction);

            BlockPos farTacBlockPos = TardisHelper.getTardisFarPos(index + 1).north().subtract(templateOffset.rotate(BlockRotation.CLOCKWISE_180).withY(0));
            BlockPos blockPos = centerPosition.add(tacBlockInfo.pos()).offset(direction).subtract(templateOffset.rotate(rotation)).toImmutable();

            placeSettings.setRotation(rotation);
            executor.accept(placeSettings, blockPos);

            placeSettings.setRotation(BlockRotation.CLOCKWISE_180);
            executor.accept(placeSettings, farTacBlockPos);
        }
    }

    private void updateRoomsEntrances(ServerWorld world, StructureTemplate teleporterRoomTemplate, List<StructureTemplate.StructureBlockInfo> tacBlockInfos) {
        int index = -1;

        StructurePlacementData placeSettings = new StructurePlacementData();
        BlockPos templateOffset = new BlockPos((int) Math.floor((double) teleporterRoomTemplate.getSize().getX() / 2), 2, 0).toImmutable();
        List<StructureTemplate.StructureBlockInfo> tpBlockInfos = teleporterRoomTemplate.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_TELEPORTER.getBlock());

        for (StructureTemplate.StructureBlockInfo tacBlockInfo : tacBlockInfos) {
            index++;

            BlockPos farTadBlockPos = TardisHelper.getTardisFarPos(index + 1);
            BlockPos tacBlockPos = this.getCenterPosition().add(tacBlockInfo.pos()).toImmutable();

            // Update info for ARS Builder block
            if (world.getBlockEntity(tacBlockPos) instanceof TardisArsCreatorBlockEntity tardisArsCreatorBlockEntity) {
                Direction direction = world.getBlockState(tacBlockPos).get(TardisArsCreatorBlock.FACING).getOpposite();
                BlockRotation wallRotation = WorldHelper.getBlockRotation(direction);

                // Update coords for Teleporter block
                if (tpBlockInfos.size() > 0) {
                    BlockPos templateBlockPos = tacBlockPos.offset(direction).subtract(templateOffset.rotate(wallRotation)).toImmutable();
                    BlockPos farTemplateBlockPos = TardisHelper.getTardisFarPos(index + 1).north().subtract(templateOffset.rotate(BlockRotation.CLOCKWISE_180).withY(0)).toImmutable();
                    BlockPos tpBlockPos = templateBlockPos.add(tpBlockInfos.get(0).pos().rotate(wallRotation)).toImmutable();
                    BlockPos farTpBlockPos = farTemplateBlockPos.add(tpBlockInfos.get(0).pos().rotate(BlockRotation.CLOCKWISE_180)).toImmutable();

                    this.updateRoomTeleporter(world, tpBlockPos, farTpBlockPos.up().south(), Direction.SOUTH);
                    this.updateRoomTeleporter(world, farTpBlockPos, tacBlockPos.down().offset(direction.getOpposite()), direction.getOpposite());
                }

                // If room was built then destroy the builder wall
                if (world.getBlockEntity(farTadBlockPos) instanceof TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
                    WorldHelper.clearArea(world, BlockBox.create(
                        tacBlockPos.add(new BlockPos(BlockPos.ZERO.up().west()).rotate(wallRotation)),
                        tacBlockPos.add(new BlockPos(BlockPos.ZERO.down().east()).rotate(wallRotation))
                    ));

                    tardisArsDestroyerBlockEntity.tacBlockPos = tacBlockPos;
                    tardisArsDestroyerBlockEntity.tacFacing = direction;
                    tardisArsDestroyerBlockEntity.markDirty();
                    continue;
                }

                tardisArsCreatorBlockEntity.index = index;
                tardisArsCreatorBlockEntity.isInitial = true;
                tardisArsCreatorBlockEntity.markDirty();
            }
        }
    }

    private void updateRoomTeleporter(ServerWorld world, BlockPos blockPos, BlockPos destinationBlockPos, Direction destinationFacing) {
        if (world.getBlockEntity(blockPos) instanceof TardisTeleporterBlockEntity tardisTeleporterBlockEntity) {
            tardisTeleporterBlockEntity.destinationBlockPos = destinationBlockPos;
            tardisTeleporterBlockEntity.destinationFacing = destinationFacing;
            tardisTeleporterBlockEntity.isLocked = true;
            tardisTeleporterBlockEntity.markDirty();
        }
    }

    private void clearGroundItems(ServerWorld world, StructureTemplate template, BlockPos blockPos, StructurePlacementData placeSettings) {
        BlockBox aabb = template.calculateBoundingBox(placeSettings, blockPos);
        List<ItemEntity> entities = world.getEntitiesByClass(ItemEntity.class, Box.from(aabb), EntityPredicates.EXCEPT_SPECTATOR);

        for (ItemEntity entity : entities) {
            if (entity.getStack().getItem() instanceof TardisKeyItem) continue;
            if (entity.getStack().getItem() instanceof ScrewdriverItem) continue;
            if (entity.getStack().getItem().getRarity(entity.getStack()) != Rarity.COMMON) continue;
            entity.kill();
        }
    }
}

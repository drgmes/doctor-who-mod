package net.drgmes.dwm.common.tardis.consolerooms;

import net.drgmes.dwm.blocks.tardis.doors.BaseTardisDoorsBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisteleporter.TardisTeleporterBlockEntity;
import net.drgmes.dwm.common.sonicdevice.SonicDevice;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.common.tardis.exteriors.TardisExteriorEntry;
import net.drgmes.dwm.items.tardis.keys.TardisKeyItem;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
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
    public final String structure;
    public final BlockPos entrance;
    public final BlockPos center;
    public final int spawnChance;

    public String imageUrl = "";
    public String repairTo = "";
    public boolean isHidden = false;

    private String teleporterRoom;
    private String decoratorBlock;
    private String doorsBlock;

    public TardisConsoleRoomEntry(String name, String title, String structure, int spawnChance, BlockPos center, BlockPos entrance) {
        this.name = name;
        this.title = title;
        this.structure = structure;
        this.spawnChance = spawnChance;
        this.center = center.toImmutable();
        this.entrance = entrance.toImmutable();
    }

    public static TardisConsoleRoomEntry fromNbt(NbtCompound tag) {
        TardisConsoleRoomEntry consoleRoom = new TardisConsoleRoomEntry(
            tag.getString("name"),
            tag.getString("title"),
            tag.getString("structure"),
            tag.getInt("spawnChance"),
            BlockPos.fromLong(tag.getLong("center")),
            BlockPos.fromLong(tag.getLong("entrance"))
        );

        if (tag.contains("teleporterRoom")) consoleRoom.setTeleporterRoom(tag.getString("teleporterRoom"));
        if (tag.contains("decoratorBlock")) consoleRoom.setDecoratorBlock(tag.getString("decoratorBlock"));
        if (tag.contains("doorsBlock")) consoleRoom.setDoorsBlock(tag.getString("doorsBlock"));
        if (tag.contains("imageUrl")) consoleRoom.setImageUrl(tag.getString("imageUrl"));
        if (tag.contains("repairTo")) consoleRoom.setRepairTo(tag.getString("repairTo"));
        if (tag.contains("isHidden")) consoleRoom.setHidden(tag.getBoolean("isHidden"));

        return consoleRoom;
    }

    public NbtCompound toNbt() {
        NbtCompound tag = new NbtCompound();

        tag.putString("name", this.name);
        tag.putString("title", this.title);
        tag.putString("structure", this.structure);

        tag.putLong("center", this.center.asLong());
        tag.putLong("entrance", this.entrance.asLong());

        tag.putBoolean("isHidden", this.isHidden);
        tag.putInt("spawnChance", this.spawnChance);

        if (this.teleporterRoom != null) tag.putString("teleporterRoom", this.teleporterRoom);
        if (this.decoratorBlock != null) tag.putString("decoratorBlock", this.decoratorBlock);
        if (this.doorsBlock != null) tag.putString("doorsBlock", this.doorsBlock);
        if (this.imageUrl != null) tag.putString("imageUrl", this.imageUrl);
        if (this.repairTo != null) tag.putString("repairTo", this.repairTo);

        return tag;
    }

    public StructureTemplate getTemplate(ServerWorld world) {
        return world.getStructureTemplateManager().getTemplateOrBlank(Identifier.of(this.structure));
    }

    public StructureTemplate getTeleporterRoomTemplate(ServerWorld world) {
        return world.getStructureTemplateManager().getTemplateOrBlank(Identifier.of(this.teleporterRoom));
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
        if (this.decoratorBlock == null) return null;
        return Registries.BLOCK.get(Identifier.of(this.decoratorBlock));
    }

    public TardisConsoleRoomEntry setDecoratorBlock(String decoratorBlock) {
        this.decoratorBlock = decoratorBlock;
        return this;
    }

    public Block getDoorsBlock() {
        if (this.doorsBlock == null) return null;
        return Registries.BLOCK.get(Identifier.of(this.doorsBlock));
    }

    public TardisConsoleRoomEntry setDoorsBlock(String doorsBlock) {
        this.doorsBlock = doorsBlock;
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
        TardisExteriorEntry exteriorType = tardis.getExteriorType();
        StructurePlacementData placeSettings = new StructurePlacementData();

        StructureTemplate template = this.getTemplate(tardisWorld);
        StructureTemplate teleporterRoomTemplate = this.getTeleporterRoomTemplate(tardisWorld);
        List<StructureTemplate.StructureBlockInfo> tacBlockInfos = template.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock());

        if (template.place(tardisWorld, this.getCenterPosition(), BlockPos.ORIGIN, placeSettings, tardisWorld.random, Block.NOTIFY_ALL)) {
            this.updateDoors(tardisWorld, template, exteriorType, placeSettings);
            this.placeTeleporterRooms(tardisWorld, teleporterRoomTemplate, tacBlockInfos);
            this.updateRoomsEntrances(tardisWorld, teleporterRoomTemplate, tacBlockInfos);
            this.clearGroundItems(tardisWorld, template, this.getCenterPosition(), placeSettings);
            return true;
        }

        return false;
    }

    public void remove(TardisStateManager tardis) {
        ServerWorld tardisWorld = tardis.getWorld();
        StructurePlacementData placeSettings = new StructurePlacementData();

        StructureTemplate template = this.getTemplate(tardisWorld);
        StructureTemplate teleporterTemplate = this.getTeleporterRoomTemplate(tardisWorld);
        List<StructureTemplate.StructureBlockInfo> tacBlockInfos = template.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_CREATOR.getBlock());

        WorldHelper.clearArea(tardisWorld, template.calculateBoundingBox(placeSettings, this.getCenterPosition()));
        this.removeTeleporterRooms(tardisWorld, this.getCenterPosition(), teleporterTemplate, tacBlockInfos);
        this.clearGroundItems(tardisWorld, template, this.getCenterPosition(), placeSettings);
    }

    private void placeTeleporterRooms(ServerWorld world, StructureTemplate teleporterRoomTemplate, List<StructureTemplate.StructureBlockInfo> tacBlockInfos) {
        this.processTeleporterRooms(this.getCenterPosition(), teleporterRoomTemplate, tacBlockInfos, (placeSettings, blockPos) -> {
            teleporterRoomTemplate.place(world, blockPos, BlockPos.ORIGIN, placeSettings, world.random, Block.NOTIFY_ALL);
        });
    }

    private void removeTeleporterRooms(ServerWorld world, BlockPos centerPosition, StructureTemplate teleporterRoomTemplate, List<StructureTemplate.StructureBlockInfo> tacBlockInfos) {
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

    private void updateDoors(ServerWorld tardisWorld, StructureTemplate template, TardisExteriorEntry exteriorType, StructurePlacementData placeSettings) {
        if (exteriorType == null) return;

        Block doorsBlock = this.getDoorsBlock();
        Block newDoorsBlock = exteriorType.getDoorsBlock();
        if (doorsBlock == null || newDoorsBlock == null) return;

        for (StructureTemplate.StructureBlockInfo doorsBlockInfo : template.getInfosForBlock(BlockPos.ORIGIN, placeSettings, doorsBlock)) {
            BlockState blockState = doorsBlockInfo.state();
            BlockPos blockPos = this.getCenterPosition().add(doorsBlockInfo.pos()).toImmutable();

            tardisWorld.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);

            BlockState newBlockState = newDoorsBlock.getDefaultState();
            newBlockState = newBlockState.with(BaseTardisDoorsBlock.WATERLOGGED, blockState.get(BaseTardisDoorsBlock.WATERLOGGED));
            newBlockState = newBlockState.with(BaseTardisDoorsBlock.FACING, blockState.get(BaseTardisDoorsBlock.FACING));
            newBlockState = newBlockState.with(BaseTardisDoorsBlock.HALF, blockState.get(BaseTardisDoorsBlock.HALF));
            newBlockState = newBlockState.with(BaseTardisDoorsBlock.OPEN, blockState.get(BaseTardisDoorsBlock.OPEN));

            tardisWorld.setBlockState(blockPos, newBlockState, Block.NOTIFY_ALL);
        }

        this.clearGroundItems(tardisWorld, template, this.getCenterPosition(), placeSettings);
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
                if (!tpBlockInfos.isEmpty()) {
                    BlockPos templateBlockPos = tacBlockPos.offset(direction).subtract(templateOffset.rotate(wallRotation)).toImmutable();
                    BlockPos farTemplateBlockPos = TardisHelper.getTardisFarPos(index + 1).north().subtract(templateOffset.rotate(BlockRotation.CLOCKWISE_180).withY(0)).toImmutable();
                    BlockPos tpBlockPos = templateBlockPos.add(tpBlockInfos.get(0).pos().rotate(wallRotation)).toImmutable();
                    BlockPos farTpBlockPos = farTemplateBlockPos.add(tpBlockInfos.get(0).pos().rotate(BlockRotation.CLOCKWISE_180)).toImmutable();

                    this.updateRoomTeleporter(world, tpBlockPos, farTpBlockPos.up().south(), Direction.SOUTH);
                    this.updateRoomTeleporter(world, farTpBlockPos, tacBlockPos.down().offset(direction.getOpposite()), direction.getOpposite());
                }

                // If room was built, then destroy the builder wall
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
            if (SonicDevice.checkItemStackIsSonicDevice(entity.getStack())) continue;
            if (entity.getStack().getItem() instanceof TardisKeyItem) continue;
            if (entity.getStack().getRarity() != Rarity.COMMON) continue;
            entity.kill();
        }
    }
}

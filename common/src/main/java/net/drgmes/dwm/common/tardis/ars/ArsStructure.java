package net.drgmes.dwm.common.tardis.ars;

import com.google.gson.JsonElement;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.EntityHelper;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.Registries;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.state.property.Property;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.*;
import org.apache.commons.lang3.function.TriFunction;

import java.util.List;
import java.util.Map;

public class ArsStructure {
    public static final PacketCodec<PacketByteBuf, ArsStructure> PACKET_CODEC = new PacketCodec<>() {
        @Override
        public void encode(PacketByteBuf buf, ArsStructure payload) {
            buf.writeString(payload.name);
            buf.writeString(payload.path);
            buf.writeString(payload.title);
            buf.writeString(payload.category);
            buf.writeInt(payload.order);
        }

        @Override
        public ArsStructure decode(PacketByteBuf buf) {
            return new ArsStructure(buf.readString(), buf.readString(), buf.readString(), buf.readString(), buf.readInt());
        }
    };

    public final String name;
    public final String path;
    public final String title;
    public final String category;
    public final int order;

    private Map<String, JsonElement> substitutes;

    public ArsStructure(String name, String path, String title, String category, int order) {
        this.name = name;
        this.path = path;
        this.title = title;
        this.category = category;
        this.order = order;
    }

    public Text getTitle() {
        return Text.translatable(this.title);
    }

    public StructureTemplate getTemplate(ServerWorld world) {
        return world.getStructureTemplateManager().getTemplateOrBlank(Identifier.of(this.path));
    }

    public ArsStructure setSubstitutes(Map<String, JsonElement> substitutes) {
        this.substitutes = substitutes;
        return this;
    }

    public boolean place(PlayerEntity player, TardisStateManager tardis, BlockPos tacBlockPos) {
        ServerWorld world = tardis.getWorld();
        BlockState tacBlockState = world.getBlockState(tacBlockPos);
        if (!(tacBlockState.getBlock() instanceof TardisArsCreatorBlock)) return false;
        if (!(world.getBlockEntity(tacBlockPos) instanceof TardisArsCreatorBlockEntity tardisArsCreatorBlockEntity)) return false;

        Direction direction = tacBlockState.get(TardisArsCreatorBlock.FACING);
        BlockRotation wallRotation = WorldHelper.getBlockRotation(direction);

        StructureTemplate template = this.getTemplate(world);
        if (template == null) return false;

        return this.process(
            template,
            tacBlockPos,
            direction,
            tardisArsCreatorBlockEntity.isInitial,
            tardisArsCreatorBlockEntity.index,
            (placeSettings, blockPos, tadOffset) -> {
                BlockBox aabb = template.calculateBoundingBox(placeSettings, blockPos);

                boolean isAreaEmpty = WorldHelper.foreachArea(aabb, (bp) -> {
                    if (!world.getBlockState(bp).isAir()) {
                        String posText = "[" + bp.getX() + " " + bp.getY() + " " + bp.getZ() + "]";
                        player.sendMessage(DWM.TEXTS.ARS_SECONDARY_ROOM_BUILD_FAILED_DETAILS.apply(posText), false);
                        return false;
                    }

                    return true;
                });

                if (isAreaEmpty && template.place(world, blockPos, BlockPos.ORIGIN, placeSettings, world.random, Block.NOTIFY_ALL)) {
                    // Replace blocks in a newly generated structure
                    if (this.substitutes != null) {
                        WorldHelper.foreachArea(aabb, (bp) -> {
                            try {
                                BlockState bs = world.getBlockState(bp);
                                String blockId = Registries.BLOCK.getId(bs.getBlock()).toString();

                                if (this.substitutes.containsKey(blockId)) {
                                    Block replacingBlock = Registries.BLOCK.get(Identifier.of(this.substitutes.get(blockId).getAsString()));
                                    BlockState replacingBlockState = replacingBlock.getDefaultState();

                                    List<Property<?>> propertiesToCopy = List.of(
                                        Properties.AXIS, Properties.HORIZONTAL_AXIS, Properties.FACING, Properties.HORIZONTAL_FACING,
                                        Properties.ORIENTATION, Properties.NORTH, Properties.SOUTH, Properties.WEST, Properties.EAST,
                                        Properties.BLOCK_HALF, Properties.DOUBLE_BLOCK_HALF, Properties.STAIR_SHAPE, Properties.SLAB_TYPE,
                                        Properties.WATERLOGGED, Properties.OPEN, Properties.ATTACHED, Properties.ATTACHMENT
                                    );

                                    for (Property<?> property : propertiesToCopy) {
                                        replacingBlockState = copyBlockStateProperty(bs, replacingBlockState, property);
                                    }

                                    world.setBlockState(bp, replacingBlockState, Block.NOTIFY_ALL);
                                }
                            } catch (Exception ignored) {
                            }

                            return true;
                        });
                    }

                    // Clear builder wall
                    WorldHelper.clearArea(world, BlockBox.create(
                        tacBlockPos.add(new BlockPos(BlockPos.ZERO.up().west()).rotate(wallRotation)),
                        tacBlockPos.add(new BlockPos(BlockPos.ZERO.down().east()).rotate(wallRotation))
                    ));

                    // Update info for ARS Destroyer block
                    if (world.getBlockEntity(blockPos.add(tadOffset)) instanceof TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
                        tardisArsDestroyerBlockEntity.arsStructureName = this.name;
                        tardisArsDestroyerBlockEntity.tacFacing = direction;
                        tardisArsDestroyerBlockEntity.tacBlockPos = tacBlockPos;
                        tardisArsDestroyerBlockEntity.tacIndex = tardisArsCreatorBlockEntity.index;
                        tardisArsDestroyerBlockEntity.tacIsInitial = tardisArsCreatorBlockEntity.isInitial;
                        tardisArsDestroyerBlockEntity.markDirty();
                    }

                    ModSounds.playTardisArsStructureCreatedSound(world, tacBlockPos);
                    tardis.updateRoomEntrancePortals();
                    return true;
                }

                return false;
            }
        );
    }

    public boolean destroy(PlayerEntity player, TardisStateManager tardis, BlockPos tadBlockPos) {
        ServerWorld world = tardis.getWorld();
        BlockState tadBlockState = world.getBlockState(tadBlockPos);
        if (!(tadBlockState.getBlock() instanceof TardisArsDestroyerBlock)) return false;
        if (!(world.getBlockEntity(tadBlockPos) instanceof TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity)) return false;

        Direction direction = tardisArsDestroyerBlockEntity.tacFacing;
        BlockRotation wallRotation = WorldHelper.getBlockRotation(direction);
        BlockPos tacBlockPos = tardisArsDestroyerBlockEntity.tacBlockPos;

        StructureTemplate template = this.getTemplate(world);
        if (template == null) return false;

        return this.process(
            template,
            tacBlockPos,
            direction,
            tardisArsDestroyerBlockEntity.tacIsInitial,
            tardisArsDestroyerBlockEntity.tacIndex,
            (placeSettings, blockPos, tadOffset) -> {
                Block decoratorBlock = tardis.getConsoleRoom().getDecoratorBlock();
                if (decoratorBlock == null) decoratorBlock = Blocks.CHISELED_QUARTZ_BLOCK;

                WorldHelper.fillArea(world, BlockBox.create(
                    tacBlockPos.add(new BlockPos(BlockPos.ZERO.up().west()).rotate(wallRotation)),
                    tacBlockPos.add(new BlockPos(BlockPos.ZERO.down().east()).rotate(wallRotation))
                ), decoratorBlock.getDefaultState());

                Vec3d pos = Vec3d.ofBottomCenter(tardis.getEntrancePosition().offset(tardis.getEntranceFacing()));
                BlockBox aabb = template.calculateBoundingBox(placeSettings, blockPos);
                List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, Box.from(aabb), EntityPredicates.EXCEPT_SPECTATOR);

                for (LivingEntity entity : entities) {
                    ModSounds.playTardisTeleporterSentSound(world, entity.getBlockPos());
                    EntityHelper.teleport(entity, world, pos, tardis.getEntranceFacing().asRotation());
                    ModSounds.playTardisTeleporterReceivedSound(world, BlockPos.ofFloored(pos));
                }

                WorldHelper.clearArea(world, aabb);
                world.setBlockState(tacBlockPos, ModBlocks.TARDIS_ARS_CREATOR.getBlock().getDefaultState().with(TardisArsCreatorBlock.FACING, direction), Block.NOTIFY_ALL);

                if (world.getBlockEntity(tacBlockPos) instanceof TardisArsCreatorBlockEntity tardisArsCreatorBlockEntity) {
                    tardisArsCreatorBlockEntity.index = tardisArsDestroyerBlockEntity.tacIndex;
                    tardisArsCreatorBlockEntity.isInitial = tardisArsDestroyerBlockEntity.tacIsInitial;
                    tardisArsCreatorBlockEntity.markDirty();
                }

                ModSounds.playTardisArsStructureDestroyedSound(world, tacBlockPos);
                tardis.updateRoomEntrancePortals();
                return true;
            }
        );
    }

    private boolean process(StructureTemplate template, BlockPos startBlockPos, Direction direction, boolean isInitial, int index, TriFunction<StructurePlacementData, BlockPos, BlockPos, Boolean> executor) {
        BlockRotation rotation = isInitial ? BlockRotation.NONE : WorldHelper.getBlockRotation(direction.getOpposite());
        StructurePlacementData placeSettings = new StructurePlacementData();
        placeSettings = placeSettings.setIgnoreEntities(false);
        placeSettings = placeSettings.setRotation(rotation);

        List<StructureTemplate.StructureBlockInfo> tadBlocksInfo = template.getInfosForBlock(BlockPos.ORIGIN, placeSettings, ModBlocks.TARDIS_ARS_DESTROYER.getBlock());
        BlockPos tadOffset = !tadBlocksInfo.isEmpty() ? tadBlocksInfo.getFirst().pos() : BlockPos.ORIGIN;
        BlockPos blockPos;

        if (isInitial) {
            blockPos = TardisHelper.getTardisFarPos(index + 1);
            blockPos = blockPos.subtract(tadOffset);
        }
        else {
            blockPos = startBlockPos.toImmutable();
            blockPos = blockPos.withY(startBlockPos.getY());
            blockPos = blockPos.offset(direction.getOpposite());
            blockPos = blockPos.subtract(tadOffset);
            blockPos = blockPos.down(2);
        }

        return executor.apply(placeSettings, blockPos, tadOffset);
    }

    private <T extends Comparable<T>> BlockState copyBlockStateProperty(BlockState origin, BlockState newBlockState, Property<T> property) {
        if (origin.contains(property) && newBlockState.contains(property)) newBlockState = newBlockState.with(property, origin.get(property));
        return newBlockState;
    }
}

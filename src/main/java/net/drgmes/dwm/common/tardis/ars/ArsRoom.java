package net.drgmes.dwm.common.tardis.ars;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.others.tardisroomcreator.TardisRoomCreatorBlock;
import net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer.TardisRoomDestroyerBlock;
import net.drgmes.dwm.blocks.tardis.others.tardisroomdestroyer.TardisRoomDestroyerBlockEntity;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.LevelHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate.StructureBlockInfo;

import java.util.List;

public class ArsRoom {
    private final String name;
    private final String title;
    private final ArsCategory category;

    public ArsRoom(String name, String title, ArsCategory category) {
        this.name = name;
        this.title = title;
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public Component getTitle() {
        return Component.translatable(this.title);
    }

    public ArsCategory getCategory() {
        return this.category;
    }

    public boolean place(Player player, ServerLevel level, BlockPos trcBlockPos) {
        BlockState blockState = level.getBlockState(trcBlockPos);
        if (!(blockState.getBlock() instanceof TardisRoomCreatorBlock)) return false;

        StructureTemplate template = this.getTemplate(level);
        if (template != null) {
            StructurePlaceSettings placeSettings = new StructurePlaceSettings();
            Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING).getOpposite();
            Rotation rotation = LevelHelper.getRotation(direction);

            BlockPos blockPos = trcBlockPos.immutable();
            List<StructureBlockInfo> trdBlocksInfo = template.filterBlocks(blockPos, placeSettings, ModBlocks.TARDIS_ROOM_DESTROYER.get());
            if (trdBlocksInfo.size() > 0) {
                blockPos = blockPos.subtract(trdBlocksInfo.get(0).pos.subtract(blockPos).rotate(rotation)).atY(blockPos.getY() - 2);
            }

            blockPos = blockPos.relative(direction);
            placeSettings = placeSettings.setRotation(rotation);
            placeSettings = placeSettings.setIgnoreEntities(false);

            BoundingBox aabb = template.getBoundingBox(placeSettings, blockPos);
            for (double x = aabb.minX(); x <= aabb.maxX(); x++) {
                for (double y = aabb.minY(); y <= aabb.maxY(); y++) {
                    for (double z = aabb.minZ(); z <= aabb.maxZ(); z++) {
                        BlockPos bp = new BlockPos(x, y, z);
                        if (!level.getBlockState(bp).isAir()) {
                            MutableComponent posText = Component.literal("[" + bp.getX() + " " + bp.getY() + " " + bp.getZ() + "]");
                            posText.setStyle(posText.getStyle().withColor(ChatFormatting.YELLOW));

                            player.displayClientMessage(Component.translatable("message." + DWM.MODID + ".tardis.ars_interface.generated.failed.details", posText), false);
                            return false;
                        }
                    }
                }
            }

            if (template.placeInWorld(level, blockPos, blockPos, placeSettings, level.random, 3)) {
                this.fillWall(level, rotation, trcBlockPos, Blocks.AIR.defaultBlockState());

                template.filterBlocks(blockPos, placeSettings, ModBlocks.TARDIS_ROOM_DESTROYER.get()).forEach((bi) -> {
                    if (level.getBlockEntity(bi.pos) instanceof TardisRoomDestroyerBlockEntity tardisRoomDestroyerBlockEntity) {
                        tardisRoomDestroyerBlockEntity.room = this;
                        tardisRoomDestroyerBlockEntity.setChanged();
                    }
                });

                ModSounds.playTardisRoomCreatedSound(level, trcBlockPos);
                return true;
            }
        }

        return false;
    }

    public boolean destroy(Player player, ServerLevel level, BlockPos trdBlockPos) {
        BlockState blockState = level.getBlockState(trdBlockPos);
        if (!(blockState.getBlock() instanceof TardisRoomDestroyerBlock)) return false;

        StructureTemplate template = this.getTemplate(level);
        if (template != null) {
            StructurePlaceSettings placeSettings = new StructurePlaceSettings();
            Direction direction = blockState.getValue(BlockStateProperties.HORIZONTAL_FACING);
            Rotation rotation = LevelHelper.getRotation(direction.getOpposite());

            BlockPos destroyerBlockPos = trdBlockPos.immutable();
            List<StructureBlockInfo> trdBlocksInfo = template.filterBlocks(destroyerBlockPos, placeSettings, ModBlocks.TARDIS_ROOM_DESTROYER.get());
            if (trdBlocksInfo.size() > 0) {
                destroyerBlockPos = destroyerBlockPos.subtract(trdBlocksInfo.get(0).pos.subtract(destroyerBlockPos).rotate(rotation));
            }

            BlockPos trcBlockPos = trdBlockPos.immutable().relative(direction).atY(trdBlockPos.getY() + 2);
            placeSettings = placeSettings.setRotation(rotation);

            BoundingBox aabb = template.getBoundingBox(placeSettings, destroyerBlockPos);
            for (double x = aabb.minX(); x <= aabb.maxX(); x++) {
                for (double y = aabb.minY(); y <= aabb.maxY(); y++) {
                    for (double z = aabb.minZ(); z <= aabb.maxZ(); z++) {
                        level.setBlock(new BlockPos(x, y, z), Blocks.AIR.defaultBlockState(), 3);
                    }
                }
            }

            this.fillWall(level, rotation, trcBlockPos, Blocks.GRAY_TERRACOTTA.defaultBlockState());
            level.setBlock(trcBlockPos, ModBlocks.TARDIS_ROOM_CREATOR.get().defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, direction), 3);

            ModSounds.playTardisRoomDestroyedSound(level, trcBlockPos);
            return true;
        }

        return false;
    }

    private StructureTemplate getTemplate(ServerLevel level) {
        String categoryPath = "";

        if (this.category != null) categoryPath = this.category.getPath().replace("_", "/") + "/";
        ResourceLocation path = new ResourceLocation(DWM.MODID, "rooms/ars/" + categoryPath + this.name);

        return level.getStructureManager().getOrCreate(path);
    }

    private void fillWall(ServerLevel level, Rotation rotation, BlockPos blockPos, BlockState blockState) {
        level.setBlock(blockPos, blockState, 3);
        level.setBlock(blockPos.offset(BlockPos.ZERO.west().rotate(rotation)), blockState, 3);
        level.setBlock(blockPos.offset(BlockPos.ZERO.east().rotate(rotation)), blockState, 3);
        level.setBlock(blockPos.offset(BlockPos.ZERO.above()), blockState, 3);
        level.setBlock(blockPos.offset(BlockPos.ZERO.above().west().rotate(rotation)), blockState, 3);
        level.setBlock(blockPos.offset(BlockPos.ZERO.above().east().rotate(rotation)), blockState, 3);
        level.setBlock(blockPos.offset(BlockPos.ZERO.below()), blockState, 3);
        level.setBlock(blockPos.offset(BlockPos.ZERO.below().west().rotate(rotation)), blockState, 3);
        level.setBlock(blockPos.offset(BlockPos.ZERO.below().east().rotate(rotation)), blockState, 3);
    }
}

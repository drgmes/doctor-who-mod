package net.drgmes.dwm.common.tardis.ars;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.CommonHelper;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

import java.util.List;

public class ArsStructure {
    private final String name;
    private final String title;
    private final ArsCategory category;

    public ArsStructure(String name, String title, ArsCategory category) {
        this.name = name;
        this.title = title;
        this.category = category;
    }

    public String getName() {
        return this.name;
    }

    public Text getTitle() {
        return Text.translatable(this.title);
    }

    public ArsCategory getCategory() {
        return this.category;
    }

    public boolean place(PlayerEntity player, ServerWorld world, BlockPos trcBlockPos) {
        BlockState blockState = world.getBlockState(trcBlockPos);
        if (!(blockState.getBlock() instanceof TardisArsCreatorBlock)) return false;

        StructureTemplate template = this.getTemplate(world);
        if (template != null) {
            StructurePlacementData placeSettings = new StructurePlacementData();
            Direction direction = blockState.get(TardisArsCreatorBlock.FACING).getOpposite();
            BlockRotation rotation = CommonHelper.getBlockRotation(direction);

            BlockPos blockPos = trcBlockPos.toImmutable();
            List<StructureTemplate.StructureBlockInfo> trdBlocksInfo = template.getInfosForBlock(blockPos, placeSettings, ModBlocks.TARDIS_ARS_DESTROYER.getBlock());
            if (trdBlocksInfo.size() > 0) blockPos = blockPos.subtract(trdBlocksInfo.get(0).pos.subtract(blockPos).rotate(rotation)).withY(blockPos.getY() - 2);

            blockPos = blockPos.offset(direction);
            placeSettings = placeSettings.setRotation(rotation);
            placeSettings = placeSettings.setIgnoreEntities(false);

            BlockBox aabb = template.calculateBoundingBox(placeSettings, blockPos);
            for (double x = aabb.getMinX(); x <= aabb.getMaxX(); x++) {
                for (double y = aabb.getMinY(); y <= aabb.getMaxY(); y++) {
                    for (double z = aabb.getMinZ(); z <= aabb.getMaxZ(); z++) {
                        BlockPos bp = new BlockPos(x, y, z);
                        if (!world.getBlockState(bp).isAir()) {
                            MutableText posText = Text.literal("[" + bp.getX() + " " + bp.getY() + " " + bp.getZ() + "]").formatted(Formatting.YELLOW);
                            player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.ars_interface.generated.failed.details", posText), false);
                            return false;
                        }
                    }
                }
            }

            if (template.place(world, blockPos, blockPos, placeSettings, world.random, 3)) {
                this.fillWall(world, rotation, trcBlockPos, Blocks.AIR.getDefaultState());

                template.getInfosForBlock(blockPos, placeSettings, ModBlocks.TARDIS_ARS_DESTROYER.getBlock()).forEach((bi) -> {
                    if (world.getBlockEntity(bi.pos) instanceof TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
                        tardisArsDestroyerBlockEntity.arsStructure = this;
                        tardisArsDestroyerBlockEntity.markDirty();
                    }
                });

                ModSounds.playTardisArsStructureCreatedSound(world, trcBlockPos);
                return true;
            }
        }

        return false;
    }

    public boolean destroy(PlayerEntity player, ServerWorld world, BlockPos trdBlockPos) {
        BlockState blockState = world.getBlockState(trdBlockPos);
        if (!(blockState.getBlock() instanceof TardisArsDestroyerBlock)) return false;

        StructureTemplate template = this.getTemplate(world);
        if (template != null) {
            StructurePlacementData placeSettings = new StructurePlacementData();
            Direction direction = blockState.get(TardisArsDestroyerBlock.FACING);
            BlockRotation rotation = CommonHelper.getBlockRotation(direction.getOpposite());

            BlockPos destroyerBlockPos = trdBlockPos.toImmutable();
            List<StructureTemplate.StructureBlockInfo> trdBlocksInfo = template.getInfosForBlock(destroyerBlockPos, placeSettings, ModBlocks.TARDIS_ARS_DESTROYER.getBlock());
            if (trdBlocksInfo.size() > 0) destroyerBlockPos = destroyerBlockPos.subtract(trdBlocksInfo.get(0).pos.subtract(destroyerBlockPos).rotate(rotation));

            BlockPos trcBlockPos = trdBlockPos.toImmutable().offset(direction).withY(trdBlockPos.getY() + 2);
            placeSettings = placeSettings.setRotation(rotation);

            BlockBox aabb = template.calculateBoundingBox(placeSettings, destroyerBlockPos);
            for (double x = aabb.getMinX(); x <= aabb.getMaxX(); x++) {
                for (double y = aabb.getMinY(); y <= aabb.getMaxY(); y++) {
                    for (double z = aabb.getMinZ(); z <= aabb.getMaxZ(); z++) {
                        world.setBlockState(new BlockPos(x, y, z), Blocks.AIR.getDefaultState(), 3);
                    }
                }
            }

            this.fillWall(world, rotation, trcBlockPos, Blocks.GRAY_TERRACOTTA.getDefaultState());
            world.setBlockState(trcBlockPos, ModBlocks.TARDIS_ARS_CREATOR.getBlock().getDefaultState().with(TardisArsDestroyerBlock.FACING, direction), 3);

            ModSounds.playTardisArsStructureDestroyedSound(world, trcBlockPos);
            return true;
        }

        return false;
    }

    private StructureTemplate getTemplate(ServerWorld world) {
        String categoryPath = "";

        if (this.category != null) categoryPath = this.category.getPath().replace("_", "/") + "/";
        Identifier path = DWM.getIdentifier("ars/" + categoryPath + this.name);

        return world.getStructureTemplateManager().getTemplateOrBlank(path);
    }

    private void fillWall(ServerWorld world, BlockRotation rotation, BlockPos blockPos, BlockState blockState) {
        world.setBlockState(blockPos, blockState, 3);
        world.setBlockState(blockPos.add(BlockPos.ZERO.up()), blockState, 3);
        world.setBlockState(blockPos.add(BlockPos.ZERO.down()), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.west()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.east()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.up().west()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.up().east()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.down().west()).rotate(rotation)), blockState, 3);
        world.setBlockState(blockPos.add(new BlockPos(BlockPos.ZERO.down().east()).rotate(rotation)), blockState, 3);
    }
}
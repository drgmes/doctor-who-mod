package net.drgmes.dwm.common.tardis.ars;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarscreator.TardisArsCreatorBlockEntity;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlock;
import net.drgmes.dwm.blocks.tardis.misc.tardisarsdestroyer.TardisArsDestroyerBlockEntity;
import net.drgmes.dwm.common.tardis.TardisStateManager;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModSounds;
import net.drgmes.dwm.utils.helpers.TardisHelper;
import net.drgmes.dwm.utils.helpers.WorldHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.StructureTemplate;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Formatting;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Direction;
import org.apache.commons.lang3.function.TriFunction;

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

    public StructureTemplate getTemplate(ServerWorld world) {
        String categoryPath = this.category != null ? this.category.getPath().replace("_", "/") + "/" : "";
        return world.getStructureTemplateManager().getTemplateOrBlank(DWM.getIdentifier("ars/" + categoryPath + this.name));
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
                boolean isAreaEmpty = WorldHelper.foreachArea(template.calculateBoundingBox(placeSettings, blockPos), (bp) -> {
                    if (!world.getBlockState(bp).isAir()) {
                        MutableText posText = Text.literal("[" + bp.getX() + " " + bp.getY() + " " + bp.getZ() + "]").formatted(Formatting.YELLOW);
                        player.sendMessage(Text.translatable("message." + DWM.MODID + ".tardis.ars_interface.generated.failed.details", posText), false);
                        return false;
                    }

                    return true;
                });

                if (isAreaEmpty && template.place(world, blockPos, BlockPos.ORIGIN, placeSettings, world.random, Block.NOTIFY_ALL)) {
                    WorldHelper.clearArea(world, BlockBox.create(
                        tacBlockPos.add(new BlockPos(BlockPos.ZERO.up().west()).rotate(wallRotation)),
                        tacBlockPos.add(new BlockPos(BlockPos.ZERO.down().east()).rotate(wallRotation))
                    ));

                    if (world.getBlockEntity(blockPos.add(tadOffset)) instanceof TardisArsDestroyerBlockEntity tardisArsDestroyerBlockEntity) {
                        tardisArsDestroyerBlockEntity.arsStructure = this;
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
                WorldHelper.fillArea(world, BlockBox.create(
                    tacBlockPos.add(new BlockPos(BlockPos.ZERO.up().west()).rotate(wallRotation)),
                    tacBlockPos.add(new BlockPos(BlockPos.ZERO.down().east()).rotate(wallRotation))
                ), tardis.getConsoleRoom().getDecoratorBlock().getDefaultState());

                BlockPos entrancePosition = tardis.getEntrancePosition();
                BlockBox aabb = template.calculateBoundingBox(placeSettings, blockPos);
                List<LivingEntity> entities = world.getEntitiesByClass(LivingEntity.class, Box.from(aabb), EntityPredicates.EXCEPT_SPECTATOR);

                for (LivingEntity entity : entities) {
                    ModSounds.playTardisTeleporterSentSound(world, entity.getBlockPos());
                    entity.setPitch(0);
                    entity.setYaw(tardis.getEntranceFacing().asRotation());
                    entity.teleport(entrancePosition.getX() + 0.5, entrancePosition.getY(), entrancePosition.getZ() + 0.5);
                    ModSounds.playTardisTeleporterReceivedSound(world, entrancePosition);
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
        BlockPos tadOffset = tadBlocksInfo.size() > 0 ? tadBlocksInfo.get(0).pos.withY(0) : BlockPos.ORIGIN;
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
}

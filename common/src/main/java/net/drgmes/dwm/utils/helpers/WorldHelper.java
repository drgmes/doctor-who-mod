package net.drgmes.dwm.utils.helpers;

import com.mojang.datafixers.util.Pair;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.structure.StructurePiece;
import net.minecraft.structure.StructureStart;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.Structure;

import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;

public class WorldHelper {
    public static final Box INFINITE_EXTENT_AABB = new Box(
        Double.NEGATIVE_INFINITY,
        Double.NEGATIVE_INFINITY,
        Double.NEGATIVE_INFINITY,
        Double.POSITIVE_INFINITY,
        Double.POSITIVE_INFINITY,
        Double.POSITIVE_INFINITY
    );

    public static Box getRenderBoundingBox(BlockEntity blockEntity) {
        BlockState blockState = blockEntity.getCachedState();
        BlockPos blockPos = blockEntity.getPos();
        World world = blockEntity.getWorld();

        try {
            VoxelShape shape = blockState.getCollisionShape(world, blockPos);
            if (!shape.isEmpty()) return shape.getBoundingBox().offset(blockPos).expand(2);
        } catch (Exception e) {
            return new Box(blockPos.add(-3, 0, -3).toCenterPos(), blockPos.add(3, 3, 3).toCenterPos());
        }

        return INFINITE_EXTENT_AABB;
    }

    public static BlockRotation getBlockRotation(Direction direction) {
        return switch (direction) {
            case WEST -> BlockRotation.CLOCKWISE_90;
            case EAST -> BlockRotation.COUNTERCLOCKWISE_90;
            case NORTH -> BlockRotation.CLOCKWISE_180;
            default -> BlockRotation.NONE;
        };
    }

    public static boolean checkBlockIsEmpty(BlockState blockState, boolean ignoreFluids) {
        return (
            blockState.isAir()
                || blockState.getFluidState().isIn(FluidTags.WATER)
                || (blockState.isReplaceable() && (ignoreFluids || blockState.getFluidState().isEmpty()))
        );
    }

    public static boolean checkBlockIsSolid(BlockState blockState) {
        return (
            !checkBlockIsEmpty(blockState, false)
                && blockState.getFluidState().isEmpty()
                && blockState.isSolid()
        );
    }

    public static boolean checkBlockIsTransparent(BlockState blockState) {
        return (
            !checkBlockIsSolid(blockState)
                || blockState.contains(Properties.WATERLOGGED)
                || !blockState.isOpaque()
        );
    }

    public static boolean foreachArea(BlockBox aabb, Function<BlockPos, Boolean> action) {
        for (double x = aabb.getMaxX(); x >= aabb.getMinX(); x--) {
            for (double y = aabb.getMaxY(); y >= aabb.getMinY(); y--) {
                for (double z = aabb.getMaxZ(); z >= aabb.getMinZ(); z--) {
                    if (!action.apply(new BlockPos((int) x, (int) y, (int) z))) return false;
                }
            }
        }

        return true;
    }

    public static void fillArea(World world, BlockBox aabb, Function<BlockPos, BlockState> newBlockStateGetter) {
        foreachArea(aabb, (bp) -> {
            world.setBlockState(bp, newBlockStateGetter.apply(bp), Block.NOTIFY_ALL);
            return true;
        });
    }

    public static void fillArea(World world, BlockBox aabb, BlockState newBlockState) {
        fillArea(world, aabb, (bp) -> newBlockState);
    }

    public static void clearArea(World world, BlockBox aabb) {
        fillArea(world, aabb, Blocks.AIR.getDefaultState());
    }

    public static Optional<RegistryKey<Biome>> locateBiome(ServerWorld world, BlockPos blockPos) {
        if (world == null || blockPos == null) return Optional.empty();
        Pair<BlockPos, RegistryEntry<Biome>> result = world.locateBiome((entry) -> true, blockPos, 1, 1, 1);
        return result == null ? Optional.empty() : result.getSecond().getKey();
    }

    public static Optional<RegistryKey<Structure>> locateStructure(ServerWorld world, BlockPos blockPos) {
        if (world == null || blockPos == null) return Optional.empty();

        int radius = 3;
        Registry<Structure> registry = world.getRegistryManager().get(RegistryKeys.STRUCTURE);
        ChunkPos centerChunkPos = new ChunkPos(new BlockPos(blockPos.getX(), blockPos.getY(), blockPos.getZ()));

        for (int x = -radius; x <= radius; x++) {
            for (int z = -radius; z <= radius; z++) {
                ChunkPos chunkPos = new ChunkPos(centerChunkPos.x + x, centerChunkPos.z + z);
                Collection<StructureStart> structureStarts = world.getChunk(chunkPos.x, chunkPos.z).getStructureStarts().values();
                if (structureStarts.isEmpty()) continue;

                for (StructureStart structureStart : structureStarts) {
                    if (structureStart == null || !structureStart.hasChildren()) continue;

                    BlockBox structureBox = structureStart.getBoundingBox();
                    if (structureBox == null) continue;

                    if (blockPos.getY() >= structureBox.getMinY() && blockPos.getY() <= structureBox.getMaxY()) {
                        for (StructurePiece piece : structureStart.getChildren()) {
                            BlockBox pieceBox = piece.getBoundingBox();
                            if (pieceBox == null) continue;

                            if (blockPos.getY() >= pieceBox.getMinY() && blockPos.getY() <= pieceBox.getMaxY()) {
                                Structure structure = structureStart.getStructure();
                                if (structure == null) continue;

                                Optional<RegistryKey<Structure>> structureKeyHolder = registry.getKey(structure);
                                if (structureKeyHolder.isPresent()) return structureKeyHolder;
                            }
                        }
                    }
                }
            }
        }

        return Optional.empty();
    }
}

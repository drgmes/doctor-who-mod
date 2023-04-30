package net.drgmes.dwm.utils.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.state.property.Properties;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public class WorldHelper {
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
            || (blockState.getMaterial().isReplaceable() && (ignoreFluids || blockState.getFluidState().isEmpty()))
        );
    }

    public static boolean checkBlockIsSolid(BlockState blockState) {
        return (
            !checkBlockIsEmpty(blockState, false)
            && blockState.getFluidState().isEmpty()
            && blockState.getMaterial().isSolid()
        );
    }

    public static boolean checkBlockIsTransparent(BlockState blockState) {
        return (
            !checkBlockIsSolid(blockState)
            || blockState.contains(Properties.WATERLOGGED)
            || !blockState.getMaterial().blocksLight()
            || !blockState.isOpaque()
        );
    }

    public static void clearArea(World world, BlockBox aabb) {
        for (double x = aabb.getMaxX(); x >= aabb.getMinX(); x--) {
            for (double y = aabb.getMaxY(); y >= aabb.getMinY(); y--) {
                for (double z = aabb.getMaxZ(); z >= aabb.getMinZ(); z--) {
                    BlockPos blockPos = new BlockPos((int) x, (int) y, (int) z);
                    BlockState blockState = world.getBlockState(blockPos);

                    if (blockState.contains(Properties.DOUBLE_BLOCK_HALF) && blockState.get(Properties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) continue;
                    world.removeBlock(blockPos, false);
                }
            }
        }
    }
}

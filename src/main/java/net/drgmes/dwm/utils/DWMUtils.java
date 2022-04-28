package net.drgmes.dwm.utils;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class DWMUtils {
    private static Map<String, Thread> threads = new HashMap<>();

    public static Thread runInThread(String name, Runnable consumer) {
        if (DWMUtils.threads.containsKey(name)) DWMUtils.threads.get(name).interrupt();

        Thread thread = new Thread(consumer);
        DWMUtils.threads.put(name, thread);
        thread.start();

        return thread;
    }

    public static boolean checkBlockIsEmpty(BlockState blockState) {
        return (
            blockState.isAir()
            || blockState.getFluidState().is(FluidTags.WATER)
            || (blockState.getMaterial().isReplaceable() && blockState.getFluidState().isEmpty())
        );
    }

    public static boolean checkBlockIsSolid(BlockState blockState) {
        return (
            !checkBlockIsEmpty(blockState)
            && blockState.getFluidState().isEmpty()
            && blockState.getMaterial().isSolid()
        );
    }

    public static boolean checkBlockIsTransparent(BlockState blockState) {
        return (
            !checkBlockIsSolid(blockState)
            && !blockState.hasBlockEntity()
            || !blockState.getMaterial().isSolidBlocking()
            || blockState.hasProperty(BlockStateProperties.WATERLOGGED)
        );
    }
}

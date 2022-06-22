package net.drgmes.dwm.utils;

import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.HashMap;
import java.util.Map;

public class DWMUtils {
    private static final Map<String, Thread> threads = new HashMap<>();

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
                || blockState.hasProperty(BlockStateProperties.WATERLOGGED)
                || !blockState.getMaterial().isSolidBlocking()
                || !blockState.canOcclude()
        );
    }

    public static String capitaliseAllWords(String str) {
        if (str == null) return null;

        boolean space = true;
        StringBuilder buffer = new StringBuilder(str.length());

        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (Character.isWhitespace(ch)) {
                buffer.append(ch);
                space = true;
            } else if (space) {
                buffer.append(Character.toTitleCase(ch));
                space = false;
            } else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }
}

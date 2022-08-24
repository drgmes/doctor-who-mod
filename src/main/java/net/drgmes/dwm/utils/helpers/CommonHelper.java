package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

public class CommonHelper {
    private static final Map<String, Thread> threads = new HashMap<>();

    public static Thread runInThread(String name, Runnable consumer) {
        if (CommonHelper.threads.containsKey(name)) CommonHelper.threads.get(name).interrupt();

        Thread thread = new Thread(consumer);
        CommonHelper.threads.put(name, thread);
        thread.start();

        return thread;
    }

    public static Identifier loadRemoteImage(String id, URL url) {
        try {
            URLConnection uc = url.openConnection();
            uc.connect();

            NativeImageBackedTexture image = new NativeImageBackedTexture(NativeImage.read(uc.getInputStream()));
            return MinecraftClient.getInstance().getTextureManager().registerDynamicTexture(DWM.MODID + "_" + id, image);
        } catch (IOException ignored) {
        }

        return null;
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
        return (blockState.isAir()
            || blockState.getFluidState().isIn(FluidTags.WATER)
            || (blockState.getMaterial().isReplaceable() && (ignoreFluids || blockState.getFluidState().isEmpty()))
        );
    }

    public static boolean checkBlockIsSolid(BlockState blockState) {
        return (!checkBlockIsEmpty(blockState, false)
            && blockState.getFluidState().isEmpty()
            && blockState.getMaterial().isSolid()
        );
    }

    public static boolean checkBlockIsTransparent(BlockState blockState) {
        return (!checkBlockIsSolid(blockState)
            || blockState.contains(Properties.WATERLOGGED)
            || !blockState.getMaterial().blocksLight()
            || !blockState.isOpaque()
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
            }
            else if (space) {
                buffer.append(Character.toTitleCase(ch));
                space = false;
            }
            else {
                buffer.append(ch);
            }
        }

        return buffer.toString();
    }
}

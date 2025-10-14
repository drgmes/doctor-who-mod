package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.item.DyeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

public class CommonHelper {
    public static final List<String> COLORS = Stream.of(
        Items.BLACK_DYE, Items.BLUE_DYE, Items.BROWN_DYE, Items.CYAN_DYE, Items.GRAY_DYE, Items.GREEN_DYE,
        Items.LIGHT_BLUE_DYE, Items.LIME_DYE, Items.MAGENTA_DYE, Items.ORANGE_DYE, Items.PINK_DYE, Items.PURPLE_DYE,
        Items.RED_DYE, Items.LIGHT_GRAY_DYE, Items.WHITE_DYE, Items.YELLOW_DYE
    ).map((dyeItem) -> ((DyeItem) dyeItem).getColor().getName()).toList();

    public static final List<String> WOODS = Stream.of(
        Blocks.ACACIA_PLANKS, Blocks.BAMBOO_PLANKS, Blocks.BIRCH_PLANKS, Blocks.CHERRY_PLANKS,
        Blocks.CRIMSON_PLANKS, Blocks.DARK_OAK_PLANKS, Blocks.JUNGLE_PLANKS, Blocks.MANGROVE_PLANKS,
        Blocks.OAK_PLANKS, Blocks.SPRUCE_PLANKS, Blocks.WARPED_PLANKS
    ).map((logBlock) -> Registries.BLOCK.getId(logBlock).getPath().replace("_planks", "")).toList();

    private static final Map<String, Thread> threads = new HashMap<>();

    public static Thread runInThread(String name, Runnable consumer) {
        if (CommonHelper.threads.containsKey(name)) CommonHelper.threads.get(name).interrupt();

        Thread thread = new Thread(consumer);
        CommonHelper.threads.put(name, thread);
        thread.start();

        return thread;
    }

    public static int getColorWithAlpha(int color, float alpha) {
        return (color & 0x00FFFFFF) | ((int) (clamp(alpha, 0F, 1F) * 255) << 24);
    }

    public static float clamp(float value, float min, float max) {
        return Math.max(min, Math.min(max, value));
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

    public static String formatNumberString(int number) {
        String str = String.valueOf(number);
        if (number >= 1000000000) str = String.format("%.1f", (float) number / 1000000000) + "b";
        else if (number >= 1000000) str = String.format("%.1f", (float) number / 1000000) + "m";
        else if (number >= 1000) str = String.format("%.1f", (float) number / 1000) + "k";
        return str.replace(",", ".");
    }

    public static String formatIndexString(int index, int length) {
        return String.format("%1$" + length + "s", index).replace(' ', '0');
    }

    public static String formatIndexString(int index) {
        return formatIndexString(index, 5);
    }

    public static String formatRegistryKey(RegistryKey<?> key) {
        return CommonHelper.capitaliseAllWords(key.getValue().getPath().replace("_", " "));
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

    public static NbtComponent getItemStackData(ItemStack itemStack) {
        return itemStack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT);
    }

    public static void updateItemStackData(ItemStack itemStack, Consumer<NbtCompound> updater) {
        NbtCompound tag = getItemStackData(itemStack).copyNbt();
        updater.accept(tag);
        itemStack.set(DataComponentTypes.CUSTOM_DATA, NbtComponent.of(tag));
    }
}

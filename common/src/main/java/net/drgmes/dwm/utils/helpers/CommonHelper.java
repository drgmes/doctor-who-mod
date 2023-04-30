package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.texture.NativeImage;
import net.minecraft.client.texture.NativeImageBackedTexture;
import net.minecraft.util.Identifier;

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

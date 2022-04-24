package net.drgmes.dwm.utils;

import java.util.HashMap;
import java.util.Map;

public class DWMUtils {
    private static Map<String, Thread> threads = new HashMap<>();

    public static Thread runInThread(String name, Runnable consumer) {
        if (DWMUtils.threads.containsKey(name)) DWMUtils.threads.get(name).interrupt();

        Thread thread = new Thread(consumer);
        DWMUtils.threads.put(name, thread);
        thread.start();

        return thread;
    }
}

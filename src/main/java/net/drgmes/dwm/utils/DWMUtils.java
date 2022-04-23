package net.drgmes.dwm.utils;

public class DWMUtils {
    public static Thread runInThread(Runnable consumer) {
        Thread thread = new Thread(consumer);
        thread.start();
        return thread;
    }
}

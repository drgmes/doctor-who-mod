package net.drgmes.dwm.common.tardis.ars;

import java.util.HashMap;
import java.util.Map;

import net.drgmes.dwm.DWM;

public class ArsCategories {
    public static final Map<String, ArsCategory> CATEGORIES = new HashMap<>();

    public static final ArsCategory ROOMS = registerWithTag("rooms", "title." + DWM.MODID + ".ars.category.rooms");
    public static final ArsCategory ROOMS_TECH = registerWithTag("rooms_tech", "title." + DWM.MODID + ".ars.category.rooms.tech", ROOMS);
    // public static final ArsCategory ROOMS_WOODEN = registerWithTag("rooms_wooden", "title." + DWM.MODID + ".ars.category.rooms.wooden", ROOMS);

    public static final ArsCategory HALLWAYS = registerWithTag("hallways", "title." + DWM.MODID + ".ars.category.hallways");
    public static final ArsCategory HALLWAYS_TECH = registerWithTag("hallways_tech", "title." + DWM.MODID + ".ars.category.hallways.tech", HALLWAYS);
    // public static final ArsCategory HALLWAYS_WOODEN = registerWithTag("hallways_wooden", "title." + DWM.MODID + ".ars.category.hallways.wooden", HALLWAYS);

    public static void init() {
    }

    public static ArsCategory register(String path, String title, String tag, ArsCategory parent) {
        ArsCategory category = new ArsCategory(path, title, tag, parent);
        ArsCategories.CATEGORIES.put(path, category);
        return category;
    }

    public static ArsCategory register(String path, String title, String tag) {
        return ArsCategories.register(path, title, tag, null);
    }

    public static ArsCategory register(String path, String title, ArsCategory parent) {
        return ArsCategories.register(path, title, title, parent);
    }

    public static ArsCategory register(String path, String title) {
        return ArsCategories.register(path, title, title, null);
    }

    public static ArsCategory registerWithTag(String path, String title, ArsCategory parent) {
        return ArsCategories.register(path, title, title + ".tag", parent);
    }

    public static ArsCategory registerWithTag(String path, String title) {
        return ArsCategories.register(path, title, title + ".tag", null);
    }
}

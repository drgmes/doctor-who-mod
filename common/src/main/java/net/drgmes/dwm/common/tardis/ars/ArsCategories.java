package net.drgmes.dwm.common.tardis.ars;

import java.util.HashMap;
import java.util.Map;

public class ArsCategories {
    public static final Map<String, ArsCategory> CATEGORIES = new HashMap<>();

    public static ArsCategory register(String path, String title, String tag, ArsCategory parent) {
        ArsCategory category = new ArsCategory(path, title, tag, parent);
        ArsCategories.CATEGORIES.put(path, category);
        return category;
    }
}

package net.drgmes.dwm.common.tardis.ars;

import java.util.HashMap;
import java.util.Map;

public class ArsCategories {
    public static final Map<String, ArsCategory> CATEGORIES = new HashMap<>();

    public static ArsCategory register(String name, String title, String tag, String parent) {
        ArsCategory arsCategory = new ArsCategory(name, title, tag, parent);
        ArsCategories.CATEGORIES.put(name, arsCategory);
        return arsCategory;
    }
}

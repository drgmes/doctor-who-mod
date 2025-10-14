package net.drgmes.dwm.common.tardis.ars;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArsCategories {
    public static final Map<String, ArsCategory> CATEGORIES = new LinkedHashMap<>();

    public static ArsCategory register(String name, String title, String tag, String parent, int order) {
        ArsCategory arsCategory = new ArsCategory(name, title, tag, parent, order);
        ArsCategories.CATEGORIES.put(name, arsCategory);
        return arsCategory;
    }
}

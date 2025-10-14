package net.drgmes.dwm.common.tardis.ars;

import java.util.LinkedHashMap;
import java.util.Map;

public class ArsStructures {
    public static final Map<String, ArsStructure> STRUCTURES = new LinkedHashMap<>();

    public static ArsStructure register(String name, String path, String title, String category, int order) {
        ArsStructure arsStructure = new ArsStructure(name, path, title, category, order);
        ArsStructures.STRUCTURES.put(name, arsStructure);
        return arsStructure;
    }
}

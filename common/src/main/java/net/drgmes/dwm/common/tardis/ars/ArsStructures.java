package net.drgmes.dwm.common.tardis.ars;

import java.util.HashMap;
import java.util.Map;

public class ArsStructures {
    public static final Map<String, ArsStructure> STRUCTURES = new HashMap<>();

    public static ArsStructure register(String name, String path, String title, String category) {
        ArsStructure arsStructure = new ArsStructure(name, path, title, category);
        ArsStructures.STRUCTURES.put(name, arsStructure);
        return arsStructure;
    }
}

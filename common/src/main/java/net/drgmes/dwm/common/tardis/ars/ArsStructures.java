package net.drgmes.dwm.common.tardis.ars;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArsStructures {
    public static final Map<ArsCategory, Map<String, ArsStructure>> STRUCTURES = new HashMap<>();

    public static ArsStructure register(String path, String structurePath, String title, ArsCategory category) {
        if (!ArsStructures.STRUCTURES.containsKey(category)) ArsStructures.STRUCTURES.put(category, new HashMap<>());

        ArsStructure structure = new ArsStructure(path, structurePath, title, category);
        ArsStructures.STRUCTURES.get(category).put(path, structure);
        return structure;
    }

    public static List<ArsStructure> getAllStructures() {
        List<ArsStructure> list = new ArrayList<>();
        STRUCTURES.forEach((category, map) -> list.addAll(map.values()));
        return list;
    }
}

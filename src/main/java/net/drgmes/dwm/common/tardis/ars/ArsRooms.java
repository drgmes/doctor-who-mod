package net.drgmes.dwm.common.tardis.ars;

import java.util.HashMap;
import java.util.Map;

import net.drgmes.dwm.DWM;

public class ArsRooms {
    public static final Map<ArsCategory, Map<String, ArsRoom>> ROOMS = new HashMap<>();

    public static final ArsRoom HALLWAY_TECH_SHORT = register("hallway_tech_short", "title." + DWM.MODID + ".ars.room.hallway_tech_short", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_MEDIYM = register("hallway_tech_medium", "title." + DWM.MODID + ".ars.room.hallway_tech_medium", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_LONG = register("hallway_tech_long", "title." + DWM.MODID + ".ars.room.hallway_tech_long", ArsCategories.HALLWAYS_TECH);

    public static void init() {
    }

    public static ArsRoom register(String path, String title, ArsCategory category) {
        if (!ArsRooms.ROOMS.containsKey(category)) ArsRooms.ROOMS.put(category, new HashMap<>());

        ArsRoom room = new ArsRoom(path, title, category);
        ArsRooms.ROOMS.get(category).put(path, room);
        return room;
    }
}

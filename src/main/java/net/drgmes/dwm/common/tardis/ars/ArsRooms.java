package net.drgmes.dwm.common.tardis.ars;

import java.util.HashMap;
import java.util.Map;

import net.drgmes.dwm.DWM;

public class ArsRooms {
    public static final Map<ArsCategory, Map<String, ArsRoom>> ROOMS = new HashMap<>();

    public static final ArsRoom HALLWAY_TECH_SHORT = register("hallway_tech_short", "title." + DWM.MODID + ".ars.room.hallway_tech_short", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_MEDIYM = register("hallway_tech_medium", "title." + DWM.MODID + ".ars.room.hallway_tech_medium", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_LONG = register("hallway_tech_long", "title." + DWM.MODID + ".ars.room.hallway_tech_long", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_JUNCTION_X = register("hallway_tech_junction_x", "title." + DWM.MODID + ".ars.room.hallway_tech_junction_x", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_JUNCTION_T = register("hallway_tech_junction_t", "title." + DWM.MODID + ".ars.room.hallway_tech_junction_t", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_TURN_LEFT = register("hallway_tech_turn_left", "title." + DWM.MODID + ".ars.room.hallway_tech_turn_left", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_TURN_RIGHT = register("hallway_tech_turn_right", "title." + DWM.MODID + ".ars.room.hallway_tech_turn_right", ArsCategories.HALLWAYS_TECH);

    public static void init() {
    }

    public static ArsRoom register(String path, String title, ArsCategory category) {
        if (!ArsRooms.ROOMS.containsKey(category)) ArsRooms.ROOMS.put(category, new HashMap<>());

        ArsRoom room = new ArsRoom(path, title, category);
        ArsRooms.ROOMS.get(category).put(path, room);
        return room;
    }
}

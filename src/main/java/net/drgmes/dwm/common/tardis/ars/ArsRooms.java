package net.drgmes.dwm.common.tardis.ars;

import net.drgmes.dwm.DWM;

import java.util.HashMap;
import java.util.Map;

public class ArsRooms {
    public static final Map<ArsCategory, Map<String, ArsRoom>> ROOMS = new HashMap<>();

    public static final ArsRoom HALLWAY_TECH_SHORT = register("hallway_tech_short", "title." + DWM.MODID + ".ars.room.hallway_tech_short", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_MEDIYM = register("hallway_tech_medium", "title." + DWM.MODID + ".ars.room.hallway_tech_medium", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_LONG = register("hallway_tech_long", "title." + DWM.MODID + ".ars.room.hallway_tech_long", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_JUNCTION_X = register("hallway_tech_junction_x", "title." + DWM.MODID + ".ars.room.hallway_tech_junction_x", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_JUNCTION_T = register("hallway_tech_junction_t", "title." + DWM.MODID + ".ars.room.hallway_tech_junction_t", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_JUNCTION_T_LEFT = register("hallway_tech_junction_t_left", "title." + DWM.MODID + ".ars.room.hallway_tech_junction_t_left", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_JUNCTION_T_RIGHT = register("hallway_tech_junction_t_right", "title." + DWM.MODID + ".ars.room.hallway_tech_junction_t_right", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_TURN_LEFT_SHORT = register("hallway_tech_turn_left_short", "title." + DWM.MODID + ".ars.room.hallway_tech_turn_left_short", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_TURN_RIGHT_SHORT = register("hallway_tech_turn_right_short", "title." + DWM.MODID + ".ars.room.hallway_tech_turn_right_short", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_TURN_LEFT_LONG = register("hallway_tech_turn_left_long", "title." + DWM.MODID + ".ars.room.hallway_tech_turn_left_long", ArsCategories.HALLWAYS_TECH);
    public static final ArsRoom HALLWAY_TECH_TURN_RIGHT_LONG = register("hallway_tech_turn_right_long", "title." + DWM.MODID + ".ars.room.hallway_tech_turn_right_long", ArsCategories.HALLWAYS_TECH);

    public static final ArsRoom ROOM_TECH_SMALL = register("room_tech_small", "title." + DWM.MODID + ".ars.room.room_tech_small", ArsCategories.ROOMS_TECH);
    public static final ArsRoom ROOM_TECH_MEDIUM = register("room_tech_medium", "title." + DWM.MODID + ".ars.room.room_tech_medium", ArsCategories.ROOMS_TECH);
    public static final ArsRoom ROOM_TECH_LARGE = register("room_tech_large", "title." + DWM.MODID + ".ars.room.room_tech_large", ArsCategories.ROOMS_TECH);
    public static final ArsRoom ROOM_TECH_HUGE = register("room_tech_huge", "title." + DWM.MODID + ".ars.room.room_tech_huge", ArsCategories.ROOMS_TECH);

    public static final ArsRoom ROOM_TECH_STORAGE = register("room_tech_storage", "title." + DWM.MODID + ".ars.room.room_tech_storage", ArsCategories.ROOMS_TECH);

    public static void init() {
    }

    public static ArsRoom register(String path, String title, ArsCategory category) {
        if (!ArsRooms.ROOMS.containsKey(category)) ArsRooms.ROOMS.put(category, new HashMap<>());

        ArsRoom room = new ArsRoom(path, title, category);
        ArsRooms.ROOMS.get(category).put(path, room);
        return room;
    }
}

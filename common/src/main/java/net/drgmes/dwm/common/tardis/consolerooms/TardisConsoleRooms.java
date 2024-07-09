package net.drgmes.dwm.common.tardis.consolerooms;

import net.minecraft.util.math.BlockPos;

import java.util.*;

public class TardisConsoleRooms {
    public static final Map<String, TardisConsoleRoomEntry> CONSOLE_ROOMS = new LinkedHashMap<>();

    public static final TardisConsoleRoomEntry DEFAULT = new TardisConsoleRoomEntry(
        "imperial_classic",
        "title.dwm.console_room.imperial_classic",
        "dwm:tardis/console_rooms/imperial_classic",
        100,
        new BlockPos(7, 1, 7),
        new BlockPos(7, 1, 1)
    )
        .setHidden(true)
        .setDoorsBlock("dwm:tardis_doors_capsule")
        .setDecoratorBlock("minecraft:chiseled_quartz_block")
        .setTeleporterRoom("imperial_classic");

    public static final TardisConsoleRoomEntry DEFAULT_ABANDONED = new TardisConsoleRoomEntry(
        "imperial_abandoned",
        "title.dwm.console_room.imperial_abandoned",
        "dwm:tardis/console_rooms/imperial_abandoned",
        100,
        new BlockPos(7, 1, 7),
        new BlockPos(7, 1, 1)
    )
        .setHidden(true)
        .setTeleporterRoom("imperial_classic")
        .setDecoratorBlock("minecraft:chiseled_quartz_block")
        .setRepairTo("imperial_classic");

    public static TardisConsoleRoomEntry getConsoleRoom(String key, boolean isAbandoned) {
        if (key != null && CONSOLE_ROOMS.containsKey(key)) return CONSOLE_ROOMS.get(key);

        List<TardisConsoleRoomEntry> regularRooms = CONSOLE_ROOMS.values().stream().filter((room) -> room.repairTo.equals("")).toList();
        List<TardisConsoleRoomEntry> abandonedRooms = CONSOLE_ROOMS.values().stream().filter((room) -> !room.repairTo.equals("")).toList();
        List<TardisConsoleRoomEntry> rooms = new ArrayList<>();

        for (TardisConsoleRoomEntry room : (isAbandoned ? abandonedRooms : regularRooms)) {
            for (int i = 0; i < Math.max(1, room.spawnChance); i++) {
                rooms.add(room);
            }
        }

        Collections.shuffle(rooms);
        if (rooms.isEmpty()) return isAbandoned ? DEFAULT_ABANDONED : DEFAULT;
        return rooms.get(new Random().nextInt(0, rooms.size()));
    }

    public static TardisConsoleRoomEntry getConsoleRoom(String key) {
        return getConsoleRoom(key, false);
    }
}

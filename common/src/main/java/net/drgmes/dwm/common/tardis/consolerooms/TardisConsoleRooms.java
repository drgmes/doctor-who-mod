package net.drgmes.dwm.common.tardis.consolerooms;

import net.minecraft.util.math.BlockPos;

import java.util.*;

public class TardisConsoleRooms {
    public static final Map<String, TardisConsoleRoomEntry> CONSOLE_ROOMS = new HashMap<>();

    public static final TardisConsoleRoomEntry DEFAULT = new TardisConsoleRoomEntry(
        "imperial_classic",
        "dwm.console_room.imperial_classic",
        new BlockPos(7, 1, 7),
        new BlockPos(7, 1, 1),
        100
    )
        .setHidden(true)
        .setDecoratorBlock("minecraft:chiseled_quartz_block")
        .setTeleporterRoom("imperial_classic");

    public static final TardisConsoleRoomEntry DEFAULT_ABANDONED = new TardisConsoleRoomEntry(
        "imperial_abandoned",
        "dwm.console_room.imperial_abandoned",
        new BlockPos(7, 1, 7),
        new BlockPos(7, 1, 1),
        100
    )
        .setHidden(true)
        .setTeleporterRoom("imperial_classic")
        .setDecoratorBlock("minecraft:chiseled_quartz_block")
        .setRepairTo("imperial_classic");

    public static TardisConsoleRoomEntry getConsoleRoom(String consoleRoomId, boolean isAbandoned) {
        if (consoleRoomId != null && CONSOLE_ROOMS.containsKey(consoleRoomId)) return CONSOLE_ROOMS.get(consoleRoomId);

        List<TardisConsoleRoomEntry> regularRooms = CONSOLE_ROOMS.values().stream().filter((room) -> room.repairTo.equals("")).toList();
        List<TardisConsoleRoomEntry> abandonedRooms = CONSOLE_ROOMS.values().stream().filter((room) -> !room.repairTo.equals("")).toList();
        List<TardisConsoleRoomEntry> rooms = new ArrayList<>();

        for (TardisConsoleRoomEntry room : (isAbandoned ? abandonedRooms : regularRooms)) {
            for (int i = 0; i < Math.max(1, room.spawnChance); i++) {
                rooms.add(room);
            }
        }

        Collections.shuffle(rooms);
        if (rooms.size() == 0) return isAbandoned ? DEFAULT_ABANDONED : DEFAULT;
        return rooms.get(new Random().nextInt(0, rooms.size()));
    }

    public static TardisConsoleRoomEntry getConsoleRoom(String consoleRoomId) {
        return getConsoleRoom(consoleRoomId, false);
    }
}

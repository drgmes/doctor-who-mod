package net.drgmes.dwm.common.tardis.consolerooms;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleRooms {
    public static final Map<String, TardisConsoleRoomEntry> CONSOLE_ROOMS = new HashMap<>();

    public static final TardisConsoleRoomEntry DEFAULT = new TardisConsoleRoomEntry(
        "imperial_classic",
        "Imperial Classic",
        new BlockPos(8, 2, 8),
        new BlockPos(8, 2, 2)
    ).setHidden(true);

    public static final TardisConsoleRoomEntry DEFAULT_ABANDONED = new TardisConsoleRoomEntry(
        "imperial_abandoned",
        "Imperial Abandoned",
        new BlockPos(8, 2, 8),
        new BlockPos(8, 2, 2)
    ).setHidden(true).setRepairTo("imperial_classic");

    public static TardisConsoleRoomEntry getConsoleRoom(String consoleRoomId, boolean isAbandoned) {
        return CONSOLE_ROOMS.getOrDefault(consoleRoomId, isAbandoned ? DEFAULT_ABANDONED : DEFAULT);
    }

    public static TardisConsoleRoomEntry getConsoleRoom(String consoleRoomId) {
        return getConsoleRoom(consoleRoomId, false);
    }
}

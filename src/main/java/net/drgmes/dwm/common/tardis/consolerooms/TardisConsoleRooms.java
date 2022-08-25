package net.drgmes.dwm.common.tardis.consolerooms;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleRooms {
    public static final Map<String, TardisConsoleRoomEntry> CONSOLE_ROOMS = new HashMap<>();

    public static final TardisConsoleRoomEntry DEFAULT = new TardisConsoleRoomEntry(
        "imperial_abandoned",
        "Imperial Abandoned",
        new BlockPos(8, 2, 8),
        new BlockPos(8, 2, 2)
    ).setHidden(true).setRepairTo("imperial_classic");

    public static TardisConsoleRoomEntry getConsoleRoom(String consoleRoomId) {
        return CONSOLE_ROOMS.getOrDefault(consoleRoomId, DEFAULT);
    }
}

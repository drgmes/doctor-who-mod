package net.drgmes.dwm.common.tardis.consolerooms;

import net.minecraft.util.math.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleRooms {
    public static final Map<String, TardisConsoleRoomEntry> CONSOLE_ROOMS = new HashMap<>();

    public static final TardisConsoleRoomEntry DEFAULT = new TardisConsoleRoomEntry(
        "imperial_classic",
        "dwm.console_room.imperial_classic",
        new BlockPos(7, 1, 7),
        new BlockPos(7, 1, 1)
    )
        .setHidden(true)
        .setDecoratorBlock("minecraft:chiseled_quartz_block")
        .setTeleporterRoom("imperial_classic");

    public static final TardisConsoleRoomEntry DEFAULT_ABANDONED = new TardisConsoleRoomEntry(
        "imperial_abandoned",
        "dwm.console_room.imperial_abandoned",
        new BlockPos(7, 1, 7),
        new BlockPos(7, 1, 1)
    )
        .setHidden(true)
        .setTeleporterRoom("imperial_classic")
        .setDecoratorBlock("minecraft:chiseled_quartz_block")
        .setRepairTo("imperial_classic");

    public static TardisConsoleRoomEntry getConsoleRoom(String consoleRoomId, boolean isAbandoned) {
        return CONSOLE_ROOMS.getOrDefault(consoleRoomId, isAbandoned ? DEFAULT_ABANDONED : DEFAULT);
    }

    public static TardisConsoleRoomEntry getConsoleRoom(String consoleRoomId) {
        return getConsoleRoom(consoleRoomId, false);
    }
}

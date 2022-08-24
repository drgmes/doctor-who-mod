package net.drgmes.dwm.common.tardis.consolerooms;

import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleRooms {
    public static final Map<String, TardisConsoleRoomEntry> CONSOLE_ROOMS = new HashMap<>();

    public static final TardisConsoleRoomEntry DEFAULT = new TardisConsoleRoomEntry(
        "imperial_classic",
        "Imperial Classic",
        new BlockPos(7, 1, 7),
        new BlockPos(7, 1, 1),
        Registry.BLOCK.getId(Blocks.CHISELED_QUARTZ_BLOCK).toString(),
        "https://i.imgur.com/nhFyIZn.png",
        true
    );

    public static TardisConsoleRoomEntry getConsoleRoom(String consoleRoomId) {
        return CONSOLE_ROOMS.getOrDefault(consoleRoomId, DEFAULT);
    }
}

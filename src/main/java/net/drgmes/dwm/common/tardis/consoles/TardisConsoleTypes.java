package net.drgmes.dwm.common.tardis.consoles;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleTypes {
    public static final Map<String, TardisConsoleTypeEntry> CONSOLE_TYPES = new HashMap<>();

    public static final TardisConsoleTypeEntry IMPERIAL = new TardisConsoleTypeEntryImperial("imperial");
    public static final TardisConsoleTypeEntry TOYOTA = new TardisConsoleTypeEntryToyota("toyota");
}

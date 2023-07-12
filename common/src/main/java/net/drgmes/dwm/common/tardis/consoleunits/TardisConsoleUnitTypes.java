package net.drgmes.dwm.common.tardis.consoleunits;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleUnitTypes {
    public static final Map<String, TardisConsoleUnitTypeEntry> CONSOLE_TYPES = new HashMap<>();

    public static final TardisConsoleUnitTypeEntry IMPERIAL = new TardisConsoleUnitTypeEntryImperial();
    public static final TardisConsoleUnitTypeEntry TOYOTA = new TardisConsoleUnitTypeEntryToyota();
}

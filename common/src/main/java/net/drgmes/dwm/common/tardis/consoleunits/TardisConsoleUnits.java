package net.drgmes.dwm.common.tardis.consoleunits;

import net.drgmes.dwm.common.tardis.consoleunits.types.TardisConsoleUnitImperial;
import net.drgmes.dwm.common.tardis.consoleunits.types.TardisConsoleUnitToyota;

import java.util.HashMap;
import java.util.Map;

public class TardisConsoleUnits {
    public static final Map<String, TardisConsoleUnitEntry> TYPES = new HashMap<>();

    public static final TardisConsoleUnitEntry IMPERIAL = new TardisConsoleUnitImperial();
    public static final TardisConsoleUnitEntry TOYOTA = new TardisConsoleUnitToyota();
}

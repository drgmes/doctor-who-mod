package net.drgmes.dwm.common.tardis.exteriors;

import net.drgmes.dwm.common.tardis.doors.TardisDoors;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class TardisExteriors {
    public static final Map<String, TardisExteriorEntry> TYPES = new HashMap<>();

    public static final TardisExteriorEntry CAPSULE = new TardisExteriorEntry(
        "capsule",
        TardisDoors.CAPSULE,
        () -> ModBlocks.TARDIS_EXTERIOR_CAPSULE,
        () -> ModBlockEntities.TARDIS_EXTERIOR_CAPSULE,
        1.0F, 2.0F
    );

    public static final TardisExteriorEntry POLICE_BOX = new TardisExteriorEntry(
        "police_box",
        TardisDoors.POLICE_BOX,
        () -> ModBlocks.TARDIS_EXTERIOR_POLICE_BOX,
        () -> ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX,
        1.0F, 2.0F
    );

    public static final TardisExteriorEntry PHONE_BOX = new TardisExteriorEntry(
        "phone_box",
        TardisDoors.PHONE_BOX,
        () -> ModBlocks.TARDIS_EXTERIOR_PHONE_BOX,
        () -> ModBlockEntities.TARDIS_EXTERIOR_PHONE_BOX,
        1.0F, 2.0F
    );

    public static TardisExteriorEntry getExteriorType(String key) {
        return TYPES.getOrDefault(key, CAPSULE);
    }
}

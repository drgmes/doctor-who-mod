package net.drgmes.dwm.common.tardis.exteriors;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class TardisExteriorTypes {
    public static final Map<String, TardisExteriorTypeEntry> EXTERIOR_TYPES = new HashMap<>();

    public static final TardisExteriorTypeEntry CAPSULE = new TardisExteriorTypeEntry(
        "capsule",
        1.0F, 2.0F,
        () -> ModBlocks.TARDIS_EXTERIOR_CAPSULE,
        () -> ModBlockEntities.TARDIS_EXTERIOR_CAPSULE
    );

    public static final TardisExteriorTypeEntry POLICE_BOX = new TardisExteriorTypeEntry(
        "police_box",
        1.0F, 2.0F,
        () -> ModBlocks.TARDIS_EXTERIOR_POLICE_BOX,
        () -> ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX
    );

    public static final TardisExteriorTypeEntry PHONE_BOX = new TardisExteriorTypeEntry(
        "phone_box",
        1.0F, 2.0F,
        () -> ModBlocks.TARDIS_EXTERIOR_PHONE_BOX,
        () -> ModBlockEntities.TARDIS_EXTERIOR_PHONE_BOX
    );
}

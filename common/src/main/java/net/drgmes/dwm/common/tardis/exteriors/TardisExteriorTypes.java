package net.drgmes.dwm.common.tardis.exteriors;

import net.drgmes.dwm.common.tardis.doors.TardisDoorsTypes;
import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class TardisExteriorTypes {
    public static final Map<String, TardisExteriorTypeEntry> TYPES = new HashMap<>();

    public static final TardisExteriorTypeEntry CAPSULE = new TardisExteriorTypeEntry(
        "capsule",
        TardisDoorsTypes.CAPSULE,
        () -> ModBlocks.TARDIS_EXTERIOR_CAPSULE,
        () -> ModBlockEntities.TARDIS_EXTERIOR_CAPSULE,
        1.0F, 2.0F
    );

    public static final TardisExteriorTypeEntry POLICE_BOX = new TardisExteriorTypeEntry(
        "police_box",
        TardisDoorsTypes.POLICE_BOX,
        () -> ModBlocks.TARDIS_EXTERIOR_POLICE_BOX,
        () -> ModBlockEntities.TARDIS_EXTERIOR_POLICE_BOX,
        1.0F, 2.0F
    );

    public static final TardisExteriorTypeEntry PHONE_BOX = new TardisExteriorTypeEntry(
        "phone_box",
        TardisDoorsTypes.PHONE_BOX,
        () -> ModBlocks.TARDIS_EXTERIOR_PHONE_BOX,
        () -> ModBlockEntities.TARDIS_EXTERIOR_PHONE_BOX,
        1.0F, 2.0F
    );
}

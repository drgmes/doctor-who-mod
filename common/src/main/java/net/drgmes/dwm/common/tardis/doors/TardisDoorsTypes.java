package net.drgmes.dwm.common.tardis.doors;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class TardisDoorsTypes {
    public static final Map<String, TardisDoorsTypeEntry> TYPES = new HashMap<>();

    public static final TardisDoorsTypeEntry CAPSULE = new TardisDoorsTypeEntry(
        "capsule",
        () -> ModBlocks.TARDIS_DOORS_CAPSULE,
        () -> ModBlockEntities.TARDIS_DOORS_CAPSULE,
        1.0F, 2.0F
    );

    public static final TardisDoorsTypeEntry POLICE_BOX = new TardisDoorsTypeEntry(
        "police_box",
        () -> ModBlocks.TARDIS_DOORS_POLICE_BOX,
        () -> ModBlockEntities.TARDIS_DOORS_POLICE_BOX,
        1.0F, 2.0F
    );

    public static final TardisDoorsTypeEntry PHONE_BOX = new TardisDoorsTypeEntry(
        "phone_box",
        () -> ModBlocks.TARDIS_DOORS_PHONE_BOX,
        () -> ModBlockEntities.TARDIS_DOORS_PHONE_BOX,
        1.0F, 2.0F
    );
}

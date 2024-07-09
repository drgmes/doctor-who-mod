package net.drgmes.dwm.common.tardis.doors;

import net.drgmes.dwm.setup.ModBlockEntities;
import net.drgmes.dwm.setup.ModBlocks;

import java.util.HashMap;
import java.util.Map;

public class TardisDoors {
    public static final Map<String, TardisDoorsEntry> TYPES = new HashMap<>();

    public static final TardisDoorsEntry CAPSULE = new TardisDoorsEntry(
        "capsule",
        () -> ModBlocks.TARDIS_DOORS_CAPSULE,
        () -> ModBlockEntities.TARDIS_DOORS_CAPSULE,
        1.0F, 2.0F
    );

    public static final TardisDoorsEntry POLICE_BOX = new TardisDoorsEntry(
        "police_box",
        () -> ModBlocks.TARDIS_DOORS_POLICE_BOX,
        () -> ModBlockEntities.TARDIS_DOORS_POLICE_BOX,
        1.0F, 2.0F
    );

    public static final TardisDoorsEntry PHONE_BOX = new TardisDoorsEntry(
        "phone_box",
        () -> ModBlocks.TARDIS_DOORS_PHONE_BOX,
        () -> ModBlockEntities.TARDIS_DOORS_PHONE_BOX,
        1.0F, 2.0F
    );

    public static TardisDoorsEntry getDoorsType(String key) {
        return TYPES.getOrDefault(key, CAPSULE);
    }
}

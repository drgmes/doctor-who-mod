package net.drgmes.dwm.common.tardis.fabric;

import net.drgmes.dwm.setup.ModCompats;

public class TardisEnergyManagerImpl {
    public static boolean hasEnergyApi() {
        return ModCompats.techReborn();
    }
}

package net.drgmes.dwm.fabric;

import net.drgmes.dwm.setup.ModCompats;

public class DWMImpl {
    public static boolean hasEnergyApi() {
        return ModCompats.techReborn();
    }
}

package net.drgmes.dwm;

import net.drgmes.dwm.setup.Registration;
import net.fabricmc.api.ModInitializer;

public class DWMCommon implements ModInitializer {
    @Override
    public void onInitialize() {
        Registration.setupCommon();
    }
}

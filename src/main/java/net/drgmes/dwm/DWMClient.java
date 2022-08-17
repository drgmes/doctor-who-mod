package net.drgmes.dwm;

import net.drgmes.dwm.setup.Registration;
import net.fabricmc.api.ClientModInitializer;

public class DWMClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Registration.setupClient();
    }
}

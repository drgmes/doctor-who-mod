package net.drgmes.dwm;

import net.drgmes.dwm.setup.Registration;
import net.fabricmc.api.DedicatedServerModInitializer;

public class DWMServer implements DedicatedServerModInitializer {
    @Override
    public void onInitializeServer() {
        Registration.setupServer();
    }
}

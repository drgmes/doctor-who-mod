package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.engines.screens.TardisEngineSystemsScreen;
import net.minecraft.client.gui.screens.MenuScreens;

public class ModContainerScreens {
    public static void setup() {
        MenuScreens.register(ModContainers.TARDIS_ENGINE.get(), TardisEngineSystemsScreen::new);
    }
}

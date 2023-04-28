package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.engines.screens.TardisEngineSystemsScreen;
import net.minecraft.client.gui.screen.ingame.HandledScreens;

public class ModScreens {
    public static void setup() {
        HandledScreens.register(ModInventories.TARDIS_ENGINE.get(), TardisEngineSystemsScreen::new);
    }
}

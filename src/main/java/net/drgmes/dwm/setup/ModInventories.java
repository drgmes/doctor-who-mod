package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.engines.screens.TardisEngineSystemsScreen;
import net.drgmes.dwm.blocks.tardis.engines.screens.handlers.TardisEngineSystemsScreenHandler;
import net.minecraft.client.gui.screen.ingame.HandledScreens;
import net.minecraft.screen.ScreenHandlerType;

public class ModInventories {
    public static final ScreenHandlerType<TardisEngineSystemsScreenHandler> TARDIS_ENGINE = Registration.registerScreenHandler("tardis_engine", TardisEngineSystemsScreenHandler::new);

    public static void init() {
    }

    public static void setup() {
        HandledScreens.register(TARDIS_ENGINE, TardisEngineSystemsScreen::new);
    }
}

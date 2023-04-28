package net.drgmes.dwm.setup;

import net.drgmes.dwm.blocks.tardis.engines.screens.handlers.TardisEngineSystemsScreenHandler;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.screen.ScreenHandlerType;

import java.util.function.Supplier;

public class ModInventories {
    public static final Supplier<ScreenHandlerType<TardisEngineSystemsScreenHandler>> TARDIS_ENGINE = Registration.registerScreenHandler("tardis_engine", () -> new ScreenHandlerType<>(TardisEngineSystemsScreenHandler::new, FeatureSet.empty()));

    public static void init() {
    }
}

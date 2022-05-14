package net.drgmes.dwm.setup;

import com.mojang.blaze3d.platform.InputConstants;

import net.drgmes.dwm.DWM;
import net.minecraft.client.KeyMapping;

public class ModKeys {
    public static final String CATEGORY_MAIN = "key." + DWM.MODID + ".categories.main";

    public static KeyMapping SCREWDRIVER_SETTINGS;

    public static void init() {
    }

    public static void setup() {
        SCREWDRIVER_SETTINGS = Registration.registerKeyBinding("screwdriver_settings_key", CATEGORY_MAIN, InputConstants.KEY_O);
    }
}

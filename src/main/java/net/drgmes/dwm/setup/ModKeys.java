package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class ModKeys {
    public static final String CATEGORY_MAIN = "key." + DWM.MODID + ".categories.main";

    public static KeyBinding SCREWDRIVER_SETTINGS;

    public static void init() {
    }

    public static void setup() {
        SCREWDRIVER_SETTINGS = Registration.registerKeyBinding("screwdriver_settings_key", CATEGORY_MAIN, GLFW.GLFW_KEY_O);
    }
}

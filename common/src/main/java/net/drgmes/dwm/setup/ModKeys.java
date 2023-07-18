package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class ModKeys {
    public static final String CATEGORY_MAIN = "key." + DWM.MODID + ".categories.main";

    public static KeyBinding SONIC_DEVICE_SETTINGS;
    public static KeyBinding SONIC_SUNGLASSES_USAGE;

    public static void init() {
    }

    public static void setup() {
        SONIC_DEVICE_SETTINGS = Registration.registerKeyBinding("sonic_device_settings_key", CATEGORY_MAIN, GLFW.GLFW_KEY_O);
        SONIC_SUNGLASSES_USAGE = Registration.registerKeyBinding("sonic_sunglasses_usage_key", CATEGORY_MAIN, GLFW.GLFW_KEY_R);
    }
}

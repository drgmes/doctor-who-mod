package net.drgmes.dwm.setup;

import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import net.minecraft.client.option.KeyBinding;
import org.lwjgl.glfw.GLFW;

public class ModKeyBindings {
    public static final String CATEGORY_MAIN = "key.dwm.categories.main";

    public static KeyBinding SONIC_DEVICE_SETTINGS;
    public static KeyBinding SONIC_SUNGLASSES_USAGE;

    public static void init() {
        SONIC_DEVICE_SETTINGS = createKeyBinding("sonic_device_settings_key", CATEGORY_MAIN, GLFW.GLFW_KEY_O);
        SONIC_SUNGLASSES_USAGE = createKeyBinding("sonic_sunglasses_usage_key", CATEGORY_MAIN, GLFW.GLFW_KEY_R);
    }

    public static void setup() {
        ModKeyBindings.init();

        KeyMappingRegistry.register(ModKeyBindings.SONIC_DEVICE_SETTINGS);
        KeyMappingRegistry.register(ModKeyBindings.SONIC_SUNGLASSES_USAGE);
    }

    private static KeyBinding createKeyBinding(String name, String category, int keycode) {
        return new KeyBinding("key.dwm." + name, keycode, category);
    }
}

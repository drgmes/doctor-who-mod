package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.util.Identifier;

public class RecipeHelper {
    public static Identifier getRecipeName(String name, String category) {
        return DWM.getIdentifier(name + "_" + category);
    }

    public static Identifier getConversionRecipeName(String name, String fromName) {
        return getRecipeName(name, "from_" + fromName);
    }

    public static Identifier getSmeltingRecipeName(String name) {
        return getConversionRecipeName(name, "smelting");
    }

    public static Identifier getSmeltingRecipeName(String name, String fromName) {
        return getConversionRecipeName(name, "smelting_" + fromName);
    }

    public static Identifier getBlastingRecipeName(String name) {
        return getConversionRecipeName(name, "blasting");
    }

    public static Identifier getBlastingRecipeName(String name, String fromName) {
        return getConversionRecipeName(name, "blasting_" + fromName);
    }

    public static Identifier getSmokingRecipeName(String name) {
        return getConversionRecipeName(name, "blasting");
    }

    public static Identifier getSmokingRecipeName(String name, String fromName) {
        return getConversionRecipeName(name, "blasting_" + fromName);
    }
}

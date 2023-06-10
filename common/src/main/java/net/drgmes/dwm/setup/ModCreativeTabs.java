package net.drgmes.dwm.setup;

import dev.architectury.registry.registries.RegistrySupplier;
import net.drgmes.dwm.DWM;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;

public class ModCreativeTabs {
    public static final RegistrySupplier<ItemGroup> GENERAL = Registration.registerItemGroup("general", () -> new ItemStack(ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlock()));
    public static final RegistrySupplier<ItemGroup> DECORATIONS = Registration.registerItemGroup("decorations", () -> new ItemStack(ModBlocks.CARBON_FIBER.getBlockItem()));

    public static void init() {
    }

    private static Text getTabTitle(String title) {
        return Text.translatable(DWM.getIdentifier(title).toTranslationKey());
    }
}

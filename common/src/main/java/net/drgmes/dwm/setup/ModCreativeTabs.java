package net.drgmes.dwm.setup;

import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {
    public static final RegistrySupplier<ItemGroup> GENERAL = Registration.registerItemGroup("general", () -> new ItemStack(ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlock()));

    public static void init() {
    }
}

package net.drgmes.dwm.setup;

import dev.architectury.registry.CreativeTabRegistry;
import net.drgmes.dwm.DWM;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {
    public static final CreativeTabRegistry.TabSupplier GENERAL = CreativeTabRegistry.create(DWM.getIdentifier("general"), () -> new ItemStack(ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlock()));
    public static final CreativeTabRegistry.TabSupplier DECORATIONS = CreativeTabRegistry.create(DWM.getIdentifier("decorations"), () -> new ItemStack(ModBlocks.CARBON_FIBER.getBlockItem()));

    public static void init() {
    }
}

package net.drgmes.dwm.setup;

import net.drgmes.dwm.DWM;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

public class ModCreativeTabs {
    public static final ItemGroup GENERAL = FabricItemGroupBuilder.build(
        DWM.getIdentifier("general"),
        () -> new ItemStack(ModBlocks.TARDIS_EXTERIOR_POLICE_BOX.getBlockItem())
    );

    public static final ItemGroup DECORATIONS = FabricItemGroupBuilder.build(
        DWM.getIdentifier("decorations"),
        () -> new ItemStack(ModBlocks.CARBON_FIBER.getBlockItem())
    );

    public static void init() {
    }
}

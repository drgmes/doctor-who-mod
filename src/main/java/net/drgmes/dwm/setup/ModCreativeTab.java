package net.drgmes.dwm.setup;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.RegistryObject;

import java.util.stream.Collectors;

public class ModCreativeTab extends CreativeModeTab {
    public ModCreativeTab(String label) {
        super(label);
    }

    @Override
    public ItemStack makeIcon() {
        return ModBlocks.TARDIS_EXTERIOR.blockObject.get().asItem().getDefaultInstance();
    }

    @Override
    public void fillItemList(NonNullList<ItemStack> itemStacks) {
        for (Item item : Registration.ITEMS.getEntries().stream().map(RegistryObject::get).collect(Collectors.toList())) {
            item.fillItemCategory(this, itemStacks);
        }
    }
}

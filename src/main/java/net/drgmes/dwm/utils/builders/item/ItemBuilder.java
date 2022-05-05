package net.drgmes.dwm.utils.builders.item;

import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.data.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class ItemBuilder {
    public final String name;
    public final RegistryObject<? extends Item> itemObject;

    public ItemBuilder(String name, Supplier<? extends Item> factory) {
        this.name = name;
        this.itemObject = Registration.registerItem(name, factory);

        ModItems.ITEM_BUILDERS.add(this);
    }

    public ItemBuilder(String name) {
        this(name, () -> new Item(getItemProperties()));
    }

    public static Item.Properties getItemProperties() {
        return new Item.Properties().tab(Registration.CREATIVE_MODE_TAB);
    }

    public Item get() {
        return this.itemObject.get();
    }

    public void registerItemModel(ModItemModelProvider provider) {
        ModelFile itemGenerated = provider.getExistingFile(provider.mcLoc("item/generated"));
        provider.getBuilder(this.name).parent(itemGenerated).texture("layer0", "item/" + this.name);
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<FinishedRecipe> consumer) {
    }
}

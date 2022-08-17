package net.drgmes.dwm.utils.builders.item;

import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ItemBuilder {
    public final String name;
    public final ArrayList<TagKey<Item>> tags = new ArrayList<>();

    private final Item item;

    public ItemBuilder(String name, Item item) {
        this.name = name;
        this.item = Registration.registerItem(name, item);

        this.registerTags();
        ModItems.ITEM_BUILDERS.add(this);
    }

    public static Item.Settings getItemSettings() {
        return new FabricItemSettings().group(ModCreativeTabs.GENERAL);
    }

    public String getName() {
        return this.name;
    }

    public Item getItem() {
        return this.item;
    }

    public void registerCustomRender() {
    }

    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
    }

    public void registerTags() {
    }
}

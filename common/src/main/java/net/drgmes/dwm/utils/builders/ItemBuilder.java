package net.drgmes.dwm.utils.builders;

import dev.architectury.registry.registries.DeferredSupplier;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.data.server.recipe.RecipeExporter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class ItemBuilder {
    public final List<TagKey<Item>> tags = new ArrayList<>();
    public final Supplier<Item> item;

    private final String name;

    public ItemBuilder(String name, Supplier<Item> itemSupplier) {
        this.name = name;
        this.item = Registration.registerItem(name, itemSupplier);

        this.registerTags();
        ModItems.ITEM_BUILDERS.add(this);
    }

    public ItemBuilder(String name, DeferredSupplier<ItemGroup> tabSupplier) {
        this(name, () -> new Item(getItemSettings(tabSupplier)));
    }

    public ItemBuilder(String name) {
        this(name, () -> new Item(getItemSettings()));
    }

    public static Item.Settings getItemSettings(DeferredSupplier<ItemGroup> tabSupplier) {
        return new Item.Settings().arch$tab(tabSupplier);
    }

    public static Item.Settings getItemSettings() {
        return getItemSettings(ModCreativeTabs.GENERAL);
    }

    public Identifier getId() {
        return DWM.getIdentifier("item/" + this.getName());
    }

    public Identifier getTexture() {
        return this.getId();
    }

    public String getName() {
        return this.name;
    }

    public Item getItem() {
        return this.item.get();
    }

    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getItem(), this.getId(), this.getTexture(), ItemModelDataBuilder.ItemType.GENERATED);
    }

    public void registerRecipe(RecipeExporter exporter) {
    }

    public void registerTags() {
    }
}

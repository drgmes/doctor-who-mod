package net.drgmes.dwm.utils.builders;

import dev.architectury.registry.CreativeTabRegistry;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
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

    public ItemBuilder(String name, CreativeTabRegistry.TabSupplier tab) {
        this(name, () -> new Item(getItemSettings(tab)));
    }

    public ItemBuilder(String name) {
        this(name, () -> new Item(getItemSettings()));
    }

    public static Item.Settings getItemSettings(CreativeTabRegistry.TabSupplier tab) {
        return new Item.Settings().arch$tab(tab);
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

    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
    }

    public void registerTags() {
    }
}

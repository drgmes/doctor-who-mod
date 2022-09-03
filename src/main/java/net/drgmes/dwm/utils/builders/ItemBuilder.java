package net.drgmes.dwm.utils.builders;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.common.ModRecipeProvider;
import net.drgmes.dwm.mixin.IMixinItemModelGenerator;
import net.drgmes.dwm.setup.ModCreativeTabs;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;

import java.util.ArrayList;
import java.util.function.Consumer;

public class ItemBuilder {
    public final ArrayList<TagKey<Item>> tags = new ArrayList<>();

    private final String name;
    private final Item item;

    public ItemBuilder(String name, Item item) {
        this.name = name;
        this.item = Registration.registerItem(name, item);

        this.registerTags();
        ModItems.ITEM_BUILDERS.add(this);
    }

    public ItemBuilder(String name) {
        this(name, new Item(getItemSettings()));
    }

    public static Item.Settings getItemSettings() {
        return new FabricItemSettings().group(ModCreativeTabs.GENERAL);
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
        return this.item;
    }

    public void registerCustomRender() {
    }

    public void registerItemModel(ItemModelGenerator itemModelGenerator) {
        Models.GENERATED.upload(this.getId(), TextureMap.layer0(this.getTexture()), ((IMixinItemModelGenerator) itemModelGenerator).getWriter());
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<RecipeJsonProvider> exporter) {
    }

    public void registerTags() {
    }
}

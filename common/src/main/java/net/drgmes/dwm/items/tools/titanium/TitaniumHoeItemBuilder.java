package net.drgmes.dwm.items.tools.titanium;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.setup.ModMaterials;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Items;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumHoeItemBuilder extends ItemBuilder {
    public TitaniumHoeItemBuilder(String name) {
        super(name, () -> new HoeItem(ModMaterials.TOOL.TITANIUM, -2, -1.0F, getItemSettings()));
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("item/tools/" + this.getName());
    }

    @Override
    public ItemModelDataBuilder getItemModelDataBuilder() {
        return new ItemModelDataBuilder(this.getItem(), this.getId(), this.getTexture(), ItemModelDataBuilder.ItemType.HANDHELD);
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.TOOLS, this.getItem())
            .input('#', ModItems.TITANIUM_INGOT.getItem())
            .input('0', Items.STICK)
            .pattern("##")
            .pattern(" 0")
            .pattern(" 0")
            .criterion("has_titanium_ingot", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModItems.TITANIUM_INGOT.getName()));
    }
}

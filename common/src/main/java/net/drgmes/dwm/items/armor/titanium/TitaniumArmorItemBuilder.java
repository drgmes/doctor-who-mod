package net.drgmes.dwm.items.armor.titanium;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.setup.ModMaterials;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.data.server.recipe.ShapedRecipeJsonBuilder;
import net.minecraft.item.ArmorItem;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class TitaniumArmorItemBuilder extends ItemBuilder {
    public TitaniumArmorItemBuilder(String name, ArmorItem.Type type) {
        super(name, () -> new ArmorItem(ModMaterials.ARMOR.TITANIUM, type, getItemSettings()));
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("item/armor/" + this.getName());
    }

    @Override
    public void registerRecipe(Consumer<RecipeJsonProvider> exporter) {
        switch (((ArmorItem) this.getItem()).getType()) {
            case HELMET -> ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, this.getItem())
                .input('#', ModItems.TITANIUM_INGOT.getItem())
                .pattern("###")
                .pattern("# #")
                .criterion("has_titanium_ingot", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
                .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModItems.TITANIUM_INGOT.getName()));

            case CHESTPLATE -> ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, this.getItem())
                .input('#', ModItems.TITANIUM_INGOT.getItem())
                .pattern("# #")
                .pattern("###")
                .pattern("###")
                .criterion("has_titanium_ingot", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
                .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModItems.TITANIUM_INGOT.getName()));

            case LEGGINGS -> ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, this.getItem())
                .input('#', ModItems.TITANIUM_INGOT.getItem())
                .pattern("###")
                .pattern("# #")
                .pattern("# #")
                .criterion("has_titanium_ingot", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
                .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModItems.TITANIUM_INGOT.getName()));

            case BOOTS -> ShapedRecipeJsonBuilder.create(RecipeCategory.COMBAT, this.getItem())
                .input('#', ModItems.TITANIUM_INGOT.getItem())
                .pattern("# #")
                .pattern("# #")
                .criterion("has_titanium_ingot", RecipeHelper.conditionsFromItem(ModItems.TITANIUM_INGOT.getItem()))
                .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModItems.TITANIUM_INGOT.getName()));
        }
    }
}

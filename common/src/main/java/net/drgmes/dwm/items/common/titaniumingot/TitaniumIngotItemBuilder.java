package net.drgmes.dwm.items.common.titaniumingot;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.drgmes.dwm.utils.helpers.RecipeHelper;
import net.minecraft.data.server.recipe.*;
import net.minecraft.recipe.Ingredient;
import net.minecraft.recipe.book.RecipeCategory;
import net.minecraft.util.Identifier;

public class TitaniumIngotItemBuilder extends ItemBuilder {
    public TitaniumIngotItemBuilder(String name) {
        super(name);
    }

    @Override
    public Identifier getTexture() {
        return DWM.getIdentifier("item/common/" + this.getName());
    }

    @Override
    public void registerRecipe(RecipeExporter exporter) {
        ShapedRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem())
            .input('#', ModItems.TITANIUM_NUGGET.getItem())
            .pattern("###")
            .pattern("###")
            .pattern("###")
            .criterion("has_titanium_nugget", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_NUGGET.getItem()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModItems.TITANIUM_NUGGET.getName()));

        ShapelessRecipeJsonBuilder.create(RecipeCategory.MISC, this.getItem(), 9)
            .input(ModBlocks.TITANIUM_BLOCK.getBlock())
            .criterion("has_titanium_block", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_BLOCK.getBlock()))
            .offerTo(exporter, RecipeHelper.getConversionRecipeName(this.getName(), ModBlocks.TITANIUM_BLOCK.getName()));

        CookingRecipeJsonBuilder
            .createSmelting(Ingredient.ofItems(ModBlocks.TITANIUM_ORE.getBlock()), RecipeCategory.MISC, this.getItem(), 0.7f, 200)
            .criterion("has_titanium_ore", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_ORE.getBlock()))
            .offerTo(exporter, RecipeHelper.getSmeltingRecipeName(this.getName(), ModBlocks.TITANIUM_ORE.getName()));

        CookingRecipeJsonBuilder
            .createBlasting(Ingredient.ofItems(ModBlocks.TITANIUM_ORE.getBlock()), RecipeCategory.MISC, this.getItem(), 0.7f, 100)
            .criterion("has_titanium_ore", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_ORE.getBlock()))
            .offerTo(exporter, RecipeHelper.getBlastingRecipeName(this.getName(), ModBlocks.TITANIUM_ORE.getName()));

        CookingRecipeJsonBuilder
            .createSmelting(Ingredient.ofItems(ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()), RecipeCategory.MISC, this.getItem(), 0.7f, 200)
            .criterion("has_titanium_ore_deepslate", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()))
            .offerTo(exporter, RecipeHelper.getSmeltingRecipeName(this.getName(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getName()));

        CookingRecipeJsonBuilder
            .createBlasting(Ingredient.ofItems(ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()), RecipeCategory.MISC, this.getItem(), 0.7f, 100)
            .criterion("has_titanium_ore_deepslate", RecipeProvider.conditionsFromItem(ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()))
            .offerTo(exporter, RecipeHelper.getBlastingRecipeName(this.getName(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getName()));

        CookingRecipeJsonBuilder
            .createSmelting(Ingredient.ofItems(ModItems.TITANIUM_RAW.getItem()), RecipeCategory.MISC, this.getItem(), 0.7f, 200)
            .criterion("has_titanium_raw_item", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_RAW.getItem()))
            .offerTo(exporter, RecipeHelper.getSmeltingRecipeName(this.getName(), ModItems.TITANIUM_RAW.getName()));

        CookingRecipeJsonBuilder
            .createBlasting(Ingredient.ofItems(ModItems.TITANIUM_RAW.getItem()), RecipeCategory.MISC, this.getItem(), 0.7f, 100)
            .criterion("has_titanium_raw_item", RecipeProvider.conditionsFromItem(ModItems.TITANIUM_RAW.getItem()))
            .offerTo(exporter, RecipeHelper.getBlastingRecipeName(this.getName(), ModItems.TITANIUM_RAW.getName()));
    }
}

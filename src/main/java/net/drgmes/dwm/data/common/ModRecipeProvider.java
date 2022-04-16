package net.drgmes.dwm.data.common;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.builders.item.ItemBuilder;
import net.minecraft.advancements.critereon.InventoryChangeTrigger;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected void buildCraftingRecipes(Consumer<FinishedRecipe> consumer) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerRecipe(this, consumer);
        }

        for (ItemBuilder itemBuilder : ModItems.ITEM_BUILDERS) {
            itemBuilder.registerRecipe(this, consumer);
        }
    }

    public InventoryChangeTrigger.TriggerInstance hasItem(ItemLike item) {
        return RecipeProvider.has(item);
    }

    public ResourceLocation getRecipeName(String name, String category) {
        return new ResourceLocation(DWM.MODID, name + category);
    }

    public ResourceLocation getConversionRecipeName(String name, String fromName) {
        return this.getRecipeName(name, "_from_" + fromName);
    }

    public ResourceLocation getSmeltingRecipeName(String name) {
        return this.getRecipeName(name, "_from_smelting");
    }

    public ResourceLocation getBlastingRecipeName(String name) {
        return this.getRecipeName(name, "_from_blasting");
    }
}

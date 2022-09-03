package net.drgmes.dwm.datagen.common;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricRecipeProvider;
import net.minecraft.data.server.recipe.RecipeJsonProvider;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ModRecipeProvider extends FabricRecipeProvider {
    public ModRecipeProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator);
    }

    public Identifier getRecipeName(String name, String category) {
        return DWM.getIdentifier(name + "_" + category);
    }

    public Identifier getConversionRecipeName(String name, String fromName) {
        return this.getRecipeName(name, "from_" + fromName);
    }

    public Identifier getSmeltingRecipeName(String name) {
        return this.getConversionRecipeName(name, "smelting");
    }

    public Identifier getSmeltingRecipeName(String name, String fromName) {
        return this.getConversionRecipeName(name, "smelting_" + fromName);
    }

    public Identifier getBlastingRecipeName(String name) {
        return this.getConversionRecipeName(name, "blasting");
    }

    public Identifier getBlastingRecipeName(String name, String fromName) {
        return this.getConversionRecipeName(name, "blasting_" + fromName);
    }

    public Identifier getSmokingRecipeName(String name) {
        return this.getConversionRecipeName(name, "blasting");
    }

    public Identifier getSmokingRecipeName(String name, String fromName) {
        return this.getConversionRecipeName(name, "blasting_" + fromName);
    }

    @Override
    protected void generateRecipes(Consumer<RecipeJsonProvider> exporter) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerRecipe(this, exporter);
        }

        for (ItemBuilder itemBuilder : ModItems.ITEM_BUILDERS) {
            itemBuilder.registerRecipe(this, exporter);
        }
    }
}

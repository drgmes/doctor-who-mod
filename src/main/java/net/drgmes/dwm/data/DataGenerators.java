package net.drgmes.dwm.data;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.data.common.ModBiomeTagsProvider;
import net.drgmes.dwm.data.common.ModBlockTagsProvider;
import net.drgmes.dwm.data.common.ModLootTableProvider;
import net.drgmes.dwm.data.common.ModRecipeProvider;
import net.minecraft.data.DataGenerator;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.forge.event.lifecycle.GatherDataEvent;

public class DataGenerators {
    public static void setup(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        if (event.includeClient()) {
            generator.addProvider(true, new ModBlockStateProvider(generator, existingFileHelper));
            generator.addProvider(true, new ModItemModelProvider(generator, existingFileHelper));
        }

        if (event.includeServer()) {
            generator.addProvider(true, new ModBiomeTagsProvider(generator, existingFileHelper));
            generator.addProvider(true, new ModBlockTagsProvider(generator, existingFileHelper));
            generator.addProvider(true, new ModLootTableProvider(generator));
            generator.addProvider(true, new ModRecipeProvider(generator));
        }
    }
}

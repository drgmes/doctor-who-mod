package net.drgmes.dwm.datagen;

import net.drgmes.dwm.datagen.client.ModModelProvider;
import net.drgmes.dwm.datagen.common.*;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class DataGenerators implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        dataGenerator.addProvider(ModModelProvider::new);
        dataGenerator.addProvider(ModItemTagsProvider::new);
        dataGenerator.addProvider(ModBlockTagsProvider::new);
        dataGenerator.addProvider(ModLootTableProvider::new);
        dataGenerator.addProvider(ModPoiTypesTagsProvider::new);
        dataGenerator.addProvider(ModRecipeProvider::new);
    }
}

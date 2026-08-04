package net.drgmes.dwm.fabric.datagen;

import net.drgmes.dwm.fabric.datagen.client.ModModelProvider;
import net.drgmes.dwm.fabric.datagen.common.*;
import net.drgmes.dwm.setup.ModWorldGen;
import net.drgmes.dwm.utils.builders.FeatureBuilder;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class DataGenerators implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator dataGenerator) {
        FabricDataGenerator.Pack pack = dataGenerator.createPack();

        pack.addProvider(ModModelProvider::new);
        pack.addProvider(ModItemTagsProvider::new);
        pack.addProvider(ModBlockTagsProvider::new);
        pack.addProvider(ModLootTableProvider::new);
        pack.addProvider(ModPoiTypesTagsProvider::new);
        pack.addProvider(ModRecipeProvider::new);
        pack.addProvider(ModWorldgenProvider::new);
        pack.addProvider(ModArsProvider::new);
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, (registry) -> {
            for (FeatureBuilder featureBuilder : ModWorldGen.FEATURE_BUILDERS) {
                featureBuilder.buildConfigured(registry);
            }
        });

        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, (registry) -> {
            for (FeatureBuilder featureBuilder : ModWorldGen.FEATURE_BUILDERS) {
                featureBuilder.buildPlaced(registry);
            }
        });
    }
}

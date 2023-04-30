package net.drgmes.dwm.fabric.datagen;

import net.drgmes.dwm.fabric.datagen.client.ModModelProvider;
import net.drgmes.dwm.fabric.datagen.common.*;
import net.drgmes.dwm.setup.ModWorldGen;
import net.drgmes.dwm.utils.builders.FeatureBuilder;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;

import java.util.concurrent.atomic.AtomicReference;

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
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        AtomicReference<Registerable<ConfiguredFeature<?, ?>>> configuredFeatureRegistry = new AtomicReference<>();
        AtomicReference<Registerable<PlacedFeature>> placedFeatureRegistry = new AtomicReference<>();

        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, (registry) -> {
            configuredFeatureRegistry.set(registry);
            buildWorldGenData(configuredFeatureRegistry.get(), placedFeatureRegistry.get());
        });

        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, (registry) -> {
            placedFeatureRegistry.set(registry);
            buildWorldGenData(configuredFeatureRegistry.get(), placedFeatureRegistry.get());
        });
    }

    private void buildWorldGenData(Registerable<ConfiguredFeature<?, ?>> configuredFeatureRegistry, Registerable<PlacedFeature> placedFeatureRegistry) {
        if (configuredFeatureRegistry == null || placedFeatureRegistry == null) return;

        for (FeatureBuilder featureBuilder : ModWorldGen.FEATURE_BUILDERS) {
            featureBuilder.build(configuredFeatureRegistry, placedFeatureRegistry);
        }
    }
}

package net.drgmes.dwm.utils.builders;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModWorldGen;
import net.drgmes.dwm.setup.Registration;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectionContext;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryEntry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FeatureBuilder {
    private final String name;
    private final Feature<?> feature;
    private final PlacedFeature placedFeature;
    private final ConfiguredFeature<?, ?> configuredFeature;

    public <T extends FeatureConfig> FeatureBuilder(String name, Feature<T> feature, T featureConfig, GenerationStep.Feature step, Predicate<BiomeSelectionContext> biomeSelector, List<PlacementModifier> placementModifiers) {
        ConfiguredFeature<?, ?> configuredFeature = new ConfiguredFeature<>(feature, featureConfig);
        PlacedFeature placedFeature = new PlacedFeature(RegistryEntry.of(configuredFeature), placementModifiers);

        this.name = name;
        this.feature = feature;
        this.placedFeature = Registration.registerPlacedFeature(this.name, placedFeature);
        this.configuredFeature = Registration.registerConfiguredFeature(this.name, configuredFeature);

        BiomeModifications.addFeature(biomeSelector, step, RegistryKey.of(Registry.PLACED_FEATURE_KEY, this.getId()));
        ModWorldGen.FEATURE_BUILDERS.add(this);
    }

    public static <T extends FeatureConfig> FeatureBuilder create(String name, Supplier<Feature<T>> featureSupplier, T featureConfig, GenerationStep.Feature step, Predicate<BiomeSelectionContext> biomeSelector, List<PlacementModifier> placementModifiers) {
        Feature<T> registeredFeature = Registration.registerFeature(name, featureSupplier.get());
        return new FeatureBuilder(name, registeredFeature, featureConfig, step, biomeSelector, placementModifiers);
    }

    public static FeatureBuilder createOre(String name, int veinSize, Predicate<BiomeSelectionContext> biomeSelector, List<PlacementModifier> placementModifiers, List<OreFeatureConfig.Target> targets) {
        return new FeatureBuilder(name, Feature.ORE, new OreFeatureConfig(targets, veinSize), GenerationStep.Feature.UNDERGROUND_ORES, biomeSelector, placementModifiers);
    }

    public Identifier getId() {
        return DWM.getIdentifier(this.getName());
    }

    public String getName() {
        return this.name;
    }

    public Feature<?> getFeature() {
        return this.feature;
    }

    public PlacedFeature getPlacedFeature() {
        return this.placedFeature;
    }

    public ConfiguredFeature<?, ?> getConfiguredFeature() {
        return this.configuredFeature;
    }
}

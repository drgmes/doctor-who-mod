package net.drgmes.dwm.utils.builders;

import dev.architectury.registry.level.biome.BiomeModifications;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModWorldGen;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class FeatureBuilder {
    private final String name;

    private final RegistryKey<ConfiguredFeature<?, ?>> configuredFeatureKey;
    private final Supplier<ConfiguredFeature<?, ?>> configuredFeatureSupplier;

    private final RegistryKey<PlacedFeature> placedFeatureKey;
    private final Function<RegistryEntry<ConfiguredFeature<?, ?>>, PlacedFeature> placedFeatureBuilder;

    @SuppressWarnings("UnstableApiUsage")
    public <T extends FeatureConfig> FeatureBuilder(String name, GenerationStep.Feature step, Supplier<Feature<T>> featureSupplier, Supplier<T> featureConfigSupplier, Predicate<BiomeModifications.BiomeContext> predicate, Supplier<List<PlacementModifier>> placementModifiersSupplier) {
        this.name = name;

        this.configuredFeatureKey = RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, this.getId());
        this.configuredFeatureSupplier = () -> new ConfiguredFeature<>(featureSupplier.get(), featureConfigSupplier.get());

        this.placedFeatureKey = RegistryKey.of(RegistryKeys.PLACED_FEATURE, this.getId());
        this.placedFeatureBuilder = (configuredFeature) -> new PlacedFeature(configuredFeature, placementModifiersSupplier.get());

        BiomeModifications.addProperties(predicate, (ctx, props) -> props.getGenerationProperties().addFeature(step, this.placedFeatureKey));
        ModWorldGen.FEATURE_BUILDERS.add(this);
    }

    public static <T extends FeatureConfig> FeatureBuilder create(String name, GenerationStep.Feature step, Supplier<Feature<T>> featureSupplier, Supplier<T> featureConfigSupplier, Predicate<BiomeModifications.BiomeContext> predicate, Supplier<List<PlacementModifier>> placementModifiersSupplier) {
        Supplier<Feature<T>> registeredFeature = Registration.registerFeature(name, featureSupplier);
        return new FeatureBuilder(name, step, registeredFeature, featureConfigSupplier, predicate, placementModifiersSupplier);
    }

    public static FeatureBuilder createOre(String name, int veinSize, Predicate<BiomeModifications.BiomeContext> predicate, Supplier<List<OreFeatureConfig.Target>> targetsSupplier, Supplier<List<PlacementModifier>> placementModifiersSupplier) {
        return new FeatureBuilder(name, GenerationStep.Feature.UNDERGROUND_ORES, () -> Feature.ORE, () -> new OreFeatureConfig(targetsSupplier.get(), veinSize), predicate, placementModifiersSupplier);
    }

    public Identifier getId() {
        return DWM.getIdentifier(this.getName());
    }

    public String getName() {
        return this.name;
    }

    public void build(Registerable<ConfiguredFeature<?, ?>> configuredFeatureRegistry, Registerable<PlacedFeature> placedFeatureRegistry) {
        RegistryEntry.Reference<ConfiguredFeature<?, ?>> configuredFeature = configuredFeatureRegistry.register(this.configuredFeatureKey, this.configuredFeatureSupplier.get());
        placedFeatureRegistry.register(this.placedFeatureKey, this.placedFeatureBuilder.apply(configuredFeature));
    }
}

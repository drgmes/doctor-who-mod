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
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;
import java.util.function.Predicate;

public class OreFeatureBuilder {
    private final String name;
    private final PlacedFeature placedFeature;
    private final ConfiguredFeature<?, ?> configuredFeature;

    public OreFeatureBuilder(String name, int veinSize, Predicate<BiomeSelectionContext> biomeSelector, List<OreFeatureConfig.Target> targets, List<PlacementModifier> placementModifiers) {
        ConfiguredFeature<?, ?> configuredFeature = new ConfiguredFeature<>(Feature.ORE, new OreFeatureConfig(targets, veinSize));
        PlacedFeature placedFeature = new PlacedFeature(RegistryEntry.of(configuredFeature), placementModifiers);

        this.name = name;
        this.placedFeature = Registration.registerPlacedFeature(this.name, placedFeature);
        this.configuredFeature = Registration.registerConfiguredFeature(this.name, configuredFeature);

        BiomeModifications.addFeature(biomeSelector, GenerationStep.Feature.UNDERGROUND_ORES, RegistryKey.of(Registry.PLACED_FEATURE_KEY, this.getId()));
        ModWorldGen.ORE_FEATURE_BUILDERS.add(this);
    }

    public Identifier getId() {
        return DWM.getIdentifier(this.getName());
    }

    public String getName() {
        return this.name;
    }

    public PlacedFeature getPlacedFeature() {
        return this.placedFeature;
    }

    public ConfiguredFeature<?, ?> getConfiguredFeature() {
        return this.configuredFeature;
    }
}

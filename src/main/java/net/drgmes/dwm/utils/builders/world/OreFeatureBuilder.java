package net.drgmes.dwm.utils.builders.world;

import net.drgmes.dwm.setup.ModWorldGen;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;

import java.util.List;

public class OreFeatureBuilder {
    public final String name;
    public final int veinSize;
    public final PlacementModifier[] placementModifiers;
    private Holder<PlacedFeature> placedFeature;

    public OreFeatureBuilder(String parentOreKey, int veinSize, PlacementModifier[] placementModifiers) {
        this.name = parentOreKey;
        this.veinSize = veinSize;
        this.placementModifiers = placementModifiers;
    }

    public Holder<PlacedFeature> getRegisteredFeature() {
        if (this.placedFeature == null) {
            List<OreConfiguration.TargetBlockState> targets = ModWorldGen.ORES_TARGET_LIST.get(this.name);

            Holder<ConfiguredFeature<OreConfiguration, ?>> oreFeature = FeatureUtils.register(this.name, Feature.ORE, new OreConfiguration(targets, this.veinSize));
            this.placedFeature = PlacementUtils.register(this.name, oreFeature, this.placementModifiers);
        }

        return this.placedFeature;
    }
}

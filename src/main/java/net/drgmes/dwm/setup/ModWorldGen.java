package net.drgmes.dwm.setup;

import net.drgmes.dwm.utils.builders.FeatureBuilder;
import net.drgmes.dwm.world.features.AbandonedTardisFeature;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Block;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.*;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModWorldGen {
    public static final ArrayList<FeatureBuilder> FEATURE_BUILDERS = new ArrayList<>();

    // //////// //
    // FEATURES //
    // //////// //

    public static final Feature<DefaultFeatureConfig> ABANDONED_TARDIS = Registration.registerFeature("abandoned_tardis", new AbandonedTardisFeature());

    // ////// //
    // COMMON //
    // ////// //

    public static final FeatureBuilder ABANDONED_TARDIS_UNDERGROUND = new FeatureBuilder(
        "abandoned_tardis_underground",
        ABANDONED_TARDIS,
        DefaultFeatureConfig.INSTANCE,
        GenerationStep.Feature.UNDERGROUND_STRUCTURES,
        BiomeSelectors.foundInOverworld(),
        getPlacementModifiers(
            CountPlacementModifier.of(1),
            RarityFilterPlacementModifier.of(ModConfig.COMMON.abandonedTardisUndergroundSpawnChance.get()),
            HeightRangePlacementModifier.uniform(YOffset.BOTTOM, YOffset.aboveBottom(96)),
            EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 32),
            RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1))
        )
    );

    public static final FeatureBuilder ABANDONED_TARDIS_SURFACE = new FeatureBuilder(
        "abandoned_tardis_surface",
        ABANDONED_TARDIS,
        DefaultFeatureConfig.INSTANCE,
        GenerationStep.Feature.SURFACE_STRUCTURES,
        BiomeSelectors.foundInOverworld(),
        getPlacementModifiers(
            CountPlacementModifier.of(1),
            RarityFilterPlacementModifier.of(ModConfig.COMMON.abandonedTardisSurfaceSpawnChance.get()),
            PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
            PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP
        )
    );

    public static final FeatureBuilder ABANDONED_TARDIS_NETHER = new FeatureBuilder(
        "abandoned_tardis_nether",
        ABANDONED_TARDIS,
        DefaultFeatureConfig.INSTANCE,
        GenerationStep.Feature.UNDERGROUND_STRUCTURES,
        BiomeSelectors.foundInTheNether(),
        getPlacementModifiers(
            CountPlacementModifier.of(1),
            RarityFilterPlacementModifier.of(ModConfig.COMMON.abandonedTardisNetherSpawnChance.get()),
            HeightRangePlacementModifier.uniform(YOffset.BOTTOM, YOffset.aboveBottom(96)),
            EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR, 32),
            RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1))
        )
    );

    // //// //
    // ORES //
    // //// //

    public static final FeatureBuilder TITANIUM_ORE = FeatureBuilder.createOre(
        "titanium_ore",
        6,
        BiomeSelectors.foundInOverworld(),
        getPlacementModifiers(CountPlacementModifier.of(14), HeightRangePlacementModifier.trapezoid(YOffset.fixed(-48), YOffset.fixed(112))),
        createOreWithDeepslateTargets(ModBlocks.TITANIUM_ORE.getBlock(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock())
    );

    public static final FeatureBuilder TITANIUM_ORE_LARGE = FeatureBuilder.createOre(
        "titanium_ore_large",
        15,
        BiomeSelectors.foundInOverworld(),
        getPlacementModifiers(CountPlacementModifier.of(14), HeightRangePlacementModifier.trapezoid(YOffset.fixed(-48), YOffset.fixed(112))),
        createOreWithDeepslateTargets(ModBlocks.TITANIUM_ORE.getBlock(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock())
    );

    public static void init() {
    }

    private static List<PlacementModifier> getPlacementModifiers(PlacementModifier... modifiers) {
        List<PlacementModifier> placementModifiers = new ArrayList<>(Arrays.stream(modifiers).toList());
        placementModifiers.add(SquarePlacementModifier.of());
        placementModifiers.add(BiomePlacementModifier.of());
        return placementModifiers;
    }

    private static OreFeatureConfig.Target createOreTarget(RuleTest ruleTest, Block block) {
        return OreFeatureConfig.createTarget(ruleTest, block.getDefaultState());
    }

    private static List<OreFeatureConfig.Target> createOreTargets(Block block) {
        return List.of(createOreTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, block));
    }

    private static List<OreFeatureConfig.Target> createDeepslateOreTargets(Block block) {
        return List.of(createOreTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, block));
    }

    private static List<OreFeatureConfig.Target> createOreWithDeepslateTargets(Block block, Block deepslateBlock) {
        return List.of(createOreTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, block), createOreTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateBlock));
    }
}

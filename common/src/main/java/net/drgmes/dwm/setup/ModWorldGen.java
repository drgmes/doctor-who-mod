package net.drgmes.dwm.setup;

import net.drgmes.dwm.utils.builders.FeatureBuilder;
import net.drgmes.dwm.world.features.AbandonedTardisFeature;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.intprovider.ConstantIntProvider;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.blockpredicate.BlockPredicate;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeatures;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public class ModWorldGen {
    public static final ArrayList<FeatureBuilder> FEATURE_BUILDERS = new ArrayList<>();

    // //////// //
    // FEATURES //
    // //////// //

    public static final Supplier<Feature<DefaultFeatureConfig>> ABANDONED_TARDIS = Registration.registerFeature("abandoned_tardis", AbandonedTardisFeature::new);

    // ////// //
    // COMMON //
    // ////// //

    public static final FeatureBuilder ABANDONED_TARDIS_UNDERGROUND = new FeatureBuilder(
        "abandoned_tardis_underground",
        GenerationStep.Feature.UNDERGROUND_STRUCTURES,
        ABANDONED_TARDIS,
        () -> DefaultFeatureConfig.INSTANCE,
        (ctx) -> ctx.hasTag(BiomeTags.IS_OVERWORLD),
        () -> getPlacementModifiers(
            CountPlacementModifier.of(1),
            RarityFilterPlacementModifier.of(10),
            HeightRangePlacementModifier.uniform(YOffset.BOTTOM, YOffset.aboveBottom(96)),
            EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR_OR_WATER, 32),
            RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1))
        )
    );

    public static final FeatureBuilder ABANDONED_TARDIS_SURFACE = new FeatureBuilder(
        "abandoned_tardis_surface",
        GenerationStep.Feature.SURFACE_STRUCTURES,
        ABANDONED_TARDIS,
        () -> DefaultFeatureConfig.INSTANCE,
        (ctx) -> ctx.hasTag(BiomeTags.IS_OVERWORLD),
        () -> getPlacementModifiers(
            CountPlacementModifier.of(1),
            RarityFilterPlacementModifier.of(50),
            PlacedFeatures.WORLD_SURFACE_WG_HEIGHTMAP,
            PlacedFeatures.OCEAN_FLOOR_WG_HEIGHTMAP
        )
    );

    public static final FeatureBuilder ABANDONED_TARDIS_NETHER = new FeatureBuilder(
        "abandoned_tardis_nether",
        GenerationStep.Feature.UNDERGROUND_STRUCTURES,
        ABANDONED_TARDIS,
        () -> DefaultFeatureConfig.INSTANCE,
        (ctx) -> ctx.hasTag(BiomeTags.IS_NETHER),
        () -> getPlacementModifiers(
            CountPlacementModifier.of(1),
            RarityFilterPlacementModifier.of(1),
            HeightRangePlacementModifier.uniform(YOffset.BOTTOM, YOffset.aboveBottom(96)),
            EnvironmentScanPlacementModifier.of(Direction.DOWN, BlockPredicate.solid(), BlockPredicate.IS_AIR, 32),
            RandomOffsetPlacementModifier.vertically(ConstantIntProvider.create(1))
        )
    );

    // //// //
    // ORES //
    // //// //

    public static final FeatureBuilder TITANIUM_ORE_SMALL = FeatureBuilder.createOre(
        "ore_titanium_small",
        6,
        (ctx) -> ctx.hasTag(BiomeTags.IS_OVERWORLD),
        () -> createOreWithDeepslateTargets(ModBlocks.TITANIUM_ORE.getBlock(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()),
        () -> getPlacementModifiers(CountPlacementModifier.of(14), HeightRangePlacementModifier.trapezoid(YOffset.fixed(-48), YOffset.fixed(112)))
    );

    public static final FeatureBuilder TITANIUM_ORE_LARGE = FeatureBuilder.createOre(
        "ore_titanium_large",
        15,
        (ctx) -> ctx.hasTag(BiomeTags.IS_OVERWORLD),
        () -> createOreWithDeepslateTargets(ModBlocks.TITANIUM_ORE.getBlock(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()),
        () -> getPlacementModifiers(CountPlacementModifier.of(14), HeightRangePlacementModifier.trapezoid(YOffset.fixed(-48), YOffset.fixed(112)))
    );

    public static void init() {
    }

    private static List<PlacementModifier> getPlacementModifiers(PlacementModifier... modifiers) {
        List<PlacementModifier> placementModifiers = new ArrayList<>(Arrays.stream(modifiers).toList());
        placementModifiers.add(SquarePlacementModifier.of());
        placementModifiers.add(BiomePlacementModifier.of());
        return placementModifiers;
    }

    private static OreFeatureConfig.Target createOreTarget(TagKey<Block> blockTag, Block block) {
        return OreFeatureConfig.createTarget(new TagMatchRuleTest(blockTag), block.getDefaultState());
    }

    private static List<OreFeatureConfig.Target> createOreTargets(Block block) {
        return List.of(createOreTarget(BlockTags.STONE_ORE_REPLACEABLES, block));
    }

    private static List<OreFeatureConfig.Target> createDeepslateOreTargets(Block block) {
        return List.of(createOreTarget(BlockTags.DEEPSLATE_ORE_REPLACEABLES, block));
    }

    private static List<OreFeatureConfig.Target> createOreWithDeepslateTargets(Block block, Block deepslateBlock) {
        return List.of(createOreTarget(BlockTags.STONE_ORE_REPLACEABLES, block), createOreTarget(BlockTags.DEEPSLATE_ORE_REPLACEABLES, deepslateBlock));
    }
}

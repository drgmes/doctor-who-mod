package net.drgmes.dwm.setup;

import net.drgmes.dwm.utils.builders.OreFeatureBuilder;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.block.Block;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.OreConfiguredFeatures;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.ArrayList;
import java.util.List;

public class ModWorldGen {
    public static final ArrayList<OreFeatureBuilder> ORE_FEATURE_BUILDERS = new ArrayList<>();

    public static final OreFeatureBuilder TITANIUM_ORE = new OreFeatureBuilder(
        "titanium_ore", 6,
        BiomeSelectors.foundInOverworld(),
        createOreWithDeepslateTarget(ModBlocks.TITANIUM_ORE.getBlock(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()),
        getPlacementModifiers(14, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-48), YOffset.fixed(112)))
    );

    public static final OreFeatureBuilder TITANIUM_ORE_LARGE = new OreFeatureBuilder(
        "titanium_ore_large", 15,
        BiomeSelectors.foundInOverworld(),
        createOreWithDeepslateTarget(ModBlocks.TITANIUM_ORE.getBlock(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()),
        getPlacementModifiers(14, HeightRangePlacementModifier.trapezoid(YOffset.fixed(-48), YOffset.fixed(112)))
    );

    public static void init() {
    }

    private static OreFeatureConfig.Target createTarget(RuleTest ruleTest, Block block) {
        return OreFeatureConfig.createTarget(ruleTest, block.getDefaultState());
    }

    private static List<OreFeatureConfig.Target> createOreTarget(Block block) {
        return List.of(createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, block));
    }

    private static List<OreFeatureConfig.Target> createDeepslateOreTarget(Block block) {
        return List.of(createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, block));
    }

    private static List<OreFeatureConfig.Target> createOreWithDeepslateTarget(Block block, Block deepslateBlock) {
        return List.of(createTarget(OreConfiguredFeatures.STONE_ORE_REPLACEABLES, block), createTarget(OreConfiguredFeatures.DEEPSLATE_ORE_REPLACEABLES, deepslateBlock));
    }

    private static List<PlacementModifier> getPlacementModifiers(int count, PlacementModifier heightModifier) {
        return List.of(CountPlacementModifier.of(count), SquarePlacementModifier.of(), heightModifier, BiomePlacementModifier.of());
    }
}

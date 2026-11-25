package net.drgmes.dwm.setup;

import net.drgmes.dwm.utils.builders.FeatureBuilder;
import net.minecraft.block.Block;
import net.minecraft.registry.tag.BiomeTags;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.OreFeatureConfig;
import net.minecraft.world.gen.placementmodifier.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ModWorldGen {
    public static final ArrayList<FeatureBuilder> FEATURE_BUILDERS = new ArrayList<>();

    // //// //
    // ORES //
    // //// //

    public static final FeatureBuilder TITANIUM_ORE_SMALL = FeatureBuilder.createOre(
        "ore_titanium_small",
        6,
        (ctx) -> ctx.hasTag(BiomeTags.IS_OVERWORLD),
        () -> createOreWithDeepslateTargets(ModBlocks.TITANIUM_ORE.getBlock(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()),
        () -> getPlacementModifiers(CountPlacementModifier.of(10), HeightRangePlacementModifier.trapezoid(YOffset.fixed(-48), YOffset.fixed(112)))
    );

    public static final FeatureBuilder TITANIUM_ORE_LARGE = FeatureBuilder.createOre(
        "ore_titanium_large",
        15,
        (ctx) -> ctx.hasTag(BiomeTags.IS_OVERWORLD),
        () -> createOreWithDeepslateTargets(ModBlocks.TITANIUM_ORE.getBlock(), ModBlocks.TITANIUM_ORE_DEEPSLATE.getBlock()),
        () -> getPlacementModifiers(CountPlacementModifier.of(10), HeightRangePlacementModifier.trapezoid(YOffset.fixed(-48), YOffset.fixed(112)))
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

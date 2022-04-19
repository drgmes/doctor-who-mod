package net.drgmes.dwm.setup;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;

import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.drgmes.dwm.utils.builders.world.OreFeatureBuilder;
import net.minecraft.core.Holder;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.fml.Bindings;

public class ModWorldGen {
    public static final Map<String, List<OreConfiguration.TargetBlockState>> ORES_TARGET_LIST = Maps.newHashMap();

    public static final List<OreFeatureBuilder> OVERWORLD_ORES = new ArrayList<>();
    public static final List<OreFeatureBuilder> NETHER_ORES = new ArrayList<>();
    public static final List<OreFeatureBuilder> END_ORES = new ArrayList<>();

    public static void init() {
        Bindings.getForgeBus().get().addListener(ModWorldGen::biomeLoading);
    }

    public static void setup() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            blockBuilder.registerWorldGen();
        }
    }

    public static void appendOreTarget(BlockBuilder blockBuilder, RuleTest ruleTest, String oreKey) {
        List<OreConfiguration.TargetBlockState> list;

        if (!ModWorldGen.ORES_TARGET_LIST.containsKey(oreKey)) list = new ArrayList<>();
        else list = ModWorldGen.ORES_TARGET_LIST.get(oreKey);

        list.add(OreConfiguration.target(ruleTest, blockBuilder.get().defaultBlockState()));
        ModWorldGen.ORES_TARGET_LIST.put(oreKey, list);
    }

    public static void appendOreTarget(BlockBuilder blockBuilder, RuleTest ruleTest) {
        ModWorldGen.appendOreTarget(blockBuilder, ruleTest, blockBuilder.name);
    }

    public static void biomeLoading(BiomeLoadingEvent event) {
        List<Holder<PlacedFeature>> oreFeatures = event.getGeneration().getFeatures(GenerationStep.Decoration.UNDERGROUND_ORES);

        switch (event.getCategory()) {
            case NETHER -> ModWorldGen.NETHER_ORES.forEach(oreFeatureBuilder -> oreFeatures.add(oreFeatureBuilder.getRegisteredFeature()));
            case THEEND -> ModWorldGen.END_ORES.forEach(oreFeatureBuilder -> oreFeatures.add(oreFeatureBuilder.getRegisteredFeature()));
            default -> ModWorldGen.OVERWORLD_ORES.forEach(oreFeatureBuilder -> oreFeatures.add(oreFeatureBuilder.getRegisteredFeature()));
        }
    }
}

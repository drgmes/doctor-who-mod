package net.drgmes.dwm.utils.builders.world;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModStructures;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Supplier;

public class FeatureBuilder {
    public final String name;
    public final RegistryObject<Feature<?>> featureObject;

    public final TagKey<Biome> biomeTag;
    public final ArrayList<TagKey<Biome>> biomeTags = new ArrayList<>();
    public final ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();


    public FeatureBuilder(String name, Supplier<Feature<?>> Structure) {
        ResourceLocation loc = new ResourceLocation(DWM.MODID, name);

        this.name = name;
        this.biomeTag = TagKey.create(Registry.BIOME_REGISTRY, loc);
        this.featureObject = Registration.registerFeature(name, Structure);

        ModStructures.FEATURE_BUILDERS.add(this);
    }

    public Feature<?> get() {
        return this.featureObject.get();
    }

    public FeatureBuilder addBiomeTag(TagKey<Biome> biomeTag) {
        this.biomeTags.add(biomeTag);
        return this;
    }

    public FeatureBuilder addBiome(ResourceKey<Biome> biome) {
        this.biomes.add(biome);
        return this;
    }
}

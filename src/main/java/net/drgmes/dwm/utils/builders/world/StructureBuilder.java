package net.drgmes.dwm.utils.builders.world;

import java.util.ArrayList;
import java.util.function.Supplier;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModStructures;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.RegistryObject;

public class StructureBuilder {
    public final String name;
    public final RegistryObject<StructureFeature<?>> structureObject;

    public final TagKey<Biome> biomeTag;
    public final ArrayList<TagKey<Biome>> biomeTags = new ArrayList<>();
    public final ArrayList<ResourceKey<Biome>> biomes = new ArrayList<>();


    public StructureBuilder(String name, Supplier<StructureFeature<?>> structureFeature) {
        ResourceLocation loc = new ResourceLocation(DWM.MODID, name);

        this.name = name;
        this.biomeTag = TagKey.create(Registry.BIOME_REGISTRY, loc);
        this.structureObject = Registration.registerStructure(name, structureFeature);

        ModStructures.STRUCTURE_BUILDERS.add(this);
    }

    public StructureBuilder addBiomeTag(TagKey<Biome> biomeTag) {
        this.biomeTags.add(biomeTag);
        return this;
    }

    public StructureBuilder addBiome(ResourceKey<Biome> biome) {
        this.biomes.add(biome);
        return this;
    }
}

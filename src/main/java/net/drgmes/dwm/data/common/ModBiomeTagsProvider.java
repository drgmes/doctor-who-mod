package net.drgmes.dwm.data.common;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.setup.ModStructures;
import net.drgmes.dwm.utils.builders.world.StructureBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ModBiomeTagsProvider extends BiomeTagsProvider {
    public ModBiomeTagsProvider(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, DWM.MODID, existingFileHelper);
    }

    @Override
    protected void addTags() {
        for (StructureBuilder structureBuilder : ModStructures.STRUCTURE_BUILDERS) {
            for (TagKey<Biome> biomeTag : structureBuilder.biomeTags) {
                this.tag(structureBuilder.biomeTag).addTag(biomeTag);
            }

            for (ResourceKey<Biome> biome : structureBuilder.biomes) {
                this.tag(structureBuilder.biomeTag).add(biome);
            }
        }
    }
}

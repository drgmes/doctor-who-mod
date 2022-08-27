package net.drgmes.dwm.datagen.common;

import net.drgmes.dwm.setup.ModVillagerProfessions;
import net.drgmes.dwm.utils.builders.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.tag.PointOfInterestTypeTags;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.poi.PointOfInterestType;

public class ModPoiTypesTagsProvider extends FabricTagProvider<PointOfInterestType> {
    public ModPoiTypesTagsProvider(FabricDataGenerator dataGenerator) {
        super(dataGenerator, Registry.POINT_OF_INTEREST_TYPE);
    }

    @Override
    protected void generateTags() {
        FabricTagBuilder<PointOfInterestType> tag = this.getOrCreateTagBuilder(PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE);

        for (VillagerProfessionBuilder villagerProfessionBuilder : ModVillagerProfessions.VILLAGER_PROFESSION_BUILDERS) {
            tag.add(villagerProfessionBuilder.getPoiKey());
        }
    }
}

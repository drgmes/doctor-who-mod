package net.drgmes.dwm.fabric.datagen.common;

import net.drgmes.dwm.setup.ModVillagerProfessions;
import net.drgmes.dwm.utils.builders.VillagerProfessionBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.registry.BuiltinRegistries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.tag.PointOfInterestTypeTags;
import net.minecraft.util.Util;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.concurrent.CompletableFuture;

public class ModPoiTypesTagsProvider extends FabricTagProvider<PointOfInterestType> {
    public ModPoiTypesTagsProvider(FabricDataOutput output) {
        super(output, RegistryKeys.POINT_OF_INTEREST_TYPE, CompletableFuture.supplyAsync(BuiltinRegistries::createWrapperLookup, Util.getMainWorkerExecutor()));
    }

    @Override
    protected void configure(RegistryWrapper.WrapperLookup arg) {
        FabricTagBuilder tag = this.getOrCreateTagBuilder(PointOfInterestTypeTags.ACQUIRABLE_JOB_SITE);

        for (VillagerProfessionBuilder villagerProfessionBuilder : ModVillagerProfessions.VILLAGER_PROFESSION_BUILDERS) {
            tag.add(villagerProfessionBuilder.getPoiKey());
        }
    }
}

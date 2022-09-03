package net.drgmes.dwm.setup;

import com.google.common.collect.ImmutableSet;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.common.tardis.ars.ArsCategories;
import net.drgmes.dwm.common.tardis.ars.ArsStructures;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.poi.PointOfInterestType;
import qouteall.q_misc_util.LifecycleHack;

import java.util.function.Supplier;

public class Registration {
    public static void setupCommon() {
        LifecycleHack.markNamespaceStable(DWM.MODID);

        ModSounds.init();
        ModItems.init();
        ModBlocks.init();
        ModBlockEntities.init();
        ModCreativeTabs.init();
        ModEntities.init();
        ModBiomes.init();
        ModDimensions.init();
        ModWorldGen.init();
        ModInventories.init();
        ModVillagerProfessions.init();

        ArsCategories.init();
        ArsStructures.init();

        ModConfig.setup();
        ModResourcePacks.setup();
        ModEvents.setup();
        ModDimensions.setup();
        ModVillagerProfessions.setup();
    }

    @Environment(EnvType.CLIENT)
    public static void setupClient() {
        ModKeys.setup();
        ModRenderers.setup();
        ModInventories.setup();
    }

    @Environment(EnvType.SERVER)
    public static void setupServer() {
    }

    public static <T extends Item> T registerItem(String name, T item) {
        Registry.register(Registry.ITEM, DWM.getIdentifier(name), item);
        return item;
    }

    public static <T extends Block> T registerBlock(String name, T block) {
        Registry.register(Registry.BLOCK, DWM.getIdentifier(name), block);
        return block;
    }

    public static <T extends Entity> EntityType<T> registerEntity(String name, EntityType.EntityFactory<T> factory, SpawnGroup spawnGroup, float width, float height, int trackingRange, int updateFreq) {
        FabricEntityTypeBuilder<T> builder = FabricEntityTypeBuilder.create(spawnGroup, factory);
        builder.dimensions(EntityDimensions.fixed(width, height));
        builder.trackRangeBlocks(trackingRange);
        builder.trackedUpdateRate(updateFreq);

        return Registry.register(Registry.ENTITY_TYPE, DWM.getIdentifier(name), builder.build());
    }

    public static <T extends BlockEntity> BlockEntityType<T> registerBlockEntity(String name, FabricBlockEntityTypeBuilder.Factory<T> factory, Block block) {
        return Registry.register(Registry.BLOCK_ENTITY_TYPE, DWM.getIdentifier(name), FabricBlockEntityTypeBuilder.create(factory, block).build(null));
    }

    public static <T extends ScreenHandler> ScreenHandlerType<T> registerScreenHandler(String name, ScreenHandlerType.Factory<T> screenHandlerFactory) {
        return Registry.register(Registry.SCREEN_HANDLER, DWM.getIdentifier(name), new ScreenHandlerType<>(screenHandlerFactory));
    }

    public static <T extends Biome> T registerBiome(String name, Supplier<T> biomeSupplier) {
        return Registry.register(BuiltinRegistries.BIOME, DWM.getIdentifier(name), biomeSupplier.get());
    }

    public static <T extends ConfiguredFeature<?, ?>> T registerConfiguredFeature(String name, T configuredFeature) {
        return Registry.register(BuiltinRegistries.CONFIGURED_FEATURE, DWM.getIdentifier(name), configuredFeature);
    }

    public static <T extends PlacedFeature> T registerPlacedFeature(String name, T placedFeature) {
        return Registry.register(BuiltinRegistries.PLACED_FEATURE, DWM.getIdentifier(name), placedFeature);
    }

    public static VillagerProfession registerVillagerProfession(String name, PointOfInterestType poiType, SoundEvent workSound) {
        return Registry.register(Registry.VILLAGER_PROFESSION, DWM.getIdentifier(name), new VillagerProfession(name, (entry) -> entry.value().equals(poiType), (entry) -> entry.value().equals(poiType), ImmutableSet.of(), ImmutableSet.of(), workSound));
    }

    public static SoundEvent registerSoundEvent(String name) {
        return Registry.register(Registry.SOUND_EVENT, DWM.getIdentifier(name), new SoundEvent(DWM.getIdentifier(name)));
    }

    public static KeyBinding registerKeyBinding(String name, String category, int keycode) {
        return KeyBindingHelper.registerKeyBinding(new KeyBinding("key." + DWM.MODID + "." + name, keycode, category));
    }
}

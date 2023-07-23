package net.drgmes.dwm.setup;

import com.mojang.serialization.Codec;
import dev.architectury.networking.simple.BaseC2SMessage;
import dev.architectury.networking.simple.BaseS2CMessage;
import dev.architectury.networking.simple.MessageDecoder;
import dev.architectury.networking.simple.MessageType;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.keymappings.KeyMappingRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.compat.ClothConfig;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.screen.ScreenHandlerType;
import net.minecraft.sound.SoundEvent;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.function.Supplier;

public class Registration {
    public static final DeferredRegister<ItemGroup> ITEM_GROUPS = DeferredRegister.create(DWM.MODID, RegistryKeys.ITEM_GROUP);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(DWM.MODID, RegistryKeys.ITEM);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(DWM.MODID, RegistryKeys.BLOCK);
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS_ENTITIES = DeferredRegister.create(DWM.MODID, RegistryKeys.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(DWM.MODID, RegistryKeys.ENTITY_TYPE);
    public static final DeferredRegister<Codec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(DWM.MODID, RegistryKeys.CHUNK_GENERATOR);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(DWM.MODID, RegistryKeys.FEATURE);
    public static final DeferredRegister<PointOfInterestType> POINT_OF_INTEREST_TYPES = DeferredRegister.create(DWM.MODID, RegistryKeys.POINT_OF_INTEREST_TYPE);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(DWM.MODID, RegistryKeys.VILLAGER_PROFESSION);
    public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS = DeferredRegister.create(DWM.MODID, RegistryKeys.SCREEN_HANDLER);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(DWM.MODID, RegistryKeys.SOUND_EVENT);

    public static void setupCommon() {
        ITEM_GROUPS.register();
        ITEMS.register();
        BLOCKS.register();
        BLOCKS_ENTITIES.register();
        ENTITIES.register();
        CHUNK_GENERATORS.register();
        FEATURES.register();
        POINT_OF_INTEREST_TYPES.register();
        VILLAGER_PROFESSIONS.register();
        SCREEN_HANDLERS.register();
        SOUND_EVENTS.register();

        ModCreativeTabs.init();
        ModSounds.init();
        ModItems.init();
        ModBlocks.init();
        ModBlockEntities.init();
        ModEntities.init();
        ModBiomes.init();
        ModDimensions.init();
        ModWorldGen.init();
        ModInventories.init();
        ModVillagerProfessions.init();
        ModNetwork.init();

        ModEvents.setup();
        ModCommands.setup();
        ModBlockEntities.setup();
        if (ModCompats.clothConfig()) ClothConfig.setup();
    }

    public static void setupClient() {
        ModRenderers.setup();
        ModScreens.setup();
        ModKeys.setup();
    }

    public static void setupServer() {
    }

    public static RegistrySupplier<ItemGroup> registerItemGroup(String name, Supplier<ItemStack> iconSupplier) {
        Identifier id = DWM.getIdentifier(name);
        return ITEM_GROUPS.register(id, () -> CreativeTabRegistry.create(Text.translatable("itemGroup." + id.toTranslationKey()), iconSupplier));
    }

    public static <T extends Item> RegistrySupplier<T> registerItem(String name, Supplier<T> itemSupplier) {
        return ITEMS.register(DWM.getIdentifier(name), itemSupplier);
    }

    public static <T extends Block> RegistrySupplier<T> registerBlock(String name, Supplier<T> blockSupplier) {
        return BLOCKS.register(DWM.getIdentifier(name), blockSupplier);
    }

    public static <T extends BlockEntityType<?>> RegistrySupplier<T> registerBlockEntity(String name, Supplier<T> blockEntitySupplier) {
        return BLOCKS_ENTITIES.register(DWM.getIdentifier(name), blockEntitySupplier);
    }

    public static <T extends EntityType<?>> RegistrySupplier<T> registerEntity(String name, Supplier<T> entitySupplier) {
        return ENTITIES.register(DWM.getIdentifier(name), entitySupplier);
    }

    public static <T extends Codec<? extends ChunkGenerator>> RegistrySupplier<T> registerChunkGenerator(String name, Supplier<T> chunkGeneratorSupplier) {
        return CHUNK_GENERATORS.register(DWM.getIdentifier(name), chunkGeneratorSupplier);
    }

    public static <T extends Feature<?>> RegistrySupplier<T> registerFeature(String name, Supplier<T> featureSupplier) {
        return FEATURES.register(DWM.getIdentifier(name), featureSupplier);
    }

    public static <T extends PointOfInterestType> RegistrySupplier<T> registerPoiType(String name, Supplier<T> poiTypeSupplier) {
        return POINT_OF_INTEREST_TYPES.register(DWM.getIdentifier(name), poiTypeSupplier);
    }

    public static <T extends VillagerProfession> RegistrySupplier<T> registerVillagerProfession(String name, Supplier<T> villagerProfessionSupplier) {
        return VILLAGER_PROFESSIONS.register(DWM.getIdentifier(name), villagerProfessionSupplier);
    }

    public static <T extends ScreenHandlerType<?>> RegistrySupplier<T> registerScreenHandler(String name, Supplier<T> screenHandlerSupplier) {
        return SCREEN_HANDLERS.register(DWM.getIdentifier(name), screenHandlerSupplier);
    }

    public static RegistrySupplier<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(DWM.getIdentifier(name), () -> SoundEvent.of(DWM.getIdentifier(name)));
    }

    public static MessageType registerC2SMessageType(String name, MessageDecoder<BaseC2SMessage> decoder) {
        return ModNetwork.NETWORK_MANAGER.registerC2S(name, decoder);
    }

    public static MessageType registerS2CMessageType(String name, MessageDecoder<BaseS2CMessage> decoder) {
        return ModNetwork.NETWORK_MANAGER.registerS2C(name, decoder);
    }

    public static KeyBinding registerKeyBinding(String name, String category, int keycode) {
        KeyBinding keyBinding = new KeyBinding("key." + DWM.MODID + "." + name, keycode, category);
        KeyMappingRegistry.register(keyBinding);
        return keyBinding;
    }
}

package net.drgmes.dwm.setup;

import com.mojang.serialization.MapCodec;
import dev.architectury.networking.NetworkManager;
import dev.architectury.platform.Platform;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import dev.architectury.utils.Env;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.compat.clothconfig.ClothConfig;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.entity.EntityType;
import net.minecraft.item.ArmorMaterial;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
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
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS = DeferredRegister.create(DWM.MODID, RegistryKeys.ARMOR_MATERIAL);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(DWM.MODID, RegistryKeys.ITEM);
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(DWM.MODID, RegistryKeys.BLOCK);
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(DWM.MODID, RegistryKeys.ENTITY_TYPE);
    public static final DeferredRegister<BlockEntityType<?>> BLOCKS_ENTITIES = DeferredRegister.create(DWM.MODID, RegistryKeys.BLOCK_ENTITY_TYPE);
    public static final DeferredRegister<MapCodec<? extends ChunkGenerator>> CHUNK_GENERATORS = DeferredRegister.create(DWM.MODID, RegistryKeys.CHUNK_GENERATOR);
    public static final DeferredRegister<Feature<?>> FEATURES = DeferredRegister.create(DWM.MODID, RegistryKeys.FEATURE);
    public static final DeferredRegister<PointOfInterestType> POINT_OF_INTEREST_TYPES = DeferredRegister.create(DWM.MODID, RegistryKeys.POINT_OF_INTEREST_TYPE);
    public static final DeferredRegister<VillagerProfession> VILLAGER_PROFESSIONS = DeferredRegister.create(DWM.MODID, RegistryKeys.VILLAGER_PROFESSION);
    public static final DeferredRegister<ScreenHandlerType<?>> SCREEN_HANDLERS = DeferredRegister.create(DWM.MODID, RegistryKeys.SCREEN_HANDLER);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(DWM.MODID, RegistryKeys.SOUND_EVENT);

    public static void setupCommon() {
        ITEM_GROUPS.register();
        ARMOR_MATERIALS.register();
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
        ModMaterials.ArmorMaterials.init();
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

        ModNetwork.setup();
        ModEvents.setup();
        ModCommands.setup();
        ModBlockEntities.setup();
        if (ModCompats.clothConfig()) ClothConfig.setup();
    }

    public static void setupClient() {
        ModRenderers.setup();
    }

    public static void setupServer() {
    }

    public static RegistrySupplier<ItemGroup> registerItemGroup(String name, Supplier<ItemStack> iconSupplier) {
        Identifier id = DWM.getIdentifier(name);
        return ITEM_GROUPS.register(id, () -> CreativeTabRegistry.create(Text.translatable("itemGroup." + id.toTranslationKey()), iconSupplier));
    }

    public static <T extends ArmorMaterial> RegistrySupplier<T> registerArmorMaterial(String name, Supplier<T> armorMaterialSupplier) {
        return ARMOR_MATERIALS.register(DWM.getIdentifier(name), armorMaterialSupplier);
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

    public static <T extends MapCodec<? extends ChunkGenerator>> RegistrySupplier<T> registerChunkGenerator(String name, Supplier<T> chunkGeneratorSupplier) {
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

    public static <T extends CustomPayload> void registerPacket(NetworkManager.Side side, CustomPayload.Id<T> id, PacketCodec<? super RegistryByteBuf, T> codec, Supplier<NetworkManager.NetworkReceiver<T>> receiverSupplier) {
        if (side == NetworkManager.Side.S2C) {
            if (Platform.getEnvironment() == Env.SERVER) NetworkManager.registerS2CPayloadType(id, codec);
            else NetworkManager.registerReceiver(side, id, codec, receiverSupplier.get());
            return;
        }

        NetworkManager.registerReceiver(side, id, codec, receiverSupplier.get());
    }
}

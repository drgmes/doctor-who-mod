package net.drgmes.dwm.setup;

import java.util.function.Function;
import java.util.function.Supplier;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.data.DataGenerators;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.datafix.fixes.References;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.IForgeRegistryEntry;
import net.minecraftforge.registries.RegistryObject;

public class Registration {
    public static final ModCreativeTab CREATIVE_MODE_TAB = new ModCreativeTab(DWM.MODID);

    public static final DeferredRegister<Item> ITEMS = createRegistry(ForgeRegistries.ITEMS);
    public static final DeferredRegister<Block> BLOCKS = createRegistry(ForgeRegistries.BLOCKS);
    public static final DeferredRegister<EntityType<?>> ENTITIES = createRegistry(ForgeRegistries.ENTITIES);
    public static final DeferredRegister<MenuType<?>> CONTAINERS = createRegistry(ForgeRegistries.CONTAINERS);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = createRegistry(ForgeRegistries.BLOCK_ENTITIES);
    public static final DeferredRegister<StructureFeature<?>> STRUCTURES = createRegistry(ForgeRegistries.STRUCTURE_FEATURES);
    public static final DeferredRegister<Biome> BIOMES = createRegistry(ForgeRegistries.BIOMES);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = createRegistry(ForgeRegistries.SOUND_EVENTS);

    public static void init() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(Registration::setupCommon);
        modEventBus.addListener(DataGenerators::setup);
        modEventBus.addListener(ModRenderers::setup);
        modEventBus.addListener(ModRenderers::setupLayerDefinitions);

        ITEMS.register(modEventBus);
        BLOCKS.register(modEventBus);
        ENTITIES.register(modEventBus);
        CONTAINERS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        STRUCTURES.register(modEventBus);
        BIOMES.register(modEventBus);
        SOUND_EVENTS.register(modEventBus);

        ModItems.init();
        ModBlocks.init();
        ModEntities.init();
        ModContainers.init();
        ModBlockEntities.init();
        ModBiomes.init();
        ModDimensions.init();
        ModStructures.init();
        ModCapabilities.init();
        ModWorldGen.init();
        ModSounds.init();

        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.CLIENT, ModConfig.CLIENT_SPEC);

        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                modEventBus.addListener(Registration::setupClient);
            }
        });

        DistExecutor.safeRunWhenOn(Dist.DEDICATED_SERVER, () -> new DistExecutor.SafeRunnable() {
            @Override
            public void run() {
                modEventBus.addListener(Registration::setupServer);
            }
        });
    }

    @OnlyIn(Dist.CLIENT)
    public static void setupClient(FMLClientSetupEvent event) {
        ModRender.setup();
        ModContainerScreens.setup();

        Minecraft mc = Minecraft.getInstance();
        if(!mc.getMainRenderTarget().isStencilEnabled()) {
            event.enqueueWork(() -> mc.getMainRenderTarget().enableStencil());
        }
    }

    @OnlyIn(Dist.DEDICATED_SERVER)
    public static void setupServer(FMLDedicatedServerSetupEvent event) {
    }

    public static void setupCommon(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            ModDimensions.setup();
            ModWorldGen.setup();
            ModPackets.setup();
        });
    }

    public static <T extends IForgeRegistryEntry<T>> DeferredRegister<T> createRegistry(IForgeRegistry<T> registry) {
        return DeferredRegister.create(registry, DWM.MODID);
    }

    public static <T extends Item> RegistryObject<T> registerItem(String name, Supplier<T> factory) {
        return ITEMS.register(name, factory);
    }

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> factory, Function<Block, Item> item) {
        RegistryObject<T> blockObject = BLOCKS.register(name, factory);
        Registration.registerItem(name, () -> item.apply(blockObject.get()));
        return blockObject;
    }

    public static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int trackingRange, int updateFreq, boolean sendUpdate) {
        return Registration.ENTITIES.register(name, () -> {
            EntityType.Builder<T> builder = EntityType.Builder.of(factory, category);
            builder.setShouldReceiveVelocityUpdates(sendUpdate);
            builder.setTrackingRange(trackingRange);
            builder.setUpdateInterval(updateFreq);
            builder.sized(width, height);
            builder.fireImmune();
            return builder.build(new ResourceLocation(DWM.MODID, name).toString());
        });
    }

    public static <T extends BlockEntity> RegistryObject<BlockEntityType<T>> registerBlockEntity(String name, BlockEntityType.BlockEntitySupplier<T> factory, RegistryObject<? extends Block> block) {
        return Registration.BLOCK_ENTITIES.register(name, () -> (
            BlockEntityType.Builder.of(factory, block.get()).build(Util.fetchChoiceType(References.BLOCK_ENTITY, name))
        ));
    }

    public static <T extends AbstractContainerMenu> RegistryObject<MenuType<T>> registerContainer(String name, MenuType.MenuSupplier<T> menuSupplier) {
        return Registration.CONTAINERS.register(name, () -> new MenuType<>(menuSupplier));
    }

    public static <T extends StructureFeature<?>> RegistryObject<T> registerStructure(String name, Supplier<T> structureSupplier) {
        return Registration.STRUCTURES.register(name, structureSupplier);
    }

    public static RegistryObject<Biome> registerBiome(String name, Supplier<Biome> biomeSupplier) {
        return Registration.BIOMES.register(name, biomeSupplier);
    }

    public static RegistryObject<SoundEvent> registerSoundEvent(String name) {
        return SOUND_EVENTS.register(name, () -> new SoundEvent(new ResourceLocation(DWM.MODID, name)));
    }
}

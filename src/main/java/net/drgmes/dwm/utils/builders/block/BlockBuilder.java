package net.drgmes.dwm.utils.builders.block;

import net.drgmes.dwm.data.client.ModBlockStateProvider;
import net.drgmes.dwm.data.client.ModItemModelProvider;
import net.drgmes.dwm.data.common.ModLootTableProvider;
import net.drgmes.dwm.data.common.ModRecipeProvider;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.Registration;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class BlockBuilder {
    public final String name;
    public final RegistryObject<? extends Block> blockObject;
    public final ArrayList<TagKey<Block>> tags = new ArrayList<>();

    public boolean isDropDisabled = false;

    public BlockBuilder(String name, Supplier<? extends Block> factory, Function<Block, Item> itemSupplier) {
        this.name = name;
        this.blockObject = Registration.registerBlock(name, factory, itemSupplier);

        ModBlocks.BLOCK_BUILDERS.add(this);

        this.addTags();
    }

    public BlockBuilder(String name, Supplier<? extends Block> factory) {
        this(name, factory, (block) -> new BlockItem(block, new Item.Properties().tab(Registration.CREATIVE_MODE_TAB)));
    }

    public BlockBuilder(String name) {
        this(name, () -> new Block(getBlockBehaviour()));
    }

    public static BlockBehaviour.Properties getBlockBehaviour() {
        return BlockBehaviour.Properties
            .of(Material.METAL)
            .sound(SoundType.METAL)
            .strength(5.0F, 6.0F)
            .requiresCorrectToolForDrops();
    }

    public Block get() {
        return this.blockObject.get();
    }

    public String getResourceName() {
        return this.name;
    }

    public void registerBlockStateAndModel(ModBlockStateProvider provider) {
        provider.simpleBlock(this.get());
    }

    public void registerItemModel(ModItemModelProvider provider) {
        provider.withExistingParent(this.name, provider.modLoc("block/" + this.getResourceName()));
    }

    public void registerLootTable(ModLootTableProvider.ModBlockLootTable provider) {
        if (!this.isDropDisabled) provider.dropSelf(this.get());
    }

    public void registerRecipe(ModRecipeProvider provider, Consumer<FinishedRecipe> consumer) {
    }

    public void registerCustomRender() {
    }

    public void registerCustomEntityRenderer(EntityRenderersEvent.RegisterRenderers event) {
    }

    public void registerCustomLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
    }

    public void registerWorldGen() {
    }

    public void addTags() {
        this.tags.add(BlockTags.MINEABLE_WITH_PICKAXE);
    }

    public void disableNativeDrop() {
        this.isDropDisabled = true;
    }
}

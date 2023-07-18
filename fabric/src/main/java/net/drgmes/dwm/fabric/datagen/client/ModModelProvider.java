package net.drgmes.dwm.fabric.datagen.client;

import net.drgmes.dwm.datagen.BlockModelDataBuilder;
import net.drgmes.dwm.datagen.ItemModelDataBuilder;
import net.drgmes.dwm.fabric.helpers.BlockStateHelper;
import net.drgmes.dwm.fabric.helpers.ModelHelper;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.setup.ModItems;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.drgmes.dwm.utils.builders.ItemBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.minecraft.data.client.*;
import net.minecraft.util.Identifier;

import java.util.Map;
import java.util.Optional;

public class ModModelProvider extends FabricModelProvider {
    public ModModelProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            BlockModelDataBuilder blockModelDataBuilder = blockBuilder.getBlockModelDataBuilder();
            if (blockModelDataBuilder == null) continue;

            Map<TextureKey, Identifier> blockTextures = blockModelDataBuilder.blockTextures;
            Map<TextureKey, Identifier> itemTextures = blockModelDataBuilder.itemTextures;

            switch (blockModelDataBuilder.blockType) {
                default -> BlockStateHelper.createSimpleBlockStateAndModel(blockStateModelGenerator, blockBuilder);
                case SIMPLE -> BlockStateHelper.createSimpleBlockState(blockStateModelGenerator, blockBuilder, blockTextures.get(TextureKey.ALL));
                case VARIED_SLAB -> BlockStateHelper.createVariedSlabBlockStateAndModel(blockStateModelGenerator, blockBuilder, blockTextures.get(TextureKey.ALL));
                case SOLID_SLAB -> BlockStateHelper.createSolidSlabBlockStateAndModel(blockStateModelGenerator, blockBuilder, blockTextures.get(TextureKey.ALL), blockTextures.get(TextureKey.CONTENT));
                case ROTATABLE_WITHOUT_MODEL -> BlockStateHelper.createRotatableBlockState(blockStateModelGenerator, blockBuilder);
                case ROTATABLE -> BlockStateHelper.createRotatableBlockStateAndModel(blockStateModelGenerator, blockBuilder, blockTextures.get(TextureKey.TOP), blockTextures.get(TextureKey.SIDE), blockTextures.get(TextureKey.FRONT), blockTextures.get(TextureKey.SOUTH), blockTextures.get(TextureKey.BOTTOM));
                case DOOR -> BlockStateHelper.createDoorBlockStateAndModel(blockStateModelGenerator, blockBuilder, blockTextures.get(TextureKey.TOP), blockTextures.get(TextureKey.BOTTOM));
                case WALL -> BlockStateHelper.createWallBlockStateAndModel(blockStateModelGenerator, blockBuilder, blockTextures.getOrDefault(TextureKey.ALL, null));
                case STAIRS -> BlockStateHelper.createStairsBlockStateAndModel(blockStateModelGenerator, blockBuilder, blockTextures.get(TextureKey.ALL));

                case SIMPLE_WITH_MODEL_AND_UNIQUE_SIDE -> {
                    Map.Entry<TextureKey, Identifier> sideTexture = blockTextures.entrySet().stream().findAny().get();
                    BlockStateHelper.createSimpleBlockStateAndModelWithUniqueSide(blockStateModelGenerator, blockBuilder, sideTexture.getValue(), sideTexture.getKey());
                }
            }

            switch (blockModelDataBuilder.itemType) {
                case PARENTED -> blockStateModelGenerator.registerParentedItemModel(blockBuilder.getBlockItem(), blockBuilder.getId());
                case GENERATED -> ModelHelper.createGeneratedBlockItemModel(blockStateModelGenerator, itemTextures.get(TextureKey.ALL), itemTextures.get(TextureKey.CONTENT));
                case WALL_XY -> ModelHelper.createWallXYBlockItemModel(blockStateModelGenerator, itemTextures.get(TextureKey.SIDE), itemTextures.get(TextureKey.ALL), itemTextures.get(TextureKey.CONTENT));
                case WALL -> ModelHelper.createWallBlockItemModel(blockStateModelGenerator, itemTextures.get(TextureKey.ALL), itemTextures.get(TextureKey.CONTENT));
            }
        }
    }

    @Override
    public void generateItemModels(ItemModelGenerator itemModelGenerator) {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            generateItemModel(itemModelGenerator, blockBuilder.getItemModelDataBuilder());
        }

        for (ItemBuilder itemBuilder : ModItems.ITEM_BUILDERS) {
            generateItemModel(itemModelGenerator, itemBuilder.getItemModelDataBuilder());
        }
    }

    private void generateItemModel(ItemModelGenerator itemModelGenerator, ItemModelDataBuilder itemModelDataBuilder) {
        if (itemModelDataBuilder == null) return;

        switch (itemModelDataBuilder.itemType) {
            case PARENTED -> itemModelGenerator.register(itemModelDataBuilder.item, new Model(Optional.of(itemModelDataBuilder.texture), Optional.empty()));
            case GENERATED -> Models.GENERATED.upload(itemModelDataBuilder.modelId, TextureMap.layer0(itemModelDataBuilder.texture), itemModelGenerator.writer);
            case HANDHELD -> Models.HANDHELD.upload(itemModelDataBuilder.modelId, TextureMap.layer0(itemModelDataBuilder.texture), itemModelGenerator.writer);
        }
    }
}

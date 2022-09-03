package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;

public class BlockStateHelper {
    public static void createSimpleBlockStateWithModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier id) {
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(blockBuilder.getBlock(), id));
    }

    public static void createSimpleBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder) {
        ModelHelper.createSimpleModel(blockStateModelGenerator, blockBuilder.getId(), blockBuilder.getTexture());
        blockStateModelGenerator.registerSimpleState(blockBuilder.getBlock());
    }

    public static void createSimpleBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier yTextureId) {
        ModelHelper.createSimpleModel(blockStateModelGenerator, blockBuilder.getId(), blockBuilder.getTexture(), yTextureId);
        blockStateModelGenerator.registerSimpleState(blockBuilder.getBlock());
    }

    public static void createRotatableBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier topTextureId, Identifier sideTextureId, Identifier frontTextureId, Identifier southTextureId, Identifier bottomTextureId) {
        ModelHelper.createRotatableModel(blockStateModelGenerator, blockBuilder.getId(), topTextureId, sideTextureId, frontTextureId, southTextureId, bottomTextureId);
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(blockBuilder.getBlock());
    }

    public static void createSlabBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, BlockBuilder parent) {
        Identifier topModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_top");
        ModelHelper.createSlabModel(blockStateModelGenerator, blockBuilder.getTexture(), parent.getTexture(), blockBuilder.getId(), topModelId);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(blockBuilder.getBlock(), blockBuilder.getId(), topModelId, parent.getId()));
    }

    public static void createSlabBlockStateAndModelWithDouble(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, BlockBuilder parent) {
        Identifier topModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_top");
        Identifier doubleModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_double");
        ModelHelper.createSlabModelWithDouble(blockStateModelGenerator, blockBuilder.getTexture(), parent.getTexture(), blockBuilder.getId(), topModelId, doubleModelId);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(blockBuilder.getBlock(), blockBuilder.getId(), topModelId, doubleModelId));
    }

    public static void createStairsBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, BlockBuilder parent) {
        Identifier innerModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_inner");
        Identifier outerModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_outer");
        ModelHelper.createStairsModel(blockStateModelGenerator, blockBuilder.getTexture(), parent.getTexture(), blockBuilder.getId(), innerModelId, outerModelId);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createStairsBlockState(blockBuilder.getBlock(), innerModelId, blockBuilder.getId(), outerModelId));
    }

    public static void createWallBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, BlockBuilder parent, boolean xy) {
        Identifier postModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_post");
        Identifier sideModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_side");
        Identifier sideTallModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_side_tall");
        if (!xy) ModelHelper.createWallModel(blockStateModelGenerator, blockBuilder.getTexture(), postModelId, sideModelId, sideTallModelId);
        else ModelHelper.createWallXYModel(blockStateModelGenerator, blockBuilder.getTexture(), parent.getTexture(), postModelId, sideModelId, sideTallModelId);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createWallBlockState(blockBuilder.getBlock(), postModelId, sideModelId, sideTallModelId));
    }

    public static void createDoorBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier topTextureId, Identifier bottomTextureId) {
        TextureMap textureMap = TextureMap.topBottom(topTextureId, bottomTextureId);
        Identifier bottomLeftModelId = Models.DOOR_BOTTOM_LEFT.upload(blockBuilder.getBlock(), textureMap, blockStateModelGenerator.modelCollector);
        Identifier bottomLeftOpenModelId = Models.DOOR_BOTTOM_LEFT_OPEN.upload(blockBuilder.getBlock(), textureMap, blockStateModelGenerator.modelCollector);
        Identifier bottomRightModelId = Models.DOOR_BOTTOM_RIGHT.upload(blockBuilder.getBlock(), textureMap, blockStateModelGenerator.modelCollector);
        Identifier bottomRightOpenModelId = Models.DOOR_BOTTOM_RIGHT_OPEN.upload(blockBuilder.getBlock(), textureMap, blockStateModelGenerator.modelCollector);
        Identifier topLeftModelId = Models.DOOR_TOP_LEFT.upload(blockBuilder.getBlock(), textureMap, blockStateModelGenerator.modelCollector);
        Identifier topLeftOpenModelId = Models.DOOR_TOP_LEFT_OPEN.upload(blockBuilder.getBlock(), textureMap, blockStateModelGenerator.modelCollector);
        Identifier topRightModelId = Models.DOOR_TOP_RIGHT.upload(blockBuilder.getBlock(), textureMap, blockStateModelGenerator.modelCollector);
        Identifier topRightOpenModelId = Models.DOOR_TOP_RIGHT_OPEN.upload(blockBuilder.getBlock(), textureMap, blockStateModelGenerator.modelCollector);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createDoorBlockState(blockBuilder.getBlock(), bottomLeftModelId, bottomLeftOpenModelId, bottomRightModelId, bottomRightOpenModelId, topLeftModelId, topLeftOpenModelId, topRightModelId, topRightOpenModelId));
    }
}

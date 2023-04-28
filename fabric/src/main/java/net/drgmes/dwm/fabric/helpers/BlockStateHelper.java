package net.drgmes.dwm.fabric.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.drgmes.dwm.DWM;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.data.client.TextureKey;
import net.minecraft.data.client.TextureMap;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiConsumer;
import java.util.function.Supplier;

public class BlockStateHelper {
    public static void createSimpleBlockState(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier id) {
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(blockBuilder.getBlock(), id));
    }

    public static void createSimpleBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder) {
        ModelHelper.createSimpleModel(blockStateModelGenerator, blockBuilder.getRenderLayer(), blockBuilder.getId(), blockBuilder.getTexture());
        blockStateModelGenerator.registerSimpleState(blockBuilder.getBlock());
    }

    public static void createSimpleBlockStateAndModelWithUniqueSide(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier textureId, TextureKey textureKey) {
        ModelHelper.createSimpleModelWithUniqueSide(blockStateModelGenerator, blockBuilder.getRenderLayer(), blockBuilder.getId(), blockBuilder.getTexture(), textureId, textureKey);
        blockStateModelGenerator.registerSimpleState(blockBuilder.getBlock());
    }

    public static void createRotatableBlockState(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder) {
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(blockBuilder.getBlock());
    }

    public static void createRotatableBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier topTextureId, Identifier sideTextureId, Identifier frontTextureId, Identifier southTextureId, Identifier bottomTextureId) {
        ModelHelper.createRotatableModel(blockStateModelGenerator, blockBuilder.getRenderLayer(), blockBuilder.getId(), topTextureId, sideTextureId, frontTextureId, southTextureId, bottomTextureId);
        blockStateModelGenerator.registerNorthDefaultHorizontalRotation(blockBuilder.getBlock());
    }

    public static void createSolidSlabBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier yTextureId, Identifier parentId) {
        Identifier topModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_top");
        ModelHelper.createSlabModel(blockStateModelGenerator, blockBuilder.getRenderLayer(), blockBuilder.getTexture(), yTextureId, blockBuilder.getId(), topModelId);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(blockBuilder.getBlock(), blockBuilder.getId(), topModelId, parentId));
    }

    public static void createVariedSlabBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier yTextureId) {
        Identifier topModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_top");
        Identifier doubleModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_double");
        ModelHelper.createSlabModelWithDouble(blockStateModelGenerator, blockBuilder.getRenderLayer(), blockBuilder.getTexture(), yTextureId, blockBuilder.getId(), topModelId, doubleModelId);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSlabBlockState(blockBuilder.getBlock(), blockBuilder.getId(), topModelId, doubleModelId));
    }

    public static void createStairsBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier yTextureId) {
        Identifier innerModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_inner");
        Identifier outerModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_outer");
        ModelHelper.createStairsModel(blockStateModelGenerator, blockBuilder.getRenderLayer(), blockBuilder.getTexture(), yTextureId, blockBuilder.getId(), innerModelId, outerModelId);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createStairsBlockState(blockBuilder.getBlock(), innerModelId, blockBuilder.getId(), outerModelId));
    }

    public static void createWallBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, @Nullable Identifier yTextureId) {
        Identifier postModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_post");
        Identifier sideModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_side");
        Identifier sideTallModelId = DWM.getIdentifier("block/" + blockBuilder.getName() + "_side_tall");
        if (yTextureId == null) ModelHelper.createWallModel(blockStateModelGenerator, blockBuilder.getRenderLayer(), blockBuilder.getTexture(), postModelId, sideModelId, sideTallModelId);
        else ModelHelper.createWallXYModel(blockStateModelGenerator, blockBuilder.getRenderLayer(), blockBuilder.getTexture(), yTextureId, postModelId, sideModelId, sideTallModelId);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createWallBlockState(blockBuilder.getBlock(), postModelId, sideModelId, sideTallModelId));
    }

    public static void createDoorBlockStateAndModel(BlockStateModelGenerator blockStateModelGenerator, BlockBuilder blockBuilder, Identifier topTextureId, Identifier bottomTextureId) {
        BiConsumer<Identifier, Supplier<JsonElement>> customModelOutput = (id, jsonSupplier) -> {
            blockStateModelGenerator.modelCollector.accept(id, () -> {
                JsonObject jsonObject = (JsonObject) jsonSupplier.get();
                if (blockBuilder.getRenderLayer() == RenderLayer.getTranslucent()) jsonObject.addProperty("render_type", "translucent");
                return jsonObject;
            });
        };

        TextureMap textureMap = TextureMap.topBottom(topTextureId, bottomTextureId);
        Identifier bottomLeftModelId = Models.DOOR_BOTTOM_LEFT.upload(blockBuilder.getBlock(), textureMap, customModelOutput);
        Identifier bottomLeftOpenModelId = Models.DOOR_BOTTOM_LEFT_OPEN.upload(blockBuilder.getBlock(), textureMap, customModelOutput);
        Identifier bottomRightModelId = Models.DOOR_BOTTOM_RIGHT.upload(blockBuilder.getBlock(), textureMap, customModelOutput);
        Identifier bottomRightOpenModelId = Models.DOOR_BOTTOM_RIGHT_OPEN.upload(blockBuilder.getBlock(), textureMap, customModelOutput);
        Identifier topLeftModelId = Models.DOOR_TOP_LEFT.upload(blockBuilder.getBlock(), textureMap, customModelOutput);
        Identifier topLeftOpenModelId = Models.DOOR_TOP_LEFT_OPEN.upload(blockBuilder.getBlock(), textureMap, customModelOutput);
        Identifier topRightModelId = Models.DOOR_TOP_RIGHT.upload(blockBuilder.getBlock(), textureMap, customModelOutput);
        Identifier topRightOpenModelId = Models.DOOR_TOP_RIGHT_OPEN.upload(blockBuilder.getBlock(), textureMap, customModelOutput);
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createDoorBlockState(blockBuilder.getBlock(), bottomLeftModelId, bottomLeftOpenModelId, bottomRightModelId, bottomRightOpenModelId, topLeftModelId, topLeftOpenModelId, topRightModelId, topRightOpenModelId));
    }
}

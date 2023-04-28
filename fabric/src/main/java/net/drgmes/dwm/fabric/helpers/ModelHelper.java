package net.drgmes.dwm.fabric.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.drgmes.dwm.DWM;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.SimpleModelSupplier;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class ModelHelper {
    public static void createSimpleModel(BlockStateModelGenerator blockStateModelGenerator, RenderLayer renderLayer, Identifier modelId, Identifier textureId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_CUBE_ALL);
        modelSupplier.setRenderType(renderLayer);
        modelSupplier.addTexture("all", textureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createSimpleModelWithUniqueSide(BlockStateModelGenerator blockStateModelGenerator, RenderLayer renderLayer, Identifier modelId, Identifier textureId, Identifier topTextureId, TextureKey textureKey) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_ORIENTABLE);
        modelSupplier.setRenderType(renderLayer);
        modelSupplier.addTexture("all", textureId);
        modelSupplier.addTexture("side", textureId);
        modelSupplier.addTexture("front", textureId);
        modelSupplier.addTexture(textureKey.getName(), topTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createRotatableModel(BlockStateModelGenerator blockStateModelGenerator, RenderLayer renderLayer, Identifier modelId, Identifier topTextureId, Identifier sideTextureId, Identifier frontTextureId, Identifier southTextureId, Identifier bottomTextureId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_ORIENTABLE);
        modelSupplier.setRenderType(renderLayer);
        modelSupplier.addTexture("top", topTextureId);
        modelSupplier.addTexture("side", sideTextureId);
        modelSupplier.addTexture("front", frontTextureId);
        modelSupplier.addTexture("south", southTextureId);
        modelSupplier.addTexture("bottom", bottomTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createSlabModel(BlockStateModelGenerator blockStateModelGenerator, RenderLayer renderLayer, Identifier xTextureId, Identifier yTextureId, Identifier modelId, Identifier topModelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_SLAB);
        modelSupplier.setRenderType(renderLayer);
        modelSupplier.addTexture("side", xTextureId);
        modelSupplier.addTexture("bottom", yTextureId);
        modelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);

        TexturedModelSupplier topModelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_SLAB_TOP);
        topModelSupplier.addTexture("side", xTextureId);
        topModelSupplier.addTexture("bottom", yTextureId);
        topModelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(topModelId, topModelSupplier);
    }

    public static void createSlabModelWithDouble(BlockStateModelGenerator blockStateModelGenerator, RenderLayer renderLayer, Identifier xTextureId, Identifier yTextureId, Identifier modelId, Identifier topModelId, Identifier doubleModelId) {
        createSlabModel(blockStateModelGenerator, renderLayer, xTextureId, yTextureId, modelId, topModelId);

        TexturedModelSupplier doubleModelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_CUBE_BOTTOM_TOP);
        doubleModelSupplier.addTexture("side", xTextureId);
        doubleModelSupplier.addTexture("bottom", yTextureId);
        doubleModelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(doubleModelId, doubleModelSupplier);
    }

    public static void createStairsModel(BlockStateModelGenerator blockStateModelGenerator, RenderLayer renderLayer, Identifier xTextureId, Identifier yTextureId, Identifier modelId, Identifier innerModelId, Identifier outerModelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_STAIRS);
        modelSupplier.setRenderType(renderLayer);
        modelSupplier.addTexture("side", xTextureId);
        modelSupplier.addTexture("bottom", yTextureId);
        modelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);

        TexturedModelSupplier innerModelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_STAIRS_INNER);
        innerModelSupplier.addTexture("side", xTextureId);
        innerModelSupplier.addTexture("bottom", yTextureId);
        innerModelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(innerModelId, innerModelSupplier);

        TexturedModelSupplier outerModelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_STAIRS_OUTER);
        outerModelSupplier.addTexture("side", xTextureId);
        outerModelSupplier.addTexture("bottom", yTextureId);
        outerModelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(outerModelId, outerModelSupplier);
    }

    public static void createWallModel(BlockStateModelGenerator blockStateModelGenerator, RenderLayer renderLayer, Identifier textureId, Identifier postModelId, Identifier sideModelId, Identifier sideTallModelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_WALL_POST);
        modelSupplier.setRenderType(renderLayer);
        modelSupplier.addTexture("wall", textureId);
        blockStateModelGenerator.modelCollector.accept(postModelId, modelSupplier);

        TexturedModelSupplier sideModelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_WALL_SIDE);
        sideModelSupplier.addTexture("wall", textureId);
        blockStateModelGenerator.modelCollector.accept(sideModelId, sideModelSupplier);

        TexturedModelSupplier sideTallModelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_WALL_SIDE_TALL);
        sideTallModelSupplier.addTexture("wall", textureId);
        blockStateModelGenerator.modelCollector.accept(sideTallModelId, sideTallModelSupplier);
    }

    public static void createWallXYModel(BlockStateModelGenerator blockStateModelGenerator, RenderLayer renderLayer, Identifier xTextureId, Identifier yTextureId, Identifier postModelId, Identifier sideModelId, Identifier sideTallModelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_WALL_XY_POST);
        modelSupplier.setRenderType(renderLayer);
        modelSupplier.addTexture("wall_x", xTextureId);
        modelSupplier.addTexture("wall_y", yTextureId);
        blockStateModelGenerator.modelCollector.accept(postModelId, modelSupplier);

        TexturedModelSupplier sideModelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_WALL_XY_SIDE);
        sideModelSupplier.addTexture("wall_x", xTextureId);
        sideModelSupplier.addTexture("wall_y", yTextureId);
        blockStateModelGenerator.modelCollector.accept(sideModelId, sideModelSupplier);

        TexturedModelSupplier sideTallModelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_WALL_XY_SIDE_TALL);
        sideTallModelSupplier.addTexture("wall_x", xTextureId);
        sideTallModelSupplier.addTexture("wall_y", yTextureId);
        blockStateModelGenerator.modelCollector.accept(sideTallModelId, sideTallModelSupplier);
    }

    public static void createWallBlockItemModel(BlockStateModelGenerator blockStateModelGenerator, Identifier textureId, Identifier modelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_WALL_INVENTORY);
        modelSupplier.addTexture("wall", textureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createWallXYBlockItemModel(BlockStateModelGenerator blockStateModelGenerator, Identifier xTextureId, Identifier yTextureId, Identifier modelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.BLOCK_WALL_XY_INVENTORY);
        modelSupplier.addTexture("wall_x", xTextureId);
        modelSupplier.addTexture("wall_y", yTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createGeneratedBlockItemModel(BlockStateModelGenerator blockStateModelGenerator, Identifier textureId, Identifier modelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(DWM.MODELS.ITEM_GENERATED);
        modelSupplier.addTexture("layer0", textureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static class TexturedModelSupplier extends SimpleModelSupplier {
        private final Map<String, Identifier> textures = new HashMap<>();
        private RenderLayer renderLayer;

        public TexturedModelSupplier(Identifier parent) {
            super(parent);
        }

        @Override
        public JsonElement get() {
            JsonObject jsonObject = (JsonObject) super.get();
            JsonObject texturesJsonObject = new JsonObject();
            if (this.renderLayer == RenderLayer.getTranslucent()) jsonObject.addProperty("render_type", "translucent");
            textures.forEach((textureName, id) -> texturesJsonObject.addProperty(textureName, id.toString()));
            jsonObject.add("textures", texturesJsonObject);
            return jsonObject;
        }

        public void addTexture(String textureName, Identifier id) {
            this.textures.put(textureName, id);
        }

        public void setRenderType(RenderLayer renderLayer) {
            this.renderLayer = renderLayer;
        }
    }
}

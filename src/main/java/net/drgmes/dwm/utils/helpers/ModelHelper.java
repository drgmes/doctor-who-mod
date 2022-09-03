package net.drgmes.dwm.utils.helpers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.drgmes.dwm.DWM;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.data.client.SimpleModelSupplier;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class ModelHelper {
    public static final Identifier BUILTIN_ENTITY = new Identifier("builtin/entity");
    public static final Identifier ITEM_GENERATED = new Identifier("item/generated");
    public static final Identifier BLOCK_CUBE_ALL = new Identifier("block/cube_all");
    public static final Identifier BLOCK_CUBE_BOTTOM_TOP = new Identifier("block/cube_bottom_top");
    public static final Identifier BLOCK_ORIENTABLE = new Identifier("block/orientable");
    public static final Identifier BLOCK_SLAB = new Identifier("block/slab");
    public static final Identifier BLOCK_SLAB_TOP = new Identifier("block/slab_top");
    public static final Identifier BLOCK_STAIRS = new Identifier("block/stairs");
    public static final Identifier BLOCK_STAIRS_INNER = new Identifier("block/inner_stairs");
    public static final Identifier BLOCK_STAIRS_OUTER = new Identifier("block/outer_stairs");
    public static final Identifier BLOCK_WALL_POST = new Identifier("block/template_wall_post");
    public static final Identifier BLOCK_WALL_SIDE = new Identifier("block/template_wall_side");
    public static final Identifier BLOCK_WALL_SIDE_TALL = new Identifier("block/template_wall_side_tall");
    public static final Identifier BLOCK_WALL_INVENTORY = new Identifier("block/wall_inventory");

    public static final Identifier BLOCK_INVISIBLE = DWM.getIdentifier("block/templates/invisible");
    public static final Identifier BLOCK_WALL_XY_POST = DWM.getIdentifier("block/templates/template_wall_xy_post");
    public static final Identifier BLOCK_WALL_XY_SIDE = DWM.getIdentifier("block/templates/template_wall_xy_side");
    public static final Identifier BLOCK_WALL_XY_SIDE_TALL = DWM.getIdentifier("block/templates/template_wall_xy_side_tall");
    public static final Identifier BLOCK_WALL_XY_INVENTORY = DWM.getIdentifier("block/templates/wall_xy_inventory");

    public static void createSimpleModel(BlockStateModelGenerator blockStateModelGenerator, Identifier modelId, Identifier textureId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(BLOCK_CUBE_ALL);
        modelSupplier.addTexture("all", textureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createSimpleModel(BlockStateModelGenerator blockStateModelGenerator, Identifier modelId, Identifier xTextureId, Identifier yTextureId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(BLOCK_ORIENTABLE);
        modelSupplier.addTexture("all", xTextureId);
        modelSupplier.addTexture("front", xTextureId);
        modelSupplier.addTexture("side", xTextureId);
        modelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createRotatableModel(BlockStateModelGenerator blockStateModelGenerator, Identifier modelId, Identifier topTextureId, Identifier sideTextureId, Identifier frontTextureId, Identifier southTextureId, Identifier bottomTextureId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(ModelHelper.BLOCK_ORIENTABLE);
        modelSupplier.addTexture("top", topTextureId);
        modelSupplier.addTexture("side", sideTextureId);
        modelSupplier.addTexture("front", frontTextureId);
        modelSupplier.addTexture("south", southTextureId);
        modelSupplier.addTexture("bottom", bottomTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createSlabModel(BlockStateModelGenerator blockStateModelGenerator, Identifier xTextureId, Identifier yTextureId, Identifier modelId, Identifier topModelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(BLOCK_SLAB);
        modelSupplier.addTexture("side", xTextureId);
        modelSupplier.addTexture("bottom", yTextureId);
        modelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);

        TexturedModelSupplier topModelSupplier = new TexturedModelSupplier(BLOCK_SLAB_TOP);
        topModelSupplier.addTexture("side", xTextureId);
        topModelSupplier.addTexture("bottom", yTextureId);
        topModelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(topModelId, topModelSupplier);
    }

    public static void createSlabModelWithDouble(BlockStateModelGenerator blockStateModelGenerator, Identifier xTextureId, Identifier yTextureId, Identifier modelId, Identifier topModelId, Identifier doubleModelId) {
        createSlabModel(blockStateModelGenerator, xTextureId, yTextureId, modelId, topModelId);

        TexturedModelSupplier doubleModelSupplier = new TexturedModelSupplier(BLOCK_CUBE_BOTTOM_TOP);
        doubleModelSupplier.addTexture("side", xTextureId);
        doubleModelSupplier.addTexture("bottom", yTextureId);
        doubleModelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(doubleModelId, doubleModelSupplier);
    }

    public static void createStairsModel(BlockStateModelGenerator blockStateModelGenerator, Identifier xTextureId, Identifier yTextureId, Identifier modelId, Identifier innerModelId, Identifier outerModelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(BLOCK_STAIRS);
        modelSupplier.addTexture("side", xTextureId);
        modelSupplier.addTexture("bottom", yTextureId);
        modelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);

        TexturedModelSupplier innerModelSupplier = new TexturedModelSupplier(BLOCK_STAIRS_INNER);
        innerModelSupplier.addTexture("side", xTextureId);
        innerModelSupplier.addTexture("bottom", yTextureId);
        innerModelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(innerModelId, innerModelSupplier);

        TexturedModelSupplier outerModelSupplier = new TexturedModelSupplier(BLOCK_STAIRS_OUTER);
        outerModelSupplier.addTexture("side", xTextureId);
        outerModelSupplier.addTexture("bottom", yTextureId);
        outerModelSupplier.addTexture("top", yTextureId);
        blockStateModelGenerator.modelCollector.accept(outerModelId, outerModelSupplier);
    }

    public static void createWallModel(BlockStateModelGenerator blockStateModelGenerator, Identifier textureId, Identifier postModelId, Identifier sideModelId, Identifier sideTallModelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(BLOCK_WALL_POST);
        modelSupplier.addTexture("wall", textureId);
        blockStateModelGenerator.modelCollector.accept(postModelId, modelSupplier);

        TexturedModelSupplier sideModelSupplier = new TexturedModelSupplier(BLOCK_WALL_SIDE);
        sideModelSupplier.addTexture("wall", textureId);
        blockStateModelGenerator.modelCollector.accept(sideModelId, sideModelSupplier);

        TexturedModelSupplier sideTallModelSupplier = new TexturedModelSupplier(BLOCK_WALL_SIDE_TALL);
        sideTallModelSupplier.addTexture("wall", textureId);
        blockStateModelGenerator.modelCollector.accept(sideTallModelId, sideTallModelSupplier);
    }

    public static void createWallXYModel(BlockStateModelGenerator blockStateModelGenerator, Identifier xTextureId, Identifier yTextureId, Identifier postModelId, Identifier sideModelId, Identifier sideTallModelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(BLOCK_WALL_XY_POST);
        modelSupplier.addTexture("wall_x", xTextureId);
        modelSupplier.addTexture("wall_y", yTextureId);
        blockStateModelGenerator.modelCollector.accept(postModelId, modelSupplier);

        TexturedModelSupplier sideModelSupplier = new TexturedModelSupplier(BLOCK_WALL_XY_SIDE);
        sideModelSupplier.addTexture("wall_x", xTextureId);
        sideModelSupplier.addTexture("wall_y", yTextureId);
        blockStateModelGenerator.modelCollector.accept(sideModelId, sideModelSupplier);

        TexturedModelSupplier sideTallModelSupplier = new TexturedModelSupplier(BLOCK_WALL_XY_SIDE_TALL);
        sideTallModelSupplier.addTexture("wall_x", xTextureId);
        sideTallModelSupplier.addTexture("wall_y", yTextureId);
        blockStateModelGenerator.modelCollector.accept(sideTallModelId, sideTallModelSupplier);
    }

    public static void createWallItemModel(BlockStateModelGenerator blockStateModelGenerator, Identifier textureId, Identifier modelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(BLOCK_WALL_INVENTORY);
        modelSupplier.addTexture("wall", textureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createWallXYItemModel(BlockStateModelGenerator blockStateModelGenerator, Identifier xTextureId, Identifier yTextureId, Identifier modelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(BLOCK_WALL_XY_INVENTORY);
        modelSupplier.addTexture("wall_x", xTextureId);
        modelSupplier.addTexture("wall_y", yTextureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createGeneratedItemModel(BlockStateModelGenerator blockStateModelGenerator, Identifier textureId, Identifier modelId) {
        TexturedModelSupplier modelSupplier = new TexturedModelSupplier(ITEM_GENERATED);
        modelSupplier.addTexture("layer0", textureId);
        blockStateModelGenerator.modelCollector.accept(modelId, modelSupplier);
    }

    public static void createItemModel(ItemModelGenerator itemModelGenerator, Item item, Identifier id) {
        itemModelGenerator.register(item, new Model(Optional.of(id), Optional.empty()));
    }

    public static void createItemModel(ItemModelGenerator itemModelGenerator, Item item, String id) {
        createItemModel(itemModelGenerator, item, DWM.getIdentifier(id));
    }

    public static class TexturedModelSupplier extends SimpleModelSupplier {
        private final Map<String, Identifier> textures = new HashMap<>();

        public TexturedModelSupplier(Identifier parent) {
            super(parent);
        }

        @Override
        public JsonElement get() {
            JsonObject jsonObject = (JsonObject) super.get();
            JsonObject texturesJsonObject = new JsonObject();
            textures.forEach((textureName, id) -> texturesJsonObject.addProperty(textureName, id.toString()));
            jsonObject.add("textures", texturesJsonObject);
            return jsonObject;
        }

        public void addTexture(String textureName, Identifier id) {
            this.textures.put(textureName, id);
        }
    }
}

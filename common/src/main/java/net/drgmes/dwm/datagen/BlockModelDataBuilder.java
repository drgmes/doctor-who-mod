package net.drgmes.dwm.datagen;

import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.data.client.TextureKey;
import net.minecraft.util.Identifier;

import java.util.HashMap;
import java.util.Map;

public class BlockModelDataBuilder {
    public enum BlockType {
        SIMPLE,
        SIMPLE_WITH_MODEL,
        SIMPLE_WITH_MODEL_AND_UNIQUE_SIDE,
        ROTATABLE_WITHOUT_MODEL,
        ROTATABLE,
        SOLID_SLAB,
        VARIED_SLAB,
        STAIRS,
        WALL,
        DOOR,
    }

    public enum ItemType {
        NONE,
        PARENTED,
        GENERATED,
        WALL,
        WALL_XY,
    }

    public final Map<TextureKey, Identifier> blockTextures = new HashMap<>();
    public final Map<TextureKey, Identifier> itemTextures = new HashMap<>();
    public final BlockBuilder blockBuilder;
    public final BlockType blockType;
    public final ItemType itemType;

    public BlockModelDataBuilder(BlockBuilder blockBuilder, BlockType blockType, ItemType itemType) {
        this.blockBuilder = blockBuilder;
        this.blockType = blockType;
        this.itemType = itemType;
    }

    public BlockModelDataBuilder(BlockBuilder blockBuilder, BlockType blockType) {
        this(blockBuilder, blockType, ItemType.NONE);
    }

    public BlockModelDataBuilder addBlockTexture(TextureKey textureKey, Identifier textureId) {
        this.blockTextures.put(textureKey, textureId);
        return this;
    }

    public BlockModelDataBuilder addItemTexture(TextureKey textureKey, Identifier textureId) {
        this.itemTextures.put(textureKey, textureId);
        return this;
    }
}

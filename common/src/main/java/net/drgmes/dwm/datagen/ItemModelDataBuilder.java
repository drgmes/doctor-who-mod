package net.drgmes.dwm.datagen;

import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

public class ItemModelDataBuilder {
    public enum ItemType {
        PARENTED,
        GENERATED,
        HANDHELD,
    }

    public final Item item;
    public final Identifier modelId;
    public final Identifier texture;
    public final ItemType itemType;

    public ItemModelDataBuilder(Item item, Identifier modelId, Identifier texture, ItemType itemType) {
        this.item = item;
        this.modelId = modelId;
        this.texture = texture;
        this.itemType = itemType;
    }
}

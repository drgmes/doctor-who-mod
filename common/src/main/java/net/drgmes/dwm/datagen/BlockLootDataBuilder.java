package net.drgmes.dwm.datagen;

import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.minecraft.item.Item;

public class BlockLootDataBuilder {
    public enum LootType {
        SELF,
        ORE,
        DOOR,
    }

    public final BlockBuilder blockBuilder;
    public final LootType lootType;
    public final Item lootItem;

    public BlockLootDataBuilder(BlockBuilder blockBuilder, LootType lootType, Item lootItem) {
        this.blockBuilder = blockBuilder;
        this.lootType = lootType;
        this.lootItem = lootItem;
    }

    public BlockLootDataBuilder(BlockBuilder blockBuilder, LootType lootType) {
        this(blockBuilder, lootType, null);
    }
}

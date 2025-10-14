package net.drgmes.dwm.utils.builders;

import net.minecraft.block.Block;
import net.minecraft.item.DyeItem;
import net.minecraft.item.Item;
import net.minecraft.item.Items;

import java.util.HashMap;
import java.util.List;
import java.util.function.BiFunction;

public class ColoredBlockBuilders {
    private static final List<Item> DYE_ITEMS = List.of(
        Items.BLACK_DYE, Items.BLUE_DYE, Items.BROWN_DYE, Items.CYAN_DYE, Items.GRAY_DYE, Items.GREEN_DYE,
        Items.LIGHT_BLUE_DYE, Items.LIME_DYE, Items.MAGENTA_DYE, Items.ORANGE_DYE, Items.PINK_DYE, Items.PURPLE_DYE,
        Items.RED_DYE, Items.LIGHT_GRAY_DYE, Items.WHITE_DYE, Items.YELLOW_DYE
    );

    public final HashMap<Item, BlockBuilder> blockBuilders = new HashMap<>();

    public <T extends BlockBuilder> ColoredBlockBuilders(String baseName, BiFunction<String, Item, BlockBuilder> blockBuilderSupplier) {
        for (Item dyeItem : DYE_ITEMS) {
            String color = ((DyeItem) dyeItem).getColor().getName();
            blockBuilders.put(dyeItem, blockBuilderSupplier.apply(baseName + "_" + color, dyeItem));
        }
    }

    public BlockBuilder get(Item dyeItem) {
        return this.blockBuilders.get(dyeItem);
    }

    public Block getBlock(Item dyeItem) {
        return this.blockBuilders.get(dyeItem).getBlock();
    }
}

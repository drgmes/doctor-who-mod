package net.drgmes.dwm.utils.helpers;

import net.drgmes.dwm.DWM;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Model;
import net.minecraft.item.Item;
import net.minecraft.util.Identifier;

import java.util.Optional;

public class ModelHelper {
    public static final Identifier BUILTIN_ENTITY = new Identifier("builtin/entity");
    public static final Identifier BLOCK_INVISIBLE = DWM.getIdentifier("block/invisible");

    public static void createBlockStateWithModel(BlockStateModelGenerator blockStateModelGenerator, Block block, Identifier id) {
        blockStateModelGenerator.blockStateCollector.accept(BlockStateModelGenerator.createSingletonBlockState(block, id));
    }

    public static void createBlockStateWithModel(BlockStateModelGenerator blockStateModelGenerator, Block block, String id) {
        createBlockStateWithModel(blockStateModelGenerator, block, DWM.getIdentifier(id));
    }

    public static void createItemModel(ItemModelGenerator itemModelGenerator, Item item, Identifier id) {
        itemModelGenerator.register(item, new Model(Optional.of(id), Optional.empty()));
    }

    public static void createItemModel(ItemModelGenerator itemModelGenerator, Item item, String id) {
        createItemModel(itemModelGenerator, item, DWM.getIdentifier(id));
    }
}

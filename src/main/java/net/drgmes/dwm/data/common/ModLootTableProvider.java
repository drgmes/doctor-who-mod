package net.drgmes.dwm.data.common;

import com.google.common.collect.ImmutableList;
import com.mojang.datafixers.util.Pair;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.block.BlockBuilder;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.loot.BlockLoot;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.LootTables;
import net.minecraft.world.level.storage.loot.ValidationContext;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSet;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(DataGenerator generator) {
        super(generator);
    }

    @Override
    protected List<Pair<Supplier<Consumer<BiConsumer<ResourceLocation, LootTable.Builder>>>, LootContextParamSet>> getTables() {
        return ImmutableList.of(
            Pair.of(ModBlockLootTable::new, LootContextParamSets.BLOCK)
        );
    }

    @Override
    protected void validate(Map<ResourceLocation, LootTable> map, ValidationContext validationContext) {
        map.forEach((resourceLocation, lootTable) -> LootTables.validate(validationContext, resourceLocation, lootTable));
    }

    public static class ModBlockLootTable extends BlockLoot {
        public void addOreDrop(Block block, Item item) {
            this.add(block, (b) -> createOreDrop(b, item));
        }

        public void addOreSaturatedDrop(Block block, Item item) {
            this.add(block, (b) -> {
                return createSilkTouchDispatchTable(block, applyExplosionDecay(block,
                    LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(2.0F, 5.0F)))
                        .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
                ));
            });
        }

        public void addDoorDrop(Block block) {
            this.add(block, (b) -> createDoorTable(b));
        }

        @Override
        protected void addTables() {
            for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
                blockBuilder.registerLootTable(this);
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return ModBlocks.BLOCK_BUILDERS.stream()
                .filter((bb) -> !bb.isDropDisabled)
                .map((bb) -> bb.get())
                .collect(Collectors.toList());
        }
    }
}

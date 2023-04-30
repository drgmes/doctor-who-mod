package net.drgmes.dwm.fabric.datagen.common;

import net.drgmes.dwm.datagen.BlockLootDataBuilder;
import net.drgmes.dwm.setup.ModBlocks;
import net.drgmes.dwm.utils.builders.BlockBuilder;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.data.server.loottable.BlockLootTableGenerator;

public class ModLootTableProvider extends FabricBlockLootTableProvider {
    public ModLootTableProvider(FabricDataOutput output) {
        super(output);
    }

    @Override
    public void generate() {
        for (BlockBuilder blockBuilder : ModBlocks.BLOCK_BUILDERS) {
            BlockLootDataBuilder blockLootDataBuilder = blockBuilder.getBlockLootDataBuilder();

            if (blockLootDataBuilder == null) {
                this.addDrop(blockBuilder.getBlock(), BlockLootTableGenerator.dropsNothing());
                continue;
            }

            switch (blockLootDataBuilder.lootType) {
                default -> this.addDrop(blockBuilder.getBlock());
                case ORE -> this.addDrop(blockBuilder.getBlock(), this.oreDrops(blockBuilder.getBlock(), blockLootDataBuilder.lootItem));
                case DOOR -> this.addDrop(blockBuilder.getBlock(), this.doorDrops(blockBuilder.getBlock()));
            }
        }
    }
}
